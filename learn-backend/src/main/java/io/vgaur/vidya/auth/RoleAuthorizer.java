package io.vgaur.vidya.auth;

import io.dropwizard.auth.Authorizer;
import io.vgaur.vidya.models.auth.AuthRoleContext;
import org.apache.commons.lang3.StringUtils;

import static io.vgaur.vidya.models.auth.AuthRoleContext.ADMIN_ROLE;

/**
 * Created by vgaur created on 09/08/20
 */
public class RoleAuthorizer<T extends AuthRoleContext> implements Authorizer<T> {

    @Override
    public boolean authorize(T authRoleContext, String role) {
        return authRoleContext.allowedRoles().contains(ADMIN_ROLE)
                || StringUtils.isBlank(role)
                || authRoleContext.allowedRoles().contains(role);
    }
}
