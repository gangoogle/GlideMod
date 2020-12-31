package com.gangoogle.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * A {@link com.gangoogle.glide.request.target.Target} that can display an {@link
 * android.graphics.Bitmap} in an {@link android.widget.ImageView}.
 */
public class BitmapImageViewTarget extends ImageViewTarget<Bitmap> {
    private final int BITMAP_MAX_SIZE = 100 * 1024 * 1024;

    // Public API.
    @SuppressWarnings("WeakerAccess")
    public BitmapImageViewTarget(ImageView view) {
        super(view);
    }

    /**
     * @deprecated Use {@link #waitForLayout()} instead.
     */
    // Public API.
    @SuppressWarnings({"unused", "deprecation"})
    @Deprecated
    public BitmapImageViewTarget(ImageView view, boolean waitForLayout) {
        super(view, waitForLayout);
    }

    /**
     * Sets the {@link android.graphics.Bitmap} on the view using {@link
     * android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)}.
     *
     * @param resource The bitmap to display.
     */
    @Override
    protected void setResource(Bitmap resource) {
        if (resource == null) {
            view.setImageBitmap(resource);
            return;
        }
        Bitmap bitmap = resource;
//        Log.d("bitmap", "ordinal:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
        bitmap = compressBitmap(bitmap);
//        Log.d("bitmap", "new:" + bitmap.getConfig().ordinal() + "-size:" + getBitmapSize(bitmap));
        view.setImageBitmap(bitmap);
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
}
