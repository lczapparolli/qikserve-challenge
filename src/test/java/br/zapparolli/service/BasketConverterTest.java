package br.zapparolli.service;

import br.zapparolli.entity.Basket;
import br.zapparolli.entity.BasketItem;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.InsertedBasketItem;
import br.zapparolli.resource.client.ProductsRestClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the basket converter
 *
 * @author lczapparolli
 */
@QuarkusTest
public class BasketConverterTest {

    @Inject
    BasketConverter basketConverter;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    public void setup() {
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the conversion of the basket entity to the model
     */
    @Test
    public void convertBasketTest() {
        // Creates a basket with two items
        var basket = Basket.newBasket("CONVETED_BASKET");
        var basketItem1 = BasketItem.newBasketItem(basket, PRODUCT_1);
        basketItem1.setAmount(BigInteger.ONE);
        var basketItem2 = BasketItem.newBasketItem(basket, PRODUCT_2);
        basketItem2.setAmount(BigInteger.TWO);

        // Converts the object
        var converted = basketConverter.convertBasket(basket);

        // Checks the basket data
        assertNotNull(converted);
        assertEquals(basket.getCustomerId(), converted.getCustomerId());
        assertEquals(basket.getItems().size(), converted.getItems().size());

        // Checks the Item 1 data
        assertEquals(basketItem1.getProductId(), converted.getItems().get(0).getProductId());
        assertEquals(PRODUCT_1.getName(), converted.getItems().get(0).getProductName());
        assertEquals(basketItem1.getUnitPrice(), converted.getItems().get(0).getUnitPrice());
        assertEquals(basketItem1.getAmount(), converted.getItems().get(0).getAmount());
        assertEquals(basketItem1.getUnitPrice().multiply(basketItem1.getAmount()), converted.getItems().get(0).getItemTotal());

        // Checks the Item 2 data
        assertEquals(basketItem2.getProductId(), converted.getItems().get(1).getProductId());
        assertEquals(PRODUCT_2.getName(), converted.getItems().get(1).getProductName());
        assertEquals(basketItem2.getUnitPrice(), converted.getItems().get(1).getUnitPrice());
        assertEquals(basketItem2.getAmount(), converted.getItems().get(1).getAmount());
        assertEquals(basketItem2.getUnitPrice().multiply(basketItem2.getAmount()), converted.getItems().get(1).getItemTotal());

        // Checks the basket total
        var basketTotal = converted.getItems().stream().map(InsertedBasketItem::getItemTotal).reduce(BigInteger.ZERO, BigInteger::add);
        assertEquals(basketTotal, converted.getTotal());
    }
}
