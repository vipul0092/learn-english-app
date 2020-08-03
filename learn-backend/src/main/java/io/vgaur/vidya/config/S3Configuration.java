package io.vgaur.vidya.config;

import com.amazonaws.regions.Regions;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Created by vgaur created on 01/08/20
 */
public class S3Configuration {

    @NotNull
    @JsonProperty
    private String accessKeyId;

    @NotNull
    @JsonProperty
    private String secretAccessKey;

    @NotNull
    @JsonProperty
    private String dataBucket;

    @NotNull
    @JsonProperty
    private Regions awsRegion;
}
