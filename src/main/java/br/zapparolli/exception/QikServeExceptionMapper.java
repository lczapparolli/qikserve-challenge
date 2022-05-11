package br.zapparolli.exception;

import br.zapparolli.model.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps a QikServeException to an error response
 *
 * @author lczapparolli
 */
@Provider
public class QikServeExceptionMapper implements ExceptionMapper<QikServeException> {

    /**
     * Converts the exception to a response with error status
     * @param exception The exception caught
     * @return Returns the response object
     */
    @Override
    public Response toResponse(QikServeException exception) {
        return Response.status(exception.getErrorMessage().getStatus())
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ErrorResponse.newErrorResponse(exception.getErrorMessage().getMessage()))
                .build();
    }

}
