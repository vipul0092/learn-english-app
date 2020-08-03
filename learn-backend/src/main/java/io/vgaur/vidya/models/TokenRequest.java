package io.vgaur.vidya.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;

/**
 * Created by vgaur created on 03/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableTokenRequest.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface TokenRequest {

    @Value.Default
    default UserType userType() {
        return UserType.STUDENT;
    }

    @NotNull
    String email();

    @NotNull
    String pass();
}
