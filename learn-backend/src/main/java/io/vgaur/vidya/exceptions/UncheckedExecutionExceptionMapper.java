package io.vgaur.vidya.exceptions;

import com.google.common.util.concurrent.UncheckedExecutionException;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.HashMap;

/**
 * Created by vgaur created on 03/08/20
 */
public class UncheckedExecutionExceptionMapper implements ExceptionMapper<UncheckedExecutionException> {
    @Override
    public Response toResponse(UncheckedExecutionException e) {
        var inner = e.getCause();
        var response = new HashMap<>();
        int status = HttpStatus.INTERNAL_SERVER_ERROR_500;
        if (inner instanceof WebApplicationException) {
            var webAppException = (WebApplicationException) inner;
            status = webAppException.getResponse().getStatus();
            response.put("message", webAppException.getMessage());
        } else {
            response.put("message", e.getMessage());
        }
        response.put("code", status);

        return Response.status(status).type(MediaType.APPLICATION_JSON_TYPE)
                .entity(response).build();
    }
}
