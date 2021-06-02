package com.gangoogle.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.widget.ImageView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.gangoogle.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;

/**
 * A base {@link com.gangoogle.glide.request.target.Target} for displaying resources in {@link
 * android.widget.ImageView}s.
 *
 * @param <Z> The type of resource that this target will display in the wrapped {@link
 *     android.widget.ImageView}.
 */
// Public API.
@SuppressWarnings("WeakerAccess")
public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z>
    implements Transition.ViewAdapter {
  private final int BITMAP_MAX_SIZE = 100 * 1024 * 1024;

  @Nullable private Animatable animatable;

  public ImageViewTarget(ImageView view) {
    super(view);
  }

  /** @deprecated Use {@link #waitForLayout()} instead. */
  @SuppressWarnings({"deprecation"})
  @Deprecated
  public ImageViewTarget(ImageView view, boolean waitForLayout) {
    super(view, waitForLayout);
  }

  /**
   * Returns the current {@link android.graphics.drawable.Drawable} being displayed in the view
   * using {@link android.widget.ImageView#getDrawable()}.
   */
  @Override
  @Nullable
  public Drawable getCurrentDrawable() {
    return view.getDrawable();
  }

  /**
   * Sets the given {@link android.graphics.drawable.Drawable} on the view using {@link
   * android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)}.
   *
   * @param drawable {@inheritDoc}
   */
  @Override
  public void setDrawable(Drawable drawable) {
    if (drawable == null) {
      view.setImageDrawable(drawable);
      return;
    }
    Bitmap bitmap = drawable2Bitmap(drawable);
    if (bitmap == null) {
      view.setImageDrawable(drawable);
      return;
    }
//        Log.d("bitmap", "ordinal:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
    bitmap = compressBitmap(bitmap);
//        Log.d("bitmap", "new -ordinal:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
    view.setImageDrawable(new BitmapDrawable(bitmap));
  }

  /**
   * Sets the given {@link android.graphics.drawable.Drawable} on the view using {@link
   * android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)}.
   *
   * @param placeholder {@inheritDoc}
   */
  @Override
  public void onLoadStarted(@Nullable Drawable placeholder) {
    super.onLoadStarted(placeholder);
    setResourceInternal(null);
    setDrawable(placeholder);
  }

  /**
   * Sets the given {@link android.graphics.drawable.Drawable} on the view using {@link
   * android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)}.
   *
   * @param errorDrawable {@inheritDoc}
   */
  @Override
  public void onLoadFailed(@Nullable Drawable errorDrawable) {
    super.onLoadFailed(errorDrawable);
    setResourceInternal(null);
    setDrawable(errorDrawable);
  }

  /**
   * Sets the given {@link android.graphics.drawable.Drawable} on the view using {@link
   * android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)}.
   *
   * @param placeholder {@inheritDoc}
   */
  @Override
  public void onLoadCleared(@Nullable Drawable placeholder) {
    super.onLoadCleared(placeholder);
    if (animatable != null) {
      animatable.stop();
    }
    setResourceInternal(null);
    setDrawable(placeholder);
  }

  @Override
  public void onResourceReady(@NonNull Z resource, @Nullable Transition<? super Z> transition) {
    if (transition == null || !transition.transition(resource, this)) {
      setResourceInternal(resource);
    } else {
      maybeUpdateAnimatable(resource);
    }
  }

  @Override
  public void onStart() {
    if (animatable != null) {
      animatable.start();
    }
  }

  @Override
  public void onStop() {
    if (animatable != null) {
      animatable.stop();
    }
  }

  private void setResourceInternal(@Nullable Z resource) {
    // Order matters here. Set the resource first to make sure that the Drawable has a valid and
    // non-null Callback before starting it.
    setResource(resource);
    maybeUpdateAnimatable(resource);
  }

  private void maybeUpdateAnimatable(@Nullable Z resource) {
    if (resource instanceof Animatable) {
      animatable = (Animatable) resource;
      animatable.start();
    } else {
      animatable = null;
    }
  }

  protected abstract void setResource(@Nullable Z resource);


  private Bitmap compressBitmap(Bitmap bitmap) {
    if (getBitmapSize(bitmap) < BITMAP_MAX_SIZE) {
      return bitmap;
    }
    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inSampleSize = 1;
    opts.inPreferredConfig = Bitmap.Config.RGB_565;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int quality = 100;
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
    // 循环判断压缩后图片是否超过限制大小
    while (baos.toByteArray().length > BITMAP_MAX_SIZE) {
      // 清空baos
      baos.reset();
      quality -= 10;
      bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
    }
    bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), opts);
    return bitmap;
  }

  /**
   * 得到bitmap的大小
   */
  public static int getBitmapSize(Bitmap bitmap) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
      return bitmap.getAllocationByteCount();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
      return bitmap.getByteCount();
    }
    // 在低版本中用一行的字节x高度
    return ((bitmap.getRowBytes() * bitmap.getHeight()));                //earlier version
  }

  public static Bitmap drawable2Bitmap(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else if (drawable instanceof NinePatchDrawable) {
      Bitmap bitmap = Bitmap
              .createBitmap(
                      drawable.getIntrinsicWidth(),
                      drawable.getIntrinsicHeight(),
                      drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                              : Bitmap.Config.RGB_565);
      Canvas canvas = new Canvas(bitmap);
      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
              drawable.getIntrinsicHeight());
      drawable.draw(canvas);
      return bitmap;
    } else {
      return null;
    }
  }
}
