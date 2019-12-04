package com.test.glidetest;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.gangoogle.glide.GlideMod;
import com.gangoogle.glide.load.engine.BaseKey;
import com.gangoogle.glide.load.engine.DiskCacheStrategy;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

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
                .load(videoPath, new BaseKey(videoPath))
                .into(ivImage);
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
