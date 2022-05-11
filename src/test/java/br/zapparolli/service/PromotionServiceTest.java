package br.zapparolli.service;

import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewPromotion;
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
import static br.zapparolli.utils.AssertionUtils.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the Promotion service
 *
 * @author lczapparolli
 */
@QuarkusTest
public class PromotionServiceTest {

    @Inject
    PromotionService promotionService;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    public void setup() {
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the promotion creation
     */
    @Test
    public void createPromotionTest() {
        var newPromotion = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();

        // Creates the promotion
        var promotion = promotionService.createPromotion(newPromotion);

        // Checks the data
        assertNotNull(promotion);
        assertNotNull(promotion.getId());
        assertEquals(newPromotion.getProductId(), promotion.getProductId());
        assertEquals(newPromotion.getMinAmount(), promotion.getMinAmount());
        assertEquals(newPromotion.getUnitDiscount(), promotion.getUnitDiscount());
    }

    /**
     * Check the error if the product is invalid
     */
    @Test
    public void createPromotionInvalidProductTest() {
        var invalidProduct = NewPromotion.builder()
                .productId("INVALID_ID")
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();

        assertThrows(ErrorMessage.ERROR_PRODUCT_NOT_FOUND, () -> promotionService.createPromotion(invalidProduct));
    }

    /**
     * Check the error if the minimum amount id invalid
     */
    @Test
    public void createPromotionInvalidAmountTest() {
        var promotionZeroAmount = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ZERO)
                .unitDiscount(BigInteger.ONE)
                .build();

        assertThrows(ErrorMessage.ERROR_PROMOTION_INVALID_AMOUNT, () -> promotionService.createPromotion(promotionZeroAmount));

        var promotionNegativeAmount = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.valueOf(-1))
                .unitDiscount(BigInteger.ONE)
                .build();

        assertThrows(ErrorMessage.ERROR_PROMOTION_INVALID_AMOUNT, () -> promotionService.createPromotion(promotionNegativeAmount));
    }

    /**
     * Check the error if the discount is invalid
     */
    @Test
    public void createPromotionInvalidDiscountTest() {
        var promotionZeroDiscount = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ONE)
                .unitDiscount(BigInteger.ZERO)
                .build();

        assertThrows(ErrorMessage.ERROR_PROMOTION_INVALID_DISCOUNT, () -> promotionService.createPromotion(promotionZeroDiscount));

        var promotionNegativeDiscount = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ONE)
                .unitDiscount(BigInteger.valueOf(-1))
                .build();

        assertThrows(ErrorMessage.ERROR_PROMOTION_INVALID_DISCOUNT, () -> promotionService.createPromotion(promotionNegativeDiscount));

        var promotionGreaterDiscount = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ONE)
                .unitDiscount(PRODUCT_1.getPrice().add(BigInteger.ONE))
                .build();

        assertThrows(ErrorMessage.ERROR_PROMOTION_GREATER_DISCOUNT, () -> promotionService.createPromotion(promotionGreaterDiscount));
    }

    /**
     * Check the error if there is already a promotion for the given product
     */
    @Test
    public void createPromotionExistentTest() {
        var newPromotion = NewPromotion.builder()
                .productId(PRODUCT_2.getId())
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();

        // Creates the promotion
        var promotion = promotionService.createPromotion(newPromotion);

        // Try to recreate it
        assertThrows(ErrorMessage.ERROR_PROMOTION_ALREADY_EXISTS, () -> promotionService.createPromotion(newPromotion));
    }
}
