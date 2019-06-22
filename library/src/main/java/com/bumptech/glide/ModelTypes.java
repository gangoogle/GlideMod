package com.bumptech.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

import com.bumptech.glide.load.engine.BaseKey;
import java.io.File;
import java.net.URL;

/**
 * Ensures that the set of explicitly supported model types remains consistent across Glide's API
 * surface.
 */
interface ModelTypes<T> {
  @NonNull
  @CheckResult
  T load(@Nullable Bitmap bitmap, BaseKey key);

  @NonNull
  @CheckResult
  T load(@Nullable Drawable drawable,BaseKey key);

  @NonNull
  @CheckResult
  T load(@Nullable String string,BaseKey key);

  @NonNull
  @CheckResult
  T load(@Nullable Uri uri,BaseKey key);

  @NonNull
  @CheckResult
  T load(@Nullable File file,BaseKey key);

  @NonNull
  @CheckResult
  T load(@RawRes @DrawableRes @Nullable Integer resourceId, BaseKey key);

  @Deprecated
  @CheckResult
  T load(@Nullable URL url,BaseKey key);

  @NonNull
  @CheckResult
  T load(@Nullable byte[] model,BaseKey key);

  @NonNull
  @CheckResult
  @SuppressWarnings("unchecked")
  T load(@Nullable Object model,BaseKey key);
}
