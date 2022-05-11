package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * The data needed to create a new basket item
 *
 * @author lczapparolli
 */
@Data
@Builder
public class NewBasketItem {

    /**
     * The customer identification
     */
    private String customerId;

    /**
     * The product identification
     */
    private String productId;

    /**
     * The units purchased of the product
     */
    private BigInteger amount;

}
