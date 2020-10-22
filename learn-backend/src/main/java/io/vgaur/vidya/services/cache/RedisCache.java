package io.vgaur.vidya.services.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.redis.RedisClientBundle;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.vgaur.vidya.config.VidyaServiceConfiguration;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Wrapper class dealing with Redis cache
 * Created by vgaur created on 22/10/20
 */
public class RedisCache<K, V> implements CacheProvider<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(RedisCache.class);

    private final StatefulRedisConnection<String, String> connection;
    private final long ttlInSeconds;
    private final Class<V> vClass;
    private final ObjectMapper objectMapper;

    public RedisCache(RedisClientBundle<String, String, VidyaServiceConfiguration> redisBundle, long ttlInSeconds,
                      ObjectMapper objectMapper, Class<V> vClass) {
        this.connection = redisBundle.getConnection();
        this.ttlInSeconds = ttlInSeconds;
        this.vClass = vClass;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<V> get(K key) {
        String value = connection.sync().get(key.toString().toLowerCase());
        try {
            return StringUtils.isBlank(value)
                    ? Optional.empty()
                    : Optional.of(objectMapper.readValue(value, vClass));
        } catch (JsonProcessingException jpe) {
            LOG.error("Exception thrown while deserializing cached value, key: {}", key, jpe);
            return Optional.empty();
        }
    }

    @Override
    public boolean set(K key, V value) {
        try {
            connection.sync().set(key.toString().toLowerCase(), objectMapper.writeValueAsString(value),
                    SetArgs.Builder.ex(ttlInSeconds));
            return true;
        } catch (JsonProcessingException jpe) {
            LOG.error("Exception thrown while serializing value, key: {}", key, jpe);
            return false;
        }
    }

    @Override
    public void remove(K key) {
        connection.sync().del(key.toString().toLowerCase());
    }

    @Override
    public void removeAll() {
        throw new NotImplementedException();
    }
}
