package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * The data needed to create a new promotion
 *
 * @author lczapparolli
 */
@Data
@Builder
public class NewPromotion {

    /**
     * The identification of the product
     */
    private String productId;

    /**
     * The minimum of items to apply the promotion
     */
    private BigInteger minAmount;

    /**
     * The discount per unit of the product
     */
    private BigInteger unitDiscount;

}
