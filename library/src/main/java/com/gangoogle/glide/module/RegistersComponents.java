package com.gangoogle.glide.module;

import android.content.Context;
import android.support.annotation.NonNull;
import com.gangoogle.glide.GlideMod;
import com.gangoogle.glide.Registry;

/** An internal interface, to be removed when {@link GlideModule}s are removed. */
// Used only in javadocs.
@SuppressWarnings("deprecation")
@Deprecated
interface RegistersComponents {

  /**
   * Lazily register components immediately after the Glide singleton is created but before any
   * requests can be started.
   *
   * <p>This method will be called once and only once per implementation.
   *
   * @param context An Application {@link android.content.Context}.
   * @param glideMod The Glide singleton that is in the process of being initialized.
   * @param registry An {@link com.gangoogle.glide.Registry} to use to register components.
   */
  void registerComponents(
          @NonNull Context context, @NonNull GlideMod glideMod, @NonNull Registry registry);
}
