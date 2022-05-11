package br.zapparolli.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

/**
 * Entity for storing Promotion data
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROMOTION")
public class Promotion {

    /**
     * Auto-generated primary key
     */
    @Id
    @GeneratedValue
    @Column(name = "PROMOTION_ID", nullable = false)
    private Long id;

    /**
     * The identification of the product related to the promotion
     */
    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;

    /**
     * The minimum of the product to apply the promotion
     */
    @Column(name = "MIN_AMOUNT", nullable = false)
    private BigInteger minAmount;

    /**
     * The discount value applied to each unit of the product
     */
    @Column(name = "UNIT_DISCOUNT", nullable = false)
    private BigInteger unitDiscount;

}
