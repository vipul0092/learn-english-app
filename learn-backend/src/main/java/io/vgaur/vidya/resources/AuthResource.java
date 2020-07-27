package io.vgaur.vidya.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by vgaur created on 27/07/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/auth")
public class AuthResource {

    @GET
    public Response getAuthResponse() {
        return Response.ok().build();
    }
}
