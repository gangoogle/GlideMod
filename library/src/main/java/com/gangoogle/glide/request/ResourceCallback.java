package com.gangoogle.glide.request;

import com.gangoogle.glide.load.DataSource;
import com.gangoogle.glide.load.engine.GlideException;
import com.gangoogle.glide.load.engine.Resource;

/**
 * A callback that listens for when a resource load completes successfully or fails due to an
 * exception.
 */
public interface ResourceCallback {

  /**
   * Called when a resource is successfully loaded.
   *
   * @param resource The loaded resource.
   */
  void onResourceReady(Resource<?> resource, DataSource dataSource);

  /**
   * Called when a resource fails to load successfully.
   *
   * @param e a non-null {@link GlideException}.
   */
  void onLoadFailed(GlideException e);
}
