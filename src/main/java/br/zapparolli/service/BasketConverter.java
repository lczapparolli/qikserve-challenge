package br.zapparolli.service;

import br.zapparolli.entity.Basket;
import br.zapparolli.entity.BasketItem;
import br.zapparolli.model.InsertedBasket;
import br.zapparolli.model.InsertedBasketItem;
import br.zapparolli.resource.client.ProductsRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.stream.Collectors;

/**
 * Converter for the basket entity
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class BasketConverter {

    @Inject
    @RestClient
    ProductsRestClient productsRestClient;

    /**
     * Converts the entity to the response model
     *
     * @param basket The basket object
     * @return Returns the model with the same data
     */
    public InsertedBasket convertBasket(Basket basket) {
        // Creates the model for the basket
        var insertedBasket = InsertedBasket.builder()
                .customerId(basket.getCustomerId())
                .items(basket.getItems().stream().map(this::convertBasketItem).collect(Collectors.toList()))
                .build();

        // Calculates the total of the items
        insertedBasket.setTotal(insertedBasket.getItems().stream()
                .map(InsertedBasketItem::getItemTotal)
                .reduce(BigInteger.ZERO, BigInteger::add));

        return insertedBasket;
    }

    /**
     * Converts the item of a basket
     *
     * @param basketItem The basket item object
     * @return Returns the model with the same data
     */
    private InsertedBasketItem convertBasketItem(BasketItem basketItem) {
        // Get the product data
        var product = productsRestClient.getProduct(basketItem.getProductId());

        // Converts the basket item
        return InsertedBasketItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .amount(basketItem.getAmount())
                .unitPrice(basketItem.getUnitPrice())
                .itemTotal(basketItem.getAmount().multiply(basketItem.getUnitPrice()))
                .build();
    }
}
