package br.zapparolli.service;

import br.zapparolli.entity.Basket;
import br.zapparolli.entity.BasketItem;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.exception.QikServeException;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.repository.BasketRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Service for basket management
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class BasketService {

    @Inject
    BasketRepository basketRepository;

    @Inject
    ProductService productService;

    /**
     * Add a new item to a basket, creating it if not exists
     *
     * @param newBasketItem New item data
     * @throws QikServeException Throw an exception if an error occurs
     * @return Return the basket with the new item
     */
    @Transactional
    public Basket addItem(NewBasketItem newBasketItem) {
        validateNewItem(newBasketItem);
        var product = productService.findProduct(newBasketItem.getProductId());

        // Checks if the customer have an open basket or creates a new one
        var basket = basketRepository.findOpenBasket(newBasketItem.getCustomerId())
                .orElseGet(() -> Basket.newBasket(newBasketItem.getCustomerId()));

        // Checks if the basket already have the product, otherwise creates a new item
        var existentItem = basket.getItems()
                .stream()
                .filter(item -> item.getProductId().equals(newBasketItem.getProductId()))
                .findFirst()
                .orElseGet(() -> BasketItem.newBasketItem(basket, product));

        // Increments the amount of the item
        existentItem.setAmount(existentItem.getAmount().add(newBasketItem.getAmount()));

        // Saves the basket and the itens
        basketRepository.persist(basket);

        return basket;
    }

    /**
     * Close the currently open basket of the giving customer
     *
     * @param customerId The identification of the customer
     * @throws QikServeException Throws an exception if the customer does not have an open basket
     * @return Returns de closed basket
     */
    public Basket checkout(String customerId) {
        // Searches for the current open basket of the customer
        var basket = basketRepository.findOpenBasket(customerId)
                // If no basket is found, throws an exception
                .orElseThrow(() -> new QikServeException(ErrorMessage.ERROR_NO_OPEN_BASKET));

        // Closes de basket
        basket.setOpen(false);
        basketRepository.persist(basket);

        return basket;
    }

    /**
     * Validate the item data
     *
     * @param newBasketItem The item to be validated
     * @throws QikServeException Throws an exception if any field is invalid
     */
    private void validateNewItem(NewBasketItem newBasketItem) {
        // Validates the customer identification
        if (Objects.isNull(newBasketItem.getCustomerId()) || newBasketItem.getCustomerId().isBlank()) {
            throw new QikServeException(ErrorMessage.ERROR_INVALID_CUSTOMER_ID);
        }

        // Validates the amount
        if (Objects.isNull(newBasketItem.getAmount()) || newBasketItem.getAmount().compareTo(BigInteger.ONE) < 0) {
            throw new QikServeException(ErrorMessage.ERROR_INVALID_AMOUNT);
        }
    }

}
