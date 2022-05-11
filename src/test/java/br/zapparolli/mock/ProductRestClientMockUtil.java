package br.zapparolli.mock;

import br.zapparolli.model.Product;
import br.zapparolli.resource.client.ProductsRestClient;
import org.mockito.Mockito;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.List;

/**
 * Utility class to configure Products API mock
 *
 * @author lczapparolli
 */
public class ProductRestClientMockUtil {

    /**
     * A single product identified as PRODUCT_1
     */
    public static final Product PRODUCT_1 = Product.builder()
            .id("PRODUCT_1")
            .name("Product 1")
            .price(BigInteger.valueOf(111))
            .build();

    /**
     * A single product identified as PRODUCT_2
     */
    public static final Product PRODUCT_2 = Product.builder()
            .id("PRODUCT_2")
            .name("Product 2")
            .price(BigInteger.valueOf(222))
            .build();

    /**
     * Configure the mock to return data for 2 products (PRODUCT_1, PRODUCT_2)
     *
     * @param productsRestClient The injected mock object
     */
    public static void configMock(ProductsRestClient productsRestClient) {
        Mockito.when(productsRestClient.listProducts()).thenReturn(List.of(PRODUCT_1, PRODUCT_2));

        Mockito.when(productsRestClient.getProduct(PRODUCT_1.getId())).thenReturn(PRODUCT_1);
        Mockito.when(productsRestClient.getProduct(PRODUCT_2.getId())).thenReturn(PRODUCT_2);
        // In case of an invalid product
        Mockito.when(productsRestClient.getProduct("INVALID_ID")).thenThrow(new WebApplicationException(Response.status(Response.Status.NOT_FOUND).build()));
    }

}
