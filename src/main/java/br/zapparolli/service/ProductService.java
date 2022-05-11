package br.zapparolli.service;

import br.zapparolli.exception.BasketException;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.model.Product;
import br.zapparolli.resource.client.ProductsRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Service for getting product data
 *
 * @author lczapparolli
 */
@ApplicationScoped
public class ProductService {

    @Inject
    @RestClient
    ProductsRestClient productsRestClient;


    /**
     * Get the price of the product from the API
     *
     * @param productId The product identification
     * @throws BasketException Throws an exception if there is any error with the API
     * @return Returns the product data
     */
    public Product findProduct(String productId) {
        try {
            // Searches the product in the API
            return productsRestClient.getProduct(productId);
        } catch (WebApplicationException exception) {
            // Checks if the product is not found
            if (exception.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new BasketException(ErrorMessage.ERROR_PRODUCT_NOT_FOUND);
            }

            // Any other exceptions is treat as generic
            throw new BasketException(ErrorMessage.ERROR_PRODUCT_API);
        } catch (Exception exception) {
            throw new BasketException(ErrorMessage.ERROR_PRODUCT_API);
        }
    }

}
