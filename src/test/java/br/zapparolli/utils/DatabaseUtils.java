package br.zapparolli.utils;

import br.zapparolli.repository.BasketRepository;
import br.zapparolli.repository.PromotionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Utility class for database operations
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class DatabaseUtils {

    @Inject
    PromotionRepository promotionRepository;

    @Inject
    BasketRepository basketRepository;

    /**
     * Clear all database data
     */
    public void clearDB() {
        basketRepository.delete("from BasketItem i");
        basketRepository.deleteAll();
        promotionRepository.deleteAll();
    }

}
