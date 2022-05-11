package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * The data of an inserted promotion
 *
 * @author lczapparolli
 */
@Data
@Builder
public class InsertedPromotion {

    /**
     * The generated id
     */
    private Long id;

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
