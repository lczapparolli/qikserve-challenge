package br.zapparolli.resource.client;

import br.zapparolli.model.Product;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Rest client for products API
 *
 * @author lczapparolli
 */
@Path("/products")
@RegisterRestClient(configKey = "products-api")
@ApplicationScoped
public interface ProductsRestClient {

    /**
     * List all products
     *
     * @return The list of products
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Collection<Product> listProducts();

    /**
     * Return the data of a single product
     *
     * @param id Product identification
     * @return The product data
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Product getProduct(@PathParam("id") String id);

}
