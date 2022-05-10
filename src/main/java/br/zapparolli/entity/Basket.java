package br.zapparolli.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity for storing Basket data
 *
 * @author lczapparolli
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BASKET")
public class Basket {

    /**
     * Auto-generated primary key
     */
    @Id
    @GeneratedValue
    @Column(name = "BASKET_ID", nullable = false)
    private Long id;

    /**
     * Customer identification
     */
    @Column(name = "CUSTOMER_ID", nullable = false)
    private String customerId;

    /**
     * Indicates if the basket is currently open or has been checked out
     */
    @Column(name = "IS_OPEN", nullable = false)
    private boolean isOpen;

    /**
     * List of items in the basket
     */
    @OneToMany(mappedBy = "basket", cascade = CascadeType.PERSIST)
    private List<BasketItem> items;

}
