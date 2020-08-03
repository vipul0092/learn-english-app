package io.vgaur.vidya.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class VidyaServiceConfiguration extends Configuration {

    @NotNull
    @JsonProperty("s3")
    private S3Configuration s3Config;

    public S3Configuration getS3Config() {
        return s3Config;
    }

    public DataSourceFactory getDatabase() {
        return database;
    }

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database;
}
