package io.vgaur.vidya.resources;

import io.dropwizard.auth.Auth;
import io.vgaur.vidya.models.auth.StudentToken;
import io.vgaur.vidya.models.auth.TokenRequest;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.services.AuthService;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Auth related endpoints
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

    @RolesAllowed("TOKEN_CONSUMER")
    @POST
    @Path("/token")
    public Response getAuthToken(
            @Valid TokenRequest tokenRequest,
            @Auth ApiKeyContext apiKeyContext
    ) throws ExecutionException {
        var token = authService.generateToken(tokenRequest, apiKeyContext);
        return Response.status(HttpStatus.CREATED_201).entity(token).build();
    }

    @RolesAllowed("TOKEN_CONSUMER")
    @GET
    @Path("/token/{id}/verify")
    public StudentToken verifyToken(
            @PathParam("id") UUID tokenId,
            @Auth ApiKeyContext apiKeyContext
    ) throws ExecutionException {
        return authService.verifyToken(tokenId);
    }

    @RolesAllowed("ADMIN")
    @DELETE
    @Path("/token/{id}")
    public Response deleteToken(
            @PathParam("id") UUID tokenId,
            @Auth ApiKeyContext apiKeyContext
    ) throws ExecutionException {
        authService.deleteToken(apiKeyContext, tokenId);
        return Response.status(HttpStatus.NO_CONTENT_204).build();
    }
}
