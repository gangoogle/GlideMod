package com.gangoogle.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.widget.ImageView;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;

/**
 * A target for display {@link Drawable} objects in {@link ImageView}s.
 */
public class DrawableImageViewTarget extends ImageViewTarget<Drawable> {

    public DrawableImageViewTarget(ImageView view) {
        super(view);
    }

    /**
     * @deprecated Use {@link #waitForLayout()} instead.
     */
    // Public API.
    @SuppressWarnings({"unused", "deprecation"})
    @Deprecated
    public DrawableImageViewTarget(ImageView view, boolean waitForLayout) {
        super(view, waitForLayout);
    }

    @Override
    protected void setResource(@Nullable Drawable resource) {
        if (resource == null) {
            view.setImageDrawable(null);
            return;
        }
        Bitmap bitmap = drawable2Bitmap(resource);
        /*传入bitmap参数，返回bitmap。*/
        ByteArrayOutputStream dataByte = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, dataByte);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(dataByte.toByteArray(), 0, dataByte.size(), opts);
        view.setImageDrawable(new BitmapDrawable(bitmap));
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
