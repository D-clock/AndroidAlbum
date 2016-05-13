package com.clock.album.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.clock.album.R;
import com.clock.album.ui.activity.AlbumActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_system_album).setOnClickListener(this);
        findViewById(R.id.btn_image_loader).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.btn_system_album) {//系统相册

            Intent albumIntent = new Intent(this, AlbumActivity.class);
            startActivity(albumIntent);

        } else if (viewId == R.id.btn_image_loader) {//网络图片加载（各大加载图片框架的实现）

        }
    }
}
