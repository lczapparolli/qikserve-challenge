package br.zapparolli.repository;

import br.zapparolli.entity.Basket;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

/**
 * Repository to manage Basket data
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class BasketRepository implements PanacheRepository<Basket> {
}
