package io.vgaur.vidya.models.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.UUID;

/**
 * Model used for mapping data while getting api key data from the db
 * Created by vgaur created on 09/08/20
 */
@Value.Immutable
@JsonSerialize
@JsonDeserialize(as = ImmutableApiKeyData.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ApiKeyData {

    UUID apiKey();

    String roles();
}
