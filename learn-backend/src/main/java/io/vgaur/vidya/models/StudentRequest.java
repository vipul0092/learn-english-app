package io.vgaur.vidya.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.Period;

/**
 * Request for creating a student
 * Created by vgaur created on 03/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableStudentRequest.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface StudentRequest {

    Period period();

    Student student();
}
