package com.gangoogle.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * A target for display {@link Drawable} objects in {@link ImageView}s.
 */
public class DrawableImageViewTarget extends ImageViewTarget<Drawable> {
    private final int BITMAP_MAX_SIZE = 100 * 1024 * 1024;

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
            view.setImageDrawable(resource);
            return;
        }
        Bitmap bitmap = drawable2Bitmap(resource);
        if (bitmap == null) {
            view.setImageDrawable(resource);
            return;
        }
//        Log.d("bitmap", "ordinal:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
        bitmap = compressBitmap(bitmap);
//        Log.d("bitmap", "new -ordinal:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
        view.setImageDrawable(new BitmapDrawable(bitmap));

    }


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
