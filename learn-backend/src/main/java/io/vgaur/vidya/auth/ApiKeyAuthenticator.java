package io.vgaur.vidya.auth;

import io.dropwizard.auth.Authenticator;
import io.vgaur.vidya.models.auth.ApiKeyData;
import io.vgaur.vidya.models.auth.ApiKeyContext;
import io.vgaur.vidya.models.auth.ImmutableApiKeyContext;
import io.vgaur.vidya.services.AuthService;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgaur created on 09/08/20
 */
public class ApiKeyAuthenticator implements Authenticator<String, ApiKeyContext> {

    private final AuthService authService;

    public ApiKeyAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<ApiKeyContext> authenticate(String token) {
        if (StringUtils.isBlank(token)) {
            return Optional.empty();
        }
        UUID apiKey;
        try {
            apiKey = UUID.fromString(token);
        } catch (Exception e) {
            throw new WebApplicationException("Invalid api key");
        }

        try {
            ApiKeyData apiKeyData = authService.getApiKeyData(apiKey);
            return Optional.of(ImmutableApiKeyContext.builder().apiKey(apiKey)
                    .allowedRoles(Arrays.asList(apiKeyData.roles().split(",")))
                    .build());
        } catch (ExecutionException e) {
            throw new WebApplicationException("Error while getting api key", e);
        }
    }
}

