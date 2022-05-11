package br.zapparolli.resource;

import br.zapparolli.model.InsertedBasket;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.service.BasketConverter;
import br.zapparolli.service.BasketService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The resource for the basket operations
 *
 * @author lczapparolli
 */
@Path("/basket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BasketResource {

    @Inject
    BasketService basketService;

    @Inject
    BasketConverter basketConverter;

    /**
     * Adds an item to the basket
     *
     * @param newBasketItem The item to be added
     * @return Returns the model with the updated data
     */
    @POST
    public InsertedBasket addItem(NewBasketItem newBasketItem) {
        // Adds the item
        var basket = basketService.addItem(newBasketItem);

        // Converts the entity
        return basketConverter.convertBasket(basket);
    }

}
