package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

/**
 * Stores Product data
 *
 * @author lczapparolli
 */
@Data
@Builder
public class Product {

    /**
     * Product ID
     */
    private String id;

    /**
     * Product name
     */
    private String name;

    /**
     * Product price (in cents)
     */
    private BigInteger price;

}
