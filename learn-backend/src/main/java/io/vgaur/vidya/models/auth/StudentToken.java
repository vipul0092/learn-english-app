package io.vgaur.vidya.models.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Token object created for a token request
 * Created by vgaur created on 03/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableStudentToken.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface StudentToken {

    String email();

    UUID id();

    UUID teacherId();

    UUID tokenId();

    UUID createdWithApiKey();
}
