package br.zapparolli.service;

import br.zapparolli.entity.Basket;
import br.zapparolli.entity.BasketItem;
import br.zapparolli.exception.BasketException;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.model.Product;
import br.zapparolli.repository.BasketRepository;
import br.zapparolli.resource.client.ProductsRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
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
    @RestClient
    ProductsRestClient productsRestClient;

    /**
     * Add a new item to a basket, creating it if not exists
     *
     * @param newBasketItem New item data
     * @throws BasketException Throw an exception if an error occurs
     * @return Return the basket with the new item
     */
    @Transactional
    public Basket addItem(NewBasketItem newBasketItem) {
        validateNewItem(newBasketItem);
        var product = findProduct(newBasketItem.getProductId());

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
     * Validate the item data
     *
     * @param newBasketItem The item to be validated
     * @throws BasketException Throws an exception if any field is invalid
     */
    private void validateNewItem(NewBasketItem newBasketItem) {
        // Validates the customer identification
        if (Objects.isNull(newBasketItem.getCustomerId()) || newBasketItem.getCustomerId().isBlank()) {
            throw new BasketException(ErrorMessage.ERROR_INVALID_CUSTOMER_ID);
        }

        // Validates the amount
        if (Objects.isNull(newBasketItem.getAmount()) || newBasketItem.getAmount().compareTo(BigInteger.ONE) < 0) {
            throw new BasketException(ErrorMessage.ERROR_INVALID_AMOUNT);
        }
    }

    /**
     * Get the price of the product from the API
     *
     * @param productId The product identification
     * @throws BasketException Throws an exception if there is any error with the API
     * @return Returns the product data
     */
    private Product findProduct(String productId) {
        try {
            // Searches the product in the API
            return productsRestClient.getProduct(productId);
        } catch (WebApplicationException exception) {
            // Checks if the product is not found
            if (exception.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new BasketException(ErrorMessage.ERROR_PRODUCT_NOT_FOUND);
            }

            // Any other exceptions is treat as generic
            exception.printStackTrace();
            throw new BasketException(ErrorMessage.ERROR_PRODUCT_API);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BasketException(ErrorMessage.ERROR_PRODUCT_API);
        }
    }

}
