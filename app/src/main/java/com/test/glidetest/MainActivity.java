package com.test.glidetest;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gangoogle.glide.GlideMod;
import com.gangoogle.glide.load.DecodeFormat;
import com.gangoogle.glide.load.engine.BaseKey;
import com.gangoogle.glide.load.engine.DiskCacheStrategy;
import com.gangoogle.glide.request.target.SimpleTarget;
import com.gangoogle.glide.request.transition.Transition;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivImage = findViewById(R.id.imageView);
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        loadImg();
                    }
                });
    }

    private void loadImg() {
//        final String videoPath = "/storage/emulated/0/Pictures/Screenshots/Screenshot_20191212-084120.jpg";
        final String videoPath = "/storage/emulated/0/DCIM/Camera/yx-3-16093165421930.png";
        final String videoPath2 = "/storage/emulated/0/DCIM/Camera/yx-3-16093175631670.jpg";
//                final String videoPath = "/storage/emulated/0/DCIM/Camera/IMG_20191217_144134.jpg";
//        GlideMod.with(this)
//                .asBitmap()
//                .load(videoPath, new BaseKey(System.currentTimeMillis() + ""))
//                .dontTransform()
//                .dontAnimate()
//                .into(new SimpleTarget<Bitmap>(SimpleTarget.SIZE_ORIGINAL, SimpleTarget.SIZE_ORIGINAL) {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        ((TextView) MainActivity.this.findViewById(R.id.tv_text))
//                                .setText("width:" + resource.getWidth() + "-height:" + resource.getHeight()
//                                        + "-des:" + resource.getDensity());
//
//                    }
//                });
        File file = new File(videoPath);
       boolean is= file.exists();
        GlideMod.with(this)
                .load(videoPath, new BaseKey(System.currentTimeMillis() + ""))
                .dontTransform()
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate()
                .into(ivImage);

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(videoPath, options);
//        options.inSampleSize = 1;
//        options.inScaled = false;
//        options.inJustDecodeBounds = false;
//
//        ((TextView) MainActivity.this.findViewById(R.id.tv_text_a))
//                .setText("原图：width:" + options.outWidth + "height:" + options.outHeight + "---\n" +
//                        "den:" + getResources().getDisplayMetrics().density + "denIn:" +
//                        getResources().getDisplayMetrics().densityDpi);
    }

    public void test(View view) {
        ArrayList list = new ArrayList<TestClass>();
        for (int i = 0; i < 2000; i++) {
            list.add(new TestClass("1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i, "1" + i));
        }
        Intent intent = new Intent(this, Main2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", list);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
