package io.vgaur.vidya.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.redis.RedisClientBundle;
import io.vgaur.vidya.config.VidyaServiceConfiguration;
import io.vgaur.vidya.models.auth.StudentToken;
import io.vgaur.vidya.services.cache.CacheProvider;
import io.vgaur.vidya.services.cache.GuavaCache;
import io.vgaur.vidya.services.cache.RedisCache;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Class responsible for dealing with token storage and fetching
 * Created by vgaur created on 22/10/20
 */
public class AuthTokenStore {

    private final CacheProvider<UUID, StudentToken> tokenCache;

    public AuthTokenStore(RedisClientBundle<String, String, VidyaServiceConfiguration> redis,
                          long ttlInSeconds, ObjectMapper objectMapper, boolean useDistributedCache) {
        if (useDistributedCache) {
            tokenCache = new RedisCache<>(redis, ttlInSeconds, objectMapper, StudentToken.class);
        } else {
            tokenCache = new GuavaCache<>(ttlInSeconds);
        }
    }

    /**
     * Get token
     */
    public Optional<StudentToken> getToken(UUID tokenId) throws ExecutionException {
        return tokenCache.get(tokenId);
    }

    /**
     * Save token
     */
    public boolean saveToken(StudentToken token) {
        return tokenCache.set(token.tokenId(), token);
    }

    /**
     * Delete token
     */
    public void deleteToken(UUID tokenId) {
        tokenCache.remove(tokenId);
    }
}
