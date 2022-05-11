package br.zapparolli.exception;

import lombok.Getter;

/**
 * Exceptions related to the basket managing
 *
 * @author lczapparolli
 */
@Getter
public class BasketException extends RuntimeException {

    /**
     * Error message that caused the exception
     */
    private final ErrorMessage errorMessage;

    /**
     * Creates a new exception
     *
     * @param errorMessage Error message that caused the exception
     */
    public BasketException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
