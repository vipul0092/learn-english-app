package io.vgaur.vidya.services.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Wrapper class dealing with Guava cache
 * Created by vgaur created on 22/10/20
 */
public class GuavaCache<K, V> implements CacheProvider<K, V> {

    private final Cache<K, V> cache;
    private final boolean isLoadingCache;

    public GuavaCache(long ttlInSeconds) {
        this(ttlInSeconds, null);
    }

    public GuavaCache(CacheLoader<K, V> cacheLoader) {
        this(0, cacheLoader);
    }

    private GuavaCache(long ttlInSeconds, CacheLoader<K, V> cacheLoader) {
        var builder = CacheBuilder.newBuilder();
        if (ttlInSeconds > 0) {
            builder.expireAfterWrite(Duration.ofSeconds(ttlInSeconds));
        }
        isLoadingCache = cacheLoader != null;
        cache = isLoadingCache ? builder.build(cacheLoader) : builder.build();
    }

    @Override
    public Optional<V> get(K key) throws ExecutionException {
        if (isLoadingCache) {
            return Optional.of(((LoadingCache<K, V>) cache).get(key));
        }
        V value = cache.getIfPresent(key);
        return value == null ? Optional.empty() : Optional.of(value);
    }

    @Override
    public boolean set(K key, V value) {
        cache.put(key, value);
        return true;
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }

    @Override
    public void removeAll() {
        cache.invalidateAll();
    }
}
