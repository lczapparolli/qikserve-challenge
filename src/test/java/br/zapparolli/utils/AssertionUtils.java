package br.zapparolli.utils;

import br.zapparolli.exception.BasketException;
import br.zapparolli.exception.ErrorMessage;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Utility class for assertions
 *
 * @author lczapparolli
 */
public class AssertionUtils {

    /**
     * Check if the method throws a {@link BasketException} with the expected message
     *
     * @param errorMessage The expect error message
     * @param method The method to be executed
     */
    public static void assertThrows(ErrorMessage errorMessage, Executable method) {
        try {
            // Executes the method
            method.execute();
            // Fails if the method does not throw
            fail("Should have thrown a BasketException");
        } catch (BasketException exception) {
            // Checks if the error message is the same
            assertEquals(errorMessage, exception.getErrorMessage());
        } catch (Throwable e) {
            // Fails if the exception is different of BasketException
            fail(String.format("Should have thrown a BasketException, but throwed a '%s'", e.getClass().getName()));
        }
    }

}
