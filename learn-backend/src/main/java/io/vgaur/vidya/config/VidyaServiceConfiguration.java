package io.vgaur.vidya.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.redis.RedisClientFactory;

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

    public RedisClientFactory<String, String> getRedisClientFactory() {
        return redisClientFactory;
    }

    @Valid
    @NotNull
    @JsonProperty("redis")
    private RedisClientFactory<String, String> redisClientFactory;

    public long getTokenTTL() {
        return tokenTTL;
    }

    @Valid
    @NotNull
    @JsonProperty
    private long tokenTTL;
}
