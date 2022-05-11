package br.zapparolli.model;

import lombok.Builder;
import lombok.Data;

/**
 * Model for error messages
 *
 * @author lczapparolli
 */
@Data
@Builder
public class ErrorResponse {

    /**
     * The message of the error
     */
    private String message;

    /**
     * Create a new object with the given message
     *
     * @param message The error message
     * @return Returns the created object
     */
    public static ErrorResponse newErrorResponse(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }

}
