package br.zapparolli.resource;

import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewBasketItem;
import br.zapparolli.resource.client.ProductsRestClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for the basket resource
 *
 * @author lczapparolli
 */
@QuarkusTest
public class BasketResourceTest {

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    public void setup() {
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the item addition
     */
    @Test
    public void addItemTest() {
        var newBasketItem = NewBasketItem.builder()
                .customerId("RESOURCE_ADD_TEST")
                .productId(PRODUCT_1.getId())
                .amount(BigInteger.ONE)
                .build();

        given()
            .when()
                .config(RestAssuredConfig.newConfig().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_INTEGER)))
                .body(newBasketItem)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/basket")
            .then()
                .statusCode(200)
                .body("customerId", is(newBasketItem.getCustomerId()))
                .body("total", is(PRODUCT_1.getPrice()))
                .body("items[0].productId", is(PRODUCT_1.getId()))
                .body("items[0].productName", is(PRODUCT_1.getName()))
                .body("items[0].unitPrice", is(PRODUCT_1.getPrice()))
                .body("items[0].amount", is(newBasketItem.getAmount()))
                .body("items[0].itemTotal", is(PRODUCT_1.getPrice()));
    }


    /**
     * Check the return in case of an error
     */
    @Test
    public void addItemErrorTest() {
        var newBasketItem = NewBasketItem.builder()
                .customerId("RESOURCE_INVALID_TEST")
                .productId("INVALID_ID")
                .amount(BigInteger.ONE)
                .build();

        given()
            .when()
                .body(newBasketItem)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/basket")
            .then()
                .statusCode(404)
                .body("message", is(ErrorMessage.ERROR_PRODUCT_NOT_FOUND.getMessage()));
    }

}
