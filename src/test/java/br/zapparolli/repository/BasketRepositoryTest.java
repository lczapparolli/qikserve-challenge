package br.zapparolli.repository;

import br.zapparolli.entity.Basket;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for BasketRepository
 *
 * @author lczapparolli
 */
@QuarkusTest
public class BasketRepositoryTest {

    @Inject BasketRepository basketRepository;

    /**
     * Check the result of the method if there is an open basket for the giving customer
     */
    @Test
    @Transactional
    public void findOpenBasketExistingTest() {
        basketRepository.persist(Basket.builder()
                .customerId("OPEN_BASKET_TEST")
                .isOpen(true)
                .build());

        var openBasket = basketRepository.findOpenBasket("OPEN_BASKET_TEST");
        assertNotNull(openBasket);
        assertTrue(openBasket.isPresent());
        assertNotNull(openBasket.get().getId());
        assertEquals("OPEN_BASKET_TEST", openBasket.get().getCustomerId());
    }

    /**
     * Check the result of the method if there is no open basket for the giving customer
     */
    @Test
    @Transactional
    public void findOpBasketNonExistingTest() {
        // Check the result if there is no basket for the customer
        var openBasket = basketRepository.findOpenBasket("NO_BASKET_TEST");
        assertNotNull(openBasket);
        assertTrue(openBasket.isEmpty());

        // Check the result if there is only closed baskets for the customer
        basketRepository.persist(Basket.builder()
                .customerId("CLOSE_BASKET_TEST")
                .isOpen(false)
                .build());

        openBasket = basketRepository.findOpenBasket("CLOSE_BASKET_TEST");
        assertNotNull(openBasket);
        assertTrue(openBasket.isEmpty());
    }

}
