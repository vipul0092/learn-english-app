package io.vgaur.vidya.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vgaur created on 27/07/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/auth")
public class AuthResource {

    @GET
    public Response getAuthResponse() {
        Map<String, String> value = new HashMap<>();
        value.put("name", "VGaur");
        value.put("location", "AWS EC2");
        return Response.ok(value).build();
    }
}
