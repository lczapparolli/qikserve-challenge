package br.zapparolli.exception;

import br.zapparolli.model.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps a BasketException to an error response
 *
 * @author lczapparolli
 */
@Provider
public class BasketExceptionMapper implements ExceptionMapper<BasketException> {

    /**
     * Converts the exception to a response with error status
     * @param exception The exception caught
     * @return Returns the response object
     */
    @Override
    public Response toResponse(BasketException exception) {
        return Response.status(exception.getErrorMessage().getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResponse.newErrorResponse(exception.getErrorMessage().getMessage()))
                .build();
    }

}
