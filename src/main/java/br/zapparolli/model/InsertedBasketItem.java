package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * The data of a single item of a basket
 *
 * @author lczapparolli
 */
@Data
@Builder
public class InsertedBasketItem {

    /**
     * The identification of the product
     */
    private String productId;

    /**
     * The name of the product
     */
    private String productName;

    /**
     * The price of a unit of the product
     */
    private BigInteger unitPrice;

    /**
     * The amount purchased of the product
     */
    private BigInteger amount;

    /**
     * The total price of the item
     */
    private BigInteger itemTotal;

}
