package br.zapparolli.resource;

import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewPromotion;
import br.zapparolli.resource.client.ProductsRestClient;
import br.zapparolli.utils.DatabaseUtils;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.config.JsonConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import java.math.BigInteger;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Tests for the promotion resource
 *
 * @author lczapparolli
 */
@QuarkusTest
public class PromotionResourceTest {

    @Inject
    DatabaseUtils databaseUtils;

    @InjectMock
    @RestClient
    ProductsRestClient productsRestClient;

    @BeforeEach
    @Transactional
    public void setup() {
        databaseUtils.clearDB();
        ProductRestClientMockUtil.configMock(productsRestClient);
    }

    /**
     * Check the create promotion process
     */
    @Test
    public void createPromotionTest() {
        var newPromotion = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();

        given()
            .when()
                .config(RestAssuredConfig.newConfig().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_INTEGER)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(newPromotion)
                .post("/promotion")
            .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("productId", is(newPromotion.getProductId()))
                .body("minAmount", is(newPromotion.getMinAmount()))
                .body("unitDiscount", is(newPromotion.getUnitDiscount()));
    }

    /**
     * Check the return when an error occurs
     */
    @Test
    public void createPromotionErrorTest() {
        var newPromotion = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.ZERO)
                .unitDiscount(BigInteger.ONE)
                .build();

        given()
            .when()
                .contentType(MediaType.APPLICATION_JSON)
                .body(newPromotion)
                .post("/promotion")
            .then()
                .statusCode(400)
                .body("message", is(ErrorMessage.ERROR_PROMOTION_INVALID_AMOUNT.getMessage()));
    }

}
