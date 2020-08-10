package io.vgaur.vidya.models.auth;

import org.immutables.value.Value;

import java.security.Principal;
import java.util.Set;

/**
 * Dropwizard Auth Principal modelling a context that supports allowed roles
 * Created by vgaur created on 09/08/20
 */
public interface AuthRoleContext extends Principal {

    String ADMIN_ROLE = "ADMIN";

    Set<String> allowedRoles();

    @Value.Derived
    default boolean isAdmin() {
        return allowedRoles().contains(ADMIN_ROLE);
    }
}
