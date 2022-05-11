package br.zapparolli.resource;

import br.zapparolli.model.InsertedBasket;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.service.BasketConverter;
import br.zapparolli.service.BasketService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    /**
     * Closes an open basket
     *
     * @param customerId The identification of the customer
     * @return Returns the model with the updated data
     */
    @POST
    @Path("/{customerId}/checkout")
    public InsertedBasket checkout(@PathParam("customerId") String customerId) {
        // Closes the basket
        var basket = basketService.checkout(customerId);

        // Converts the entity
        return basketConverter.convertBasket(basket);
    }

    /**
     * Gets the currently open basket for the given customerId
     *
     * @param customerId The customer identification
     * @return Returns the basket found
     */
    @GET
    @Path("/{customerId}")
    public InsertedBasket getOpenBasket(@PathParam("customerId") String customerId) {
        // Closes the basket
        var basket = basketService.getOpenBasket(customerId);

        // Converts the entity
        return basketConverter.convertBasket(basket);
    }

}
