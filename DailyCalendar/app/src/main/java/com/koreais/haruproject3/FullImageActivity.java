package com.koreais.haruproject3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        // get intent data
        Intent i = getIntent();

        // Selected image id
        String path = i.getExtras().getString("img_path");

        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
        Bitmap bm = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bm);
    }

}