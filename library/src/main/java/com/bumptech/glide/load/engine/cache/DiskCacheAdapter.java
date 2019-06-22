package com.bumptech.glide.load.engine.cache;

import android.support.annotation.Nullable;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.BaseKey;
import java.io.File;

/** A simple class that returns null for all gets and ignores all writes. */
public class DiskCacheAdapter implements DiskCache {

  @Nullable
  @Override
  public File get(BaseKey key) {
    return null;
  }

  @Override
  public void put(BaseKey key, Writer writer) {

  }

  @Override
  public void delete(BaseKey key) {

  }

  @Override
  public void clear() {
    // no op, default for overriders
  }

  /** Default factory for {@link DiskCacheAdapter}. */
  public static final class Factory implements DiskCache.Factory {
    @Override
    public DiskCache build() {
      return new DiskCacheAdapter();
    }
  }
}
