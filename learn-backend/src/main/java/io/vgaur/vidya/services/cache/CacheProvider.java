package io.vgaur.vidya.services.cache;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Interface modelling a cache provider
 * Created by vgaur created on 22/10/20
 */
public interface CacheProvider<K, V> {

    /**
     * Get value for the key
     */
    Optional<V> get(K key) throws ExecutionException;

    /**
     * Set value for the key
     */
    boolean set(K key, V value);

    /**
     * Remove the key from the cache
     */
    void remove(K key);

    /**
     * Remove all the keys from the cache
     */
    void removeAll();
}