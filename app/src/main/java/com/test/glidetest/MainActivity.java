package com.test.glidetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.BaseKey;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView ivImage = findViewById(R.id.imageView);
        Glide.with(this).load("http://pic25.nipic.com/20121205/10197997_003647426000_2.jpg", new BaseKey() {
            @Override
            public String getKey() {
                return "KeyAs";
            }
        })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .onlyRetrieveFromCache(false)
                .dontAnimate()
                .into(ivImage);
    }
}
