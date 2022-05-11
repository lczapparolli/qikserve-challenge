package br.zapparolli.service;

import br.zapparolli.exception.BasketException;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.repository.BasketRepository;
import br.zapparolli.resource.client.ProductsRestClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.inject.Inject;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the basket service
 *
 * @author lczapparolli
 */
@QuarkusTest
public class BasketServiceTest {

    @Inject BasketService basketService;
    @Inject BasketRepository basketRepository;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    public void setup() {
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the addition of an item on a non-existing basket
     */
    @Test
    public void addItemNewBasketTest() {
        var basketCount = basketRepository.count();

        var newBasketItem = NewBasketItem.builder()
                        .customerId("NEW_BASKET_CUSTOMER")
                        .productId(PRODUCT_1.getId())
                        .amount(BigInteger.ONE)
                        .build();

        var insertedBasket = basketService.addItem(newBasketItem);
        // Checks the basket count
        assertEquals(basketCount + 1, basketRepository.count());

        // Checks the basket data
        assertNotNull(insertedBasket);
        assertNotNull(insertedBasket.getId());
        assertEquals(newBasketItem.getCustomerId(), insertedBasket.getCustomerId());
        assertTrue(insertedBasket.isOpen());

        // Checks the item data
        assertEquals(1, insertedBasket.getItems().size());
        assertEquals(newBasketItem.getProductId(), insertedBasket.getItems().get(0).getProductId());
        assertEquals(newBasketItem.getAmount(), insertedBasket.getItems().get(0).getAmount());
        assertEquals(PRODUCT_1.getPrice(), insertedBasket.getItems().get(0).getUnitPrice());
    }

    /**
     * Check the addition of an item on an existing basket
     */
    @Test
    public void addItemExistingBasketTest() {
        // Creates a basket
        basketService.addItem(NewBasketItem.builder()
                .customerId("EXISTING_BASKET_CUSTOMER")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build());
        var basketCount = basketRepository.count();

        // Adds a new item
        var newBasketItem = NewBasketItem.builder()
                .customerId("EXISTING_BASKET_CUSTOMER")
                .productId(PRODUCT_2.getId())
                .amount(BigInteger.ONE)
                .build();
        var insertedBasket = basketService.addItem(newBasketItem);

        // Checks the basket count
        assertEquals(basketCount, basketRepository.count());

        // Checks the basket data
        assertNotNull(insertedBasket);
        assertNotNull(insertedBasket.getId());
        assertEquals(newBasketItem.getCustomerId(), insertedBasket.getCustomerId());
        assertTrue(insertedBasket.isOpen());

        // Checks if the list is incremented
        assertEquals(2, insertedBasket.getItems().size());
    }

    /**
     * Check the addition of an item on a basket that already have the same product
     */
    @Test
    public void addItemIncrementTest() {
        // Creates the basket and add the same product again
        basketService.addItem(NewBasketItem.builder()
                .customerId("EXISTING_BASKET_CUSTOMER")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build());

        var newBasketItem = NewBasketItem.builder()
                .customerId("EXISTING_BASKET_CUSTOMER")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build();
        var insertedBasket = basketService.addItem(newBasketItem);

        // Checks the basket data
        assertNotNull(insertedBasket);
        assertNotNull(insertedBasket.getId());
        assertEquals(newBasketItem.getCustomerId(), insertedBasket.getCustomerId());
        assertTrue(insertedBasket.isOpen());

        // Checks the item data
        assertEquals(1, insertedBasket.getItems().size());
        assertEquals(newBasketItem.getProductId(), insertedBasket.getItems().get(0).getProductId());
        assertEquals(PRODUCT_1.getPrice(), insertedBasket.getItems().get(0).getUnitPrice());
        // Checks if the amount was incremented
        assertEquals(BigInteger.TWO, insertedBasket.getItems().get(0).getAmount());
    }

    /**
     * Check the addition of an invalid product
     */
    @Test
    public void addItemNotFoundTest() {
        var newBasketItem = NewBasketItem.builder()
                .customerId("INVALID_PRODUCT_BASKET")
                .productId("INVALID_ID")
                .amount(BigInteger.ONE)
                .build();

        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_PRODUCT_NOT_FOUND, () -> basketService.addItem(newBasketItem));
    }

    /**
     * Check the addition of an item with an invalid amount
     */
    @Test
    public void addItemInvalidAmountTest() {
        var nullAmountItem = NewBasketItem.builder()
                .customerId("INVALID_AMOUNT_BASKET")
                .productId(PRODUCT_1.getId())
                .amount(null)
                .build();
        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_INVALID_AMOUNT, () -> basketService.addItem(nullAmountItem));

        var zeroAmountItem = NewBasketItem.builder()
                .customerId("INVALID_AMOUNT_BASKET")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ZERO)
                .build();
        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_INVALID_AMOUNT, () -> basketService.addItem(zeroAmountItem));

        var negativeAmountItem = NewBasketItem.builder()
                .customerId("INVALID_AMOUNT_BASKET")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.valueOf(-1))
                .build();
        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_INVALID_AMOUNT, () -> basketService.addItem(negativeAmountItem));
    }

    /**
     * Check the addition of an item with an invalid customer id
     */
    @Test
    public void addItemInvalidCustomerIdTest() {
        var nullCustomerItem = NewBasketItem.builder()
                .customerId(null)
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build();
        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_INVALID_CUSTOMER_ID, () -> basketService.addItem(nullCustomerItem));

        var emptyCustomerItem = NewBasketItem.builder()
                .customerId("")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build();
        // Checks if the service throws the excepted exception
        assertThrows(ErrorMessage.ERROR_INVALID_CUSTOMER_ID, () -> basketService.addItem(emptyCustomerItem));
    }

    /**
     * Check if the method throws a {@link BasketException} with the expected message
     *
     * @param errorMessage The expect error message
     * @param method The method to be executed
     */
    private void assertThrows(ErrorMessage errorMessage, Executable method) {
        try {
            // Executes the method
            method.execute();
            // Fails if the method does not throw
            fail("Should have thrown a BasketException");
        } catch (BasketException exception) {
            // Checks if the error message is the same
            assertEquals(errorMessage, exception.getErrorMessage());
        } catch (Throwable e) {
            // Fails if the exception is different of BasketException
            fail(String.format("Should have thrown a BasketException, but throwed a '%s'", e.getClass().getName()));
        }
    }

}
