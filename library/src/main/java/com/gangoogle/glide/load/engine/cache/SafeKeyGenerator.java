package com.gangoogle.glide.load.engine.cache;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools;
import android.text.TextUtils;

import com.gangoogle.glide.util.LruCache;
import com.gangoogle.glide.util.Preconditions;
import com.gangoogle.glide.util.Synthetic;
import com.gangoogle.glide.util.Util;
import com.gangoogle.glide.util.pool.FactoryPools;
import com.gangoogle.glide.util.pool.StateVerifier;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class that generates and caches safe and unique string file names from {@link
 * com.gangoogle.glide.load.Key}s.
 */
// Public API.
@SuppressWarnings("WeakerAccess")
public class SafeKeyGenerator {
    private final LruCache<String, String> loadIdToSafeHash = new LruCache<>(1000);
    private final Pools.Pool<PoolableDigestContainer> digestPool =
            FactoryPools.threadSafe(
                    10,
                    new FactoryPools.Factory<PoolableDigestContainer>() {
                        @Override
                        public PoolableDigestContainer create() {
                            try {
                                return new PoolableDigestContainer(MessageDigest.getInstance("SHA-256"));
                            } catch (NoSuchAlgorithmException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


    public String getSafeKey(String key) {
        String safeKey;
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key);
        }
        if (safeKey == null) {
            if (TextUtils.isEmpty(key)) {
                safeKey = Encode.md5Sync(System.currentTimeMillis() + "");
            } else {
                //改为md5
                safeKey = Encode.md5Sync(key);
            }
        }
        synchronized (loadIdToSafeHash) {
            loadIdToSafeHash.put(key, safeKey);
        }
        return safeKey;
    }


    private String calculateHexStringDigest(String key) {
        PoolableDigestContainer container = Preconditions.checkNotNull(digestPool.acquire());
        try {
//      key.updateDiskCacheKey(container.messageDigest);
            // calling digest() will automatically reset()
            return Util.sha256BytesToHex(container.messageDigest.digest());
        } finally {
            digestPool.release(container);
        }
    }

    private static final class PoolableDigestContainer implements FactoryPools.Poolable {

        @Synthetic
        final MessageDigest messageDigest;
        private final StateVerifier stateVerifier = StateVerifier.newInstance();

        PoolableDigestContainer(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
        }

        @NonNull
        @Override
        public StateVerifier getVerifier() {
            return stateVerifier;
        }
    }
}
