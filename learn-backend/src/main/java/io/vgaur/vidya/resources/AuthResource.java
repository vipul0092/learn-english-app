package io.vgaur.vidya.resources;

import io.vgaur.vidya.models.StudentToken;
import io.vgaur.vidya.models.TokenRequest;
import io.vgaur.vidya.services.AuthService;
import org.eclipse.jetty.http.HttpStatus;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgaur created on 27/07/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/auth")
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @Path("/token")
    public Response getAuthToken(
            @Valid TokenRequest tokenRequest
    ) throws ExecutionException {
        var token = authService.generateToken(tokenRequest);
        return Response.status(HttpStatus.CREATED_201).entity(token).build();
    }

    @GET
    @Path("/token/{id}/verify")
    public StudentToken verifyToken(
            @PathParam("id") UUID tokenId
    ) throws ExecutionException {
        return authService.verifyToken(tokenId);
    }
}
