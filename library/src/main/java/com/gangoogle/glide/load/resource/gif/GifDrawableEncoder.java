package com.gangoogle.glide.load.resource.gif;

import android.util.Log;
import android.support.annotation.NonNull;
import com.gangoogle.glide.load.EncodeStrategy;
import com.gangoogle.glide.load.Options;
import com.gangoogle.glide.load.ResourceEncoder;
import com.gangoogle.glide.load.engine.Resource;
import com.gangoogle.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

/**
 * Writes the original bytes of a {@link com.gangoogle.glide.load.resource.gif.GifDrawable} to an
 * {@link java.io.OutputStream}.
 */
public class GifDrawableEncoder implements ResourceEncoder<GifDrawable> {
  private static final String TAG = "GifEncoder";

  @NonNull
  @Override
  public EncodeStrategy getEncodeStrategy(@NonNull Options options) {
    return EncodeStrategy.SOURCE;
  }

  @Override
  public boolean encode(
      @NonNull Resource<GifDrawable> data, @NonNull File file, @NonNull Options options) {
    GifDrawable drawable = data.get();
    boolean success = false;
    try {
      ByteBufferUtil.toFile(drawable.getBuffer(), file);
      success = true;
    } catch (IOException e) {
      if (Log.isLoggable(TAG, Log.WARN)) {
        Log.w(TAG, "Failed to encode GIF drawable data", e);
      }
    }
    return success;
  }
}
