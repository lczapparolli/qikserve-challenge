package br.zapparolli.repository;

import br.zapparolli.entity.Promotion;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the Promotion repository
 *
 * @author lczapparolli
 */
@QuarkusTest
public class PromotionRepositoryTest {

    @Inject
    PromotionRepository promotionRepository;

    @BeforeEach
    @Transactional
    public void setup() {
        promotionRepository.deleteAll();
    }

    /**
     * Check the return of the query
     */
    @Test
    @Transactional
    public void findByProductTest() {
        // Creates a promotion for the product
        var insertedPromotion = Promotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ONE)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionRepository.persist(insertedPromotion);

        // Executes the query
        var promoction = promotionRepository.findByProduct(PRODUCT_1.getId());
        assertTrue(promoction.isPresent());
        assertEquals(insertedPromotion, promoction.get());
    }

    /**
     * Check the return of the query when no promotion is found
     */
    @Test
    public void findByProductEmptyTest() {
        var promoction = promotionRepository.findByProduct(PRODUCT_2.getId());
        assertTrue(promoction.isEmpty());
    }

    /**
     * Check the return of the query
     */
    @Test
    @Transactional
    public void findByProductAmountTest() {
        // Creates a promotion for the product
        var insertedPromotion = Promotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ONE)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionRepository.persist(insertedPromotion);

        // Executes the query with the same amount
        var promotion = promotionRepository.findByProductAmount(PRODUCT_1.getId(), BigInteger.ONE);
        assertTrue(promotion.isPresent());
        assertEquals(insertedPromotion, promotion.get());

        // Executes the query with a greater amount
        promotion = promotionRepository.findByProductAmount(PRODUCT_1.getId(), BigInteger.TWO);
        assertTrue(promotion.isPresent());
        assertEquals(insertedPromotion, promotion.get());
    }

    /**
     * Check the return of the query when no promotion is found
     */
    @Test
    @Transactional
    public void findByProductAmountEmptyTest() {
        // Creates a promotion for the product
        var insertedPromotion = Promotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.TWO)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionRepository.persist(insertedPromotion);

        // Executes the query with a lesser amount
        var promotion = promotionRepository.findByProductAmount(PRODUCT_1.getId(), BigInteger.ONE);
        assertTrue(promotion.isEmpty());

        // Executes the query with a different product id
        promotion = promotionRepository.findByProductAmount(PRODUCT_2.getId(), BigInteger.TWO);
        assertTrue(promotion.isEmpty());
    }

}
