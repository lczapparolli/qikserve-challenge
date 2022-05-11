package br.zapparolli.service;

import br.zapparolli.exception.BasketException;
import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.resource.client.ProductsRestClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for the product service
 *
 * @author lczapparolli
 */
@QuarkusTest
public class ProductServiceTest {

    @Inject
    ProductService productService;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    public void setup() {
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the response for a product query
     */
    @Test
    public void findProductTest() {
        var product = productService.findProduct(PRODUCT_1.getId());
        assertNotNull(product);
        verify(productsRestClient, times(1)).getProduct(PRODUCT_1.getId());
    }

    /**
     * Check the response for an invalid product
     */
    @Test
    public void findProductNotFoundTest() {
        try {
            productService.findProduct("INVALID_ID");
        } catch (BasketException exception) {
            assertEquals(ErrorMessage.ERROR_PRODUCT_NOT_FOUND, exception.getErrorMessage());
        }
        verify(productsRestClient, times(1)).getProduct("INVALID_ID");
    }

    /**
     * Check the response for a problem with the products API
     */
    @Test
    public void findProductAPIErrorTest() {
        try {
            productService.findProduct("API_ERROR");
        } catch (BasketException exception) {
            assertEquals(ErrorMessage.ERROR_PRODUCT_API, exception.getErrorMessage());
        }
        verify(productsRestClient, times(1)).getProduct("API_ERROR");
    }

}
