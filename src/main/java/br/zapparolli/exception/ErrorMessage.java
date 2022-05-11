package br.zapparolli.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * Errors that could be thrown during the execution
 *
 * @author lczapparolli
 */
@Getter
@AllArgsConstructor
public enum ErrorMessage {

    ERROR_PRODUCT_NOT_FOUND("The product could not be found.", Response.Status.NOT_FOUND),
    ERROR_PRODUCT_API("An error occurred while getting product data, please try again later", Response.Status.INTERNAL_SERVER_ERROR),
    ERROR_INVALID_AMOUNT("The pushased amount must be a positive number", Response.Status.BAD_REQUEST),
    ERROR_INVALID_CUSTOMER_ID("The customer identification should be provided", Response.Status.BAD_REQUEST),
    ERROR_NO_OPEN_BASKET("This customer does not have an open basket.", Response.Status.NOT_FOUND),
    ERROR_PROMOTION_INVALID_AMOUNT("The minimum amount must be a positive number", Response.Status.BAD_REQUEST),
    ERROR_PROMOTION_INVALID_DISCOUNT("The discount must be a positive number", Response.Status.BAD_REQUEST),
    ERROR_PROMOTION_GREATER_DISCOUNT("The discount value must be less than the product value", Response.Status.BAD_REQUEST),
    ERROR_PROMOTION_ALREADY_EXISTS("There is already a promotion for this product", Response.Status.BAD_REQUEST);

    /**
     * The message that should be returned to de caller
     */
    private final String message;
    /**
     * The HTTP status of the response
     */
    private final Response.Status status;

}
