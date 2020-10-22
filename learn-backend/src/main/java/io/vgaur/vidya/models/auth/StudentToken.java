package io.vgaur.vidya.models.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.vgaur.vidya.models.serialization.LocalDateTimeStringSerializer;
import org.immutables.value.Value;

import java.time.LocalDateTime;
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

    @JsonSerialize(using = LocalDateTimeStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDateTime createdTimestamp();
}
