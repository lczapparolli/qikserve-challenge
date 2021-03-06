package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * The data of an inserted basket
 *
 * @author lczapparolli
 */
@Data
@Builder
public class InsertedBasket {

    /**
     * The customer identification
     */
    private String customerId;

    /**
     * The list of itens in the basket
     */
    private List<InsertedBasketItem> items;

    /**
     * The sum of raw values of the items
     */
    private BigInteger rawValue;

    /**
     * The total discount given to the basket
     */
    private BigInteger discount;

    /**
     * The total price of the basket in cents
     */
    private BigInteger total;

}
