package br.zapparolli.repository;

import br.zapparolli.entity.Promotion;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigInteger;
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

    /**
     * Find if there is a promotion for the given product and with the minumum amount
     *
     * @param productId The product identification
     * @param amount The amount of puchased products
     * @return Returns the promotion or an empty {@link Optional}
     */
    public Optional<Promotion> findByProductAmount(String productId, BigInteger amount) {
        // Builds the parameters
        var parameters = Parameters.with("productId", productId)
                .and("amount", amount);

        // Executes the query
        return find("productId = :productId and minAmount <= :amount", parameters)
                .singleResultOptional();
    }
}
