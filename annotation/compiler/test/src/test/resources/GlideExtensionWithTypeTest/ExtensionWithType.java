package com.gangoogle.glide.test;

import androidx.annotation.NonNull;
import com.gangoogle.glide.RequestBuilder;
import com.gangoogle.glide.annotation.GlideExtension;
import com.gangoogle.glide.annotation.GlideType;

@GlideExtension
public final class ExtensionWithType {

  private ExtensionWithType() {
    // Utility class.
  }

  @NonNull
  @GlideType(Number.class)
  public static RequestBuilder<Number> asNumber(RequestBuilder<Number> builder) {
    return builder;
  }
}
