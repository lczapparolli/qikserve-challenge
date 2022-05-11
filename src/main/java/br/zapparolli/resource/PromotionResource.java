package br.zapparolli.resource;

import br.zapparolli.model.InsertedPromotion;
import br.zapparolli.model.NewPromotion;
import br.zapparolli.service.PromotionService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The resource for the promotions operations
 *
 * @author lczapparolli
 */
@Path("/promotion")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PromotionResource {

    @Inject
    PromotionService promotionService;

    /**
     * Creates a new promotion
     *
     * @param newPromotion The promotion data
     * @return Returns the created promotion
     */
    @POST
    public InsertedPromotion createPromotion(NewPromotion newPromotion) {
        // Creates the promotion
        var promotion = promotionService.createPromotion(newPromotion);

        // Converts the entity to the return model
        return InsertedPromotion.builder()
                .id(promotion.getId())
                .productId(promotion.getProductId())
                .minAmount(promotion.getMinAmount())
                .unitDiscount(promotion.getUnitDiscount())
                .build();
    }

}
