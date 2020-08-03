package io.vgaur.vidya.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vgaur.vidya.models.serialization.InternalField;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableTeacher.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Teacher {

    @Value.Default
    default UUID id() {
        return UUID.randomUUID();
    }

    @NotNull
    String name();

    @NotNull
    String email();

    @InternalField
    @NotNull
    String pass();

    @Value.Default
    default boolean active() {
        return true;
    }
}
