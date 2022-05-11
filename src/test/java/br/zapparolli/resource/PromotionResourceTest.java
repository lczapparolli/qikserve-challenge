package br.zapparolli.resource;

import br.zapparolli.exception.ErrorMessage;
import br.zapparolli.mock.ProductRestClientMockUtil;
import br.zapparolli.model.NewPromotion;
import br.zapparolli.resource.client.ProductsRestClient;
import br.zapparolli.service.PromotionService;
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
import java.util.List;

import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_1;
import static br.zapparolli.mock.ProductRestClientMockUtil.PRODUCT_2;
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
    PromotionService promotionService;

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

    /**
     * Check the listing of promotions
     */
    @Test
    public void listPromotionsTest() {
        // Inserts the promotions
        var promotion1 = NewPromotion.builder()
                .productId(PRODUCT_1.getId())
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionService.createPromotion(promotion1);

        var promotion2 = NewPromotion.builder()
                .productId(PRODUCT_2.getId())
                .minAmount(BigInteger.TEN)
                .unitDiscount(BigInteger.ONE)
                .build();
        promotionService.createPromotion(promotion2);

        given()
            .when()
                .config(RestAssuredConfig.newConfig().jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.BIG_INTEGER)))
                .get("/promotion")
            .then()
                .body("[0].id", is(notNullValue()))
                .body("[0].productId", is(promotion1.getProductId()))
                .body("[0].minAmount", is(promotion1.getMinAmount()))
                .body("[0].unitDiscount", is(promotion1.getUnitDiscount()))
                .body("[1].id", is(notNullValue()))
                .body("[1].productId", is(promotion2.getProductId()))
                .body("[1].minAmount", is(promotion2.getMinAmount()))
                .body("[1].unitDiscount", is(promotion2.getUnitDiscount()));
    }

}
