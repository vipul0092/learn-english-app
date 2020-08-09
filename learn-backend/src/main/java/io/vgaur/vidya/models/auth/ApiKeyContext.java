package io.vgaur.vidya.models.auth;

import org.immutables.value.Value;

import java.util.Set;
import java.util.UUID;

/**
 * Dropwizard Auth Principal for requests using api key auth
 * Created by vgaur created on 09/08/20
 */
@Value.Immutable
public interface ApiKeyContext extends AuthRoleContext {

    UUID apiKey();

    @Override
    Set<String> allowedRoles();

    @Override
    @Value.Default
    default String getName() {
        return "ApiKeyContext";
    }
}
