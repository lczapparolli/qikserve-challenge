package br.zapparolli.service;

import br.zapparolli.entity.Basket;
import br.zapparolli.entity.BasketItem;
import br.zapparolli.entity.Promotion;
import br.zapparolli.model.InsertedBasket;
import br.zapparolli.model.InsertedBasketItem;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Converter for the basket entity
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class BasketConverter {

    @Inject
    ProductService productService;

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

        // Calculates the raw value of the basket
        insertedBasket.setRawValue(insertedBasket.getItems().stream()
                .map(InsertedBasketItem::getRawValue)
                .reduce(BigInteger.ZERO, BigInteger::add));

        // Calculates the discount value of the basket
        insertedBasket.setDiscount(insertedBasket.getItems().stream()
                .map(InsertedBasketItem::getDiscount)
                .reduce(BigInteger.ZERO, BigInteger::add));

        // Calculates the total value of the basket
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
        var product = productService.findProduct(basketItem.getProductId());
        var rawValue = basketItem.getAmount().multiply(basketItem.getUnitPrice());
        var discount = Optional.ofNullable(basketItem.getPromotion())
                .map(Promotion::getUnitDiscount)
                .orElse(BigInteger.ZERO)
                .multiply(basketItem.getAmount());

        // Converts the basket item
        return InsertedBasketItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .amount(basketItem.getAmount())
                .unitPrice(basketItem.getUnitPrice())
                .rawValue(rawValue)
                .discount(discount)
                .itemTotal(rawValue.subtract(discount))
                .build();
    }
}
