package com.gangoogle.glide.test;

import androidx.annotation.NonNull;
import com.gangoogle.glide.annotation.GlideExtension;
import com.gangoogle.glide.annotation.GlideOption;
import com.gangoogle.glide.request.BaseRequestOptions;

@GlideExtension
public final class ExtensionWithOption {

  private ExtensionWithOption() {
    // Utility class.
  }

  @NonNull
  @GlideOption
  public static BaseRequestOptions<?> squareThumb(BaseRequestOptions<?> requestOptions) {
    return requestOptions.centerCrop();
  }
}
