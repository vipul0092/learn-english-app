package io.vgaur.vidya.auth;

import io.dropwizard.auth.Authenticator;
import io.vgaur.vidya.models.auth.StudentToken;
import io.vgaur.vidya.models.auth.ImmutableUserContext;
import io.vgaur.vidya.models.auth.UserContext;
import io.vgaur.vidya.services.AuthService;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgaur created on 09/08/20
 */
public class BearerAuthenticator implements Authenticator<String, UserContext> {

    private final AuthService authService;

    public BearerAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<UserContext> authenticate(String token) {
        if(StringUtils.isBlank(token)) {
            return Optional.empty();
        }
        UUID tokenId;
        try {
            tokenId = UUID.fromString(token);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid token");
        }

        try {
            StudentToken tokenData = authService.verifyToken(tokenId);
            return Optional.of(ImmutableUserContext.builder().token(tokenData).build());
        } catch (ExecutionException e) {
            throw new WebApplicationException("Error while getting token", e);
        }
    }
}

