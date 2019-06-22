package com.gangoogle.glide.load.engine;

import android.support.annotation.NonNull;
import com.gangoogle.glide.load.DataSource;
import com.gangoogle.glide.load.Key;
import com.gangoogle.glide.load.data.DataFetcher;
import com.gangoogle.glide.load.model.ModelLoader;
import com.gangoogle.glide.load.model.ModelLoader.LoadData;
import java.io.File;
import java.util.List;

/**
 * Generates {@link com.gangoogle.glide.load.data.DataFetcher DataFetchers} from cache files
 * containing original unmodified source data.
 */
class DataCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback<Object> {

  private final List<Key> cacheKeys;
  private final DecodeHelper<?> helper;
  private final FetcherReadyCallback cb;
  private final BaseKey baseKey;
  private int sourceIdIndex = -1;
  private Key sourceKey;
  private List<ModelLoader<File, ?>> modelLoaders;
  private int modelLoaderIndex;
  private volatile LoadData<?> loadData;
  // PMD is wrong here, this File must be an instance variable because it may be used across
  // multiple calls to startNext.
  @SuppressWarnings("PMD.SingularField")
  private File cacheFile;

  DataCacheGenerator(DecodeHelper<?> helper, FetcherReadyCallback cb,BaseKey baseKey) {
    this(helper.getCacheKeys(), helper, cb,baseKey);
  }

  // In some cases we may want to load a specific cache key (when loading from source written to
  // cache), so we accept a list of keys rather than just obtain the list from the helper.
  DataCacheGenerator(List<Key> cacheKeys, DecodeHelper<?> helper, FetcherReadyCallback cb,BaseKey baseKey) {
    this.cacheKeys = cacheKeys;
    this.helper = helper;
    this.cb = cb;
    this.baseKey =baseKey;
  }

  @Override
  public boolean startNext() {
    while (modelLoaders == null || !hasNextModelLoader()) {
      sourceIdIndex++;
      if (sourceIdIndex >= cacheKeys.size()) {
        return false;
      }

      Key sourceId = cacheKeys.get(sourceIdIndex);
      // PMD.AvoidInstantiatingObjectsInLoops The loop iterates a limited number of times
      // and the actions it performs are much more expensive than a single allocation.
      @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
      Key originalKey = new DataCacheKey(sourceId, helper.getSignature());
      cacheFile = helper.getDiskCache().get(baseKey);
      if (cacheFile != null) {
        this.sourceKey = sourceId;
        modelLoaders = helper.getModelLoaders(cacheFile);
        modelLoaderIndex = 0;
      }
    }

    loadData = null;
    boolean started = false;
    while (!started && hasNextModelLoader()) {
      ModelLoader<File, ?> modelLoader = modelLoaders.get(modelLoaderIndex++);
      loadData =
          modelLoader.buildLoadData(
              cacheFile, helper.getWidth(), helper.getHeight(), helper.getOptions());
      if (loadData != null && helper.hasLoadPath(loadData.fetcher.getDataClass())) {
        started = true;
        loadData.fetcher.loadData(helper.getPriority(), this);
      }
    }
    return started;
  }

  private boolean hasNextModelLoader() {
    return modelLoaderIndex < modelLoaders.size();
  }

  @Override
  public void cancel() {
    LoadData<?> local = loadData;
    if (local != null) {
      local.fetcher.cancel();
    }
  }

  @Override
  public void onDataReady(Object data) {
    cb.onDataFetcherReady(sourceKey, data, loadData.fetcher, DataSource.DATA_DISK_CACHE, sourceKey);
  }

  @Override
  public void onLoadFailed(@NonNull Exception e) {
    cb.onDataFetcherFailed(sourceKey, e, loadData.fetcher, DataSource.DATA_DISK_CACHE);
  }
}
