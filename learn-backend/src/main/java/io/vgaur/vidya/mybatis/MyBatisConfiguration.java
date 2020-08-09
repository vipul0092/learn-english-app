package io.vgaur.vidya.mybatis;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.constraints.NotEmpty;

/**
 * Mybatis config
 */
public class MyBatisConfiguration {

    @JsonProperty
    private String configFile;

    @JsonProperty
    private final DataSourceFactory config = new DataSourceFactory();

    @NotEmpty
    @JsonProperty
    private String environment;

    public String getConfigFile() {
        return configFile;
    }

    public DataSourceFactory getConfig() {
        return config;
    }

    public String getEnvironment() {
        return environment;
    }
}
