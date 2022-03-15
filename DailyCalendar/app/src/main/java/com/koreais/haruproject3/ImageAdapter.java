package com.koreais.haruproject3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> list;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.image, R.drawable.image, R.drawable.image
    };

    // Constructor
    public ImageAdapter(Context c, ArrayList<String> list){
        mContext = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);


        //imageView.setImageResource(mThumbIds[position]);

        Bitmap bm = BitmapFactory.decodeFile(list.get(position));
        imageView.setImageBitmap(bm);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 300));
        return imageView;
    }

}