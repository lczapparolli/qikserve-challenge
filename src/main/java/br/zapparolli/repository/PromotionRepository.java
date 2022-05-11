package br.zapparolli.repository;

import br.zapparolli.entity.Promotion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Repository for managing Promotion data
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class PromotionRepository implements PanacheRepository<Promotion> {

    /**
     * Find if there is already a promotion for the given product
     *
     * @param productId The product identification
     * @return Returns the promotion or an empty {@link Optional}
     */
    public Optional<Promotion> findByProduct(String productId) {
        return find("productId", productId)
                .singleResultOptional();
    }
}
