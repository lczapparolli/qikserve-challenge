package br.zapparolli.service;

import br.zapparolli.entity.Promotion;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.repository.BasketRepository;
import br.zapparolli.repository.PromotionRepository;
import br.zapparolli.resource.client.ProductsRestClient;
import br.zapparolli.utils.DatabaseUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_2;
import static br.zapparolli.utils.AssertionUtils.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the basket service
 *
 * @author lczapparolli
 */
@QuarkusTest
public class BasketServiceTest {

    @Inject BasketService basketService;
    @Inject BasketRepository basketRepository;
    @Inject PromotionRepository promotionRepository;
    @Inject DatabaseUtils databaseUtils;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    @Transactional
    public void setup() {
        databaseUtils.clearDB();
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
     * Check if the promotion is applied to the item
     */
    @Test
    @Transactional
    public void addItemCheckPromotionTest() {
        // Creates a promotion
        var promotion = Promotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.TWO)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionRepository.persist(promotion);

        var newBasketItem = NewBasketItem.builder()
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .customerId("PROMOTION_TEST")
                .build();

        // Inserts an item
        var basket = basketService.addItem(newBasketItem);
        // Checks if the promotion is no applied
        assertNull(basket.getItems().get(0).getPromotion());

        // Increments the item
        basket = basketService.addItem(newBasketItem);
        // Checks if the promotion is now applied
        assertNotNull(basket.getItems().get(0).getPromotion());
        assertEquals(promotion, basket.getItems().get(0).getPromotion());

    }

    /**
     * Check the checkout process
     */
    @Test
    public void checkoutTest() {
        // Creates a new basket
        basketService.addItem(NewBasketItem.builder()
                .customerId("CHECKOUT_BASKET")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build());

        // Closes the basket
        var closedBasket = basketService.checkout("CHECKOUT_BASKET");
        assertNotNull(closedBasket);
        assertFalse(closedBasket.isOpen());
    }

    /**
     * Check the result if there is no open basket for the given customer
     */
    @Test
    public void checkoutNoOpenBasketTest() {
        assertThrows(ErrorMessage.ERROR_NO_OPEN_BASKET, () -> basketService.checkout("NO_OPEN_BASKET"));
    }

    /**
     * Check the method to get the currently open basket
     */
    @Test
    public void getOpenBasketTest() {
        // Creates a new basket
        var basket = basketService.addItem(NewBasketItem.builder()
                .customerId("GET_BASKET")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build());

        // Checks if is the same inserted
        var openBasket = basketService.getOpenBasket("GET_BASKET");
        assertEquals(basket.getId(), openBasket.getId());
    }

    /**
     * Check the result if there is no open basket for the given customer
     */
    @Test
    public void getOpenBasketNoOpenBasketTest() {
        assertThrows(ErrorMessage.ERROR_NO_OPEN_BASKET, () -> basketService.getOpenBasket("NO_OPEN_BASKET"));
    }

}
