package io.vgaur.vidya.models.auth;

import io.vgaur.vidya.models.UserType;
import org.immutables.value.Value;

import java.util.Set;

/**
 * Dropwizard Auth Principal for requests using bearer auth
 * Created by vgaur created on 09/08/20
 */
@Value.Immutable
public interface UserContext extends AuthRoleContext {

    @Override
    @Value.Default
    default String getName() {
        return "UserContext";
    }

    @Value.Derived
    default UserType userType() {
        return UserType.STUDENT;
    }

    @Override
    @Value.Default
    default Set<String> allowedRoles() {
        return Set.of();
    }

    StudentToken token();
}
