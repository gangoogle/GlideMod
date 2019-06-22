package com.gangoogle.glide.test;

import androidx.annotation.NonNull;
import com.gangoogle.glide.annotation.GlideExtension;
import com.gangoogle.glide.annotation.GlideOption;
import com.gangoogle.glide.request.BaseRequestOptions;

@GlideExtension
public final class Extension {

  private Extension() {
    // Utility class.
  }

  @NonNull
  @GlideOption(override = GlideOption.OVERRIDE_EXTEND)
  public static BaseRequestOptions<?> override(BaseRequestOptions<?> requestOptions, int width, int height) {
    return requestOptions
        .override(width, height)
        .centerCrop();
  }
}
