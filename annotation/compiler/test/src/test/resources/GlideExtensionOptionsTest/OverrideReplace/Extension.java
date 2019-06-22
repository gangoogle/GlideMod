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
  @GlideOption(override = GlideOption.OVERRIDE_REPLACE)
  public static BaseRequestOptions<?> centerCrop(BaseRequestOptions<?> requestOptions) {
    return requestOptions.centerCrop();
  }
}
