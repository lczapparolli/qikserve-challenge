package br.zapparolli.resource;

import br.zapparolli.entity.Promotion;
import br.zapparolli.model.InsertedPromotion;
import br.zapparolli.model.NewPromotion;
import br.zapparolli.service.PromotionService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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
        return convertPromotion(promotion);
    }

    /**
     * Lists all promotions
     *
     * @return Returns the list of existent promotions
     */
    @GET
    public List<InsertedPromotion> listPromotions() {
        // Lists all promotions
        var promotions = promotionService.getAllPromotions();

        // Converts the entities to the return model
        return promotions.stream()
                .map(this::convertPromotion)
                .collect(Collectors.toList());
    }

    /**
     * Creates an example promotion for all products
     */
    @POST
    @Path("/example")
    public void createExamplePromotions() {
        promotionService.createExamplePromotions();
    }

    /**
     * Converts the entity to the model
     *
     * @param promotion Entity to be converted
     * @return Return the model with the same data
     */
    private InsertedPromotion convertPromotion(Promotion promotion) {
        return InsertedPromotion.builder()
                .id(promotion.getId())
                .productId(promotion.getProductId())
                .minAmount(promotion.getMinAmount())
                .unitDiscount(promotion.getUnitDiscount())
                .build();
    }

}
