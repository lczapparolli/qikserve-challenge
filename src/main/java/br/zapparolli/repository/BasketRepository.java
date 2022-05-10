package br.zapparolli.repository;

import br.zapparolli.entity.Basket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Repository to manage Basket data
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class BasketRepository implements PanacheRepository<Basket> {

    /**
     * Finds an open basket for the giving user, if there is no open baskets return an empty {@link Optional}
     *
     * @param customerId Identification of the customer
     * @return Return the option with or without the basket found
     */
    public Optional<Basket> findOpenBasket(String customerId) {
        var parameters = Parameters.with("isOpen", true)
                .and("customerId", customerId);

        return find("isOpen = :isOpen and customerId = :customerId", parameters)
                .firstResultOptional();
    }
    
}
