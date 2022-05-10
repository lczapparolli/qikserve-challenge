package br.zapparolli.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigInteger;

/**
 * Single item of a basket
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BASKET_ITEM")
public class BasketItem {

    /**
     * Auto-generated primary key
     */
    @Id
    @GeneratedValue
    @Column(name = "BASKET_ITEM_ID", nullable = false)
    private Long id;

    /**
     * Identification of the product
     */
    @Column(name = "PRODUCT_ID", nullable = false)
    private String productId;

    /**
     * Actual price of a single unit of the product at the moment of basket creation
     */
    @Column(name = "UNIT_PRICE", nullable = false)
    private BigInteger unitPrice;

    /**
     * Amount of units of the product
     */
    @Column(name = "AMOUNT", nullable = false)
    private BigInteger amount;

    /**
     * Basket associated with the item
     */
    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "BASKET_ID", referencedColumnName = "BASKET_ID", nullable = false)
    private Basket basket;

}
