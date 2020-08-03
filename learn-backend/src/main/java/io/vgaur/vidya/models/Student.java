package io.vgaur.vidya.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vgaur.vidya.models.serialization.InternalField;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableStudent.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Student {

    @Value.Default
    default UUID id() {
        return UUID.randomUUID();
    }

    @Nullable
    UUID teacherId();

    @NotNull
    String name();

    @NotNull
    String email();

    @InternalField
    @NotNull
    String pass();

    @InternalField
    @Nullable
    LocalDateTime validUntil();
}
