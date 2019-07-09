package com.gangoogle.glide.module;

import android.content.Context;
import android.support.annotation.NonNull;
import com.gangoogle.glide.GlideBuilder;

/** An internal interface, to be removed when {@link GlideModule}s are removed. */
@Deprecated
interface AppliesOptions {
  /**
   * Lazily apply options to a {@link com.gangoogle.glide.GlideBuilder} immediately before the Glide
   * singleton is created.
   *
   * <p>This method will be called once and only once per implementation.
   *
   * @param context An Application {@link android.content.Context}.
   * @param builder The {@link com.gangoogle.glide.GlideBuilder} that will be used to create Glide.
   */
  void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder);
}