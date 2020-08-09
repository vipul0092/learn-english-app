package io.vgaur.vidya.auth;

import io.dropwizard.auth.Authorizer;
import io.vgaur.vidya.models.auth.AuthRoleContext;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by vgaur created on 09/08/20
 */
public class RoleAuthorizer<T extends AuthRoleContext> implements Authorizer<T> {

    private static final String ADMIN_ROLE = "ADMIN";

    @Override
    public boolean authorize(T authRoleContext, String role) {
        return authRoleContext.allowedRoles().contains(ADMIN_ROLE)
                || StringUtils.isBlank(role)
                || authRoleContext.allowedRoles().contains(role);
    }
}
