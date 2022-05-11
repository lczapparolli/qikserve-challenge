package br.zapparolli.service;

import br.zapparolli.entity.Promotion;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.exception.QikServeException;
import br.zapparolli.model.NewPromotion;
import br.zapparolli.repository.PromotionRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

/**
 * Service for managing promotions
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class PromotionService {

    @Inject
    PromotionRepository promotionRepository;

    @Inject
    ProductService productService;

    /**
     * Create a new promotion
     *
     * @param newPromotion The data of the new promotion
     * @throws QikServeException Throws an exception if there is any error
     * @return Returns the inserted promotion
     */
    @Transactional
    public Promotion createPromotion(NewPromotion newPromotion) {
        validatePromotion(newPromotion);
        var promotion = Promotion.newPromotion(newPromotion);

        promotionRepository.persist(promotion);

        return promotion;
    }

    /**
     * Find the promotion that is applyable to the given product and amount
     *
     * @param productId The product identification
     * @param amount The amount of the purchase
     * @return Returns the promotion or an empty {@link Optional}
     */
    public Optional<Promotion> getPromotion(String productId, BigInteger amount) {
        return promotionRepository.findByProductAmount(productId, amount);
    }

    /**
     * Validate the promotion data
     *
     * @param newPromotion The promotion to be validated
     * @throws QikServeException Throws an exception if there is any error with the data
     */
    private void validatePromotion(NewPromotion newPromotion) {
        // Checks the minimum amount
        if (Objects.isNull(newPromotion.getMinAmount()) || newPromotion.getMinAmount().compareTo(BigInteger.ONE) < 0) {
            throw new QikServeException(ErrorMessage.ERROR_PROMOTION_INVALID_AMOUNT);
        }

        // Checks the discount value
        if (Objects.isNull(newPromotion.getUnitDiscount()) || newPromotion.getUnitDiscount().compareTo(BigInteger.ONE) < 0) {
            throw new QikServeException(ErrorMessage.ERROR_PROMOTION_INVALID_DISCOUNT);
        }

        // Checks if the discount is less than the product value
        var product = productService.findProduct(newPromotion.getProductId());
        if (newPromotion.getUnitDiscount().compareTo(product.getPrice()) > 0) {
            throw new QikServeException(ErrorMessage.ERROR_PROMOTION_GREATER_DISCOUNT);
        }

        // Checks if there is a promotion for the same product
        var existentPromotion = promotionRepository.findByProduct(newPromotion.getProductId());
        if (existentPromotion.isPresent()) {
            throw new QikServeException(ErrorMessage.ERROR_PROMOTION_ALREADY_EXISTS);
        }
    }
}
