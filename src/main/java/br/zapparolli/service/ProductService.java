package br.zapparolli.service;

import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.exception.QikServeException;
import br.zapparolli.model.Product;
import br.zapparolli.resource.client.ProductsRestClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

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
     * @throws QikServeException Throws an exception if there is any error with the API
     * @return Returns the product data
     */
    public Product findProduct(String productId) {
        try {
            // Searches the product in the API
            return productsRestClient.getProduct(productId);
        } catch (WebApplicationException exception) {
            // Checks if the product is not found
            if (exception.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
                throw new QikServeException(ErrorMessage.ERROR_PRODUCT_NOT_FOUND);
            }

            // Any other exceptions is treat as generic
            throw new QikServeException(ErrorMessage.ERROR_PRODUCT_API);
        } catch (Exception exception) {
            throw new QikServeException(ErrorMessage.ERROR_PRODUCT_API);
        }
    }

    /**
     * Get all products in the API
     *
     * @return Returns the list of products
     */
    public List<Product> getProducts() {
        return productsRestClient.listProducts();
    }

}
