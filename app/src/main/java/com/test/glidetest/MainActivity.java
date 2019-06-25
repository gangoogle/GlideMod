package com.test.glidetest;

import android.Manifest;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.gangoogle.glide.GlideMod;
import com.gangoogle.glide.load.engine.BaseKey;
import com.gangoogle.glide.load.engine.DiskCacheStrategy;
import com.tbruyelle.rxpermissions2.RxPermissions;

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
        final String videoPath = "/storage/emulated/0/DCIM/Camera/v0200f080000bgsjsrceae1efua6o8og.MP4";
        GlideMod.with(this)
                .load(videoPath, new BaseKey() {
                    @Override
                    public String getKey() {
                        return videoPath+"s7s";
                    }
                })
                .into(ivImage);
    }
}
