package io.vgaur.vidya.resources;

import com.google.common.cache.Cache;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.auth.Auth;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by vgaur created on 10/08/20
 */
@Produces("application/json")
@Consumes("application/json")
@Path("/cache")
public class CacheResource {

    private final ImmutableMap<CacheType, Runnable> cacheInvalidators;

    public CacheResource(ImmutableMap<CacheType, Runnable> cacheInvalidators) {
        this.cacheInvalidators = cacheInvalidators;
    }

    @RolesAllowed("ADMIN")
    @Path("/invalidate")
    @DELETE
    public Response invalidateCaches(@Auth ApiKeyContext apiKeyContext,
                                     @QueryParam("cacheTypes") String cacheTypes) {
        Set<CacheType> cacheTypesToInvalidate = Arrays.stream(cacheTypes.split(","))
                .map(c -> CacheType.valueOf(c))
                .collect(Collectors.toSet());
        if (cacheTypesToInvalidate.contains(CacheType.ALL)) {
            cacheInvalidators.values().forEach(invalidator -> invalidator.run());
        } else {
            cacheTypesToInvalidate.forEach(cType -> cacheInvalidators.get(cType).run());
        }

        return Response.status(HttpStatus.NO_CONTENT_204).build();
    }

    public enum CacheType {
        ALL,
        TOKENS,
        APIKEYS,
        STUDENTS,
        TEACHERS
    }
}
