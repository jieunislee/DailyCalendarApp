package com.koreais.haruproject3;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koreais.haruproject3.FullImageActivity;
import com.koreais.haruproject3.ImageAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PageFragment_image extends Fragment {

    ImageView imageView;
    Button button;
    ImageAdapter adapter;
    ArrayList<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.activy_page_flagment_image, container, false);

        imageView = (ImageView)layout.findViewById(R.id.image);

        button = (Button)layout.findViewById(R.id.button);

        // 버튼 클릭 시 갤러리에서 가져오기
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*"); //이미지만 가지고 오기
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //여러개 가지고 오기
//                startActivityForResult(Intent.createChooser(intent, "사진선택"), 1);
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> list = getImageList(Haru_Calendar.getDate()); //나중에 캘린더 완성 하면 sDate 바꾸기
            }
        });



        list = getImageList(Haru_Calendar.getDate());


        GridView gridView = layout.findViewById(R.id.gridView);
        adapter = new ImageAdapter(getContext(), list);
        // Instance of ImageAdapter Class
        gridView.setAdapter(adapter);

        //누르면 사진 한장만 크게 보이게
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // 이미지 id 를 FullScreenActivity
                Intent i = new Intent(getActivity().getApplicationContext().getApplicationContext(), FullImageActivity.class);
                // passing array index
                i.putExtra("img_path", list.get(position)); // 선택된 항목의 경로
                startActivity(i);
            }
        });

        return layout;
    }//onCreateView



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성

                    InputStream in = getActivity().getApplicationContext().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }//try catch
            }//if()
        }//if()
    } //onActivity

    //converting date to timestamp
    public static long getTimeStamp(String calculatedDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = formatter.parse(calculatedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long output = 0;

        if (date != null) {
            output = date.getTime()/1000L;
        }
        String str = Long.toString(output);
        return Long.parseLong(str) * 1000;
    }


    public ArrayList<String> getImageList( String sDate ) {
        ArrayList<String> list = new ArrayList<>();

        long givendate_timestamp = getTimeStamp(sDate);             // 2019-01-17 00:00:00
        long nextdate_timestamp = getTimeStamp(sDate) + 86399000;   //2019-01-17 23:59:59


        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.MediaColumns.DATA},
                MediaStore.Images.Media.DATE_TAKEN + ">=? and " + MediaStore.Images.Media.DATE_TAKEN + "<=?",
                new String[]{"" + givendate_timestamp, "" + nextdate_timestamp},
                MediaStore.Images.Media.DATE_TAKEN + " DESC");

        int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        // int columnDisplayname = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);

        while (cursor.moveToNext())
        {
            String absolutePathOfImage = cursor.getString(columnIndex);

            Log.d("mylog", "absolutePathOfImage : " + absolutePathOfImage);

            list.add(absolutePathOfImage);
        }


        Log.d("mylog", "end");

        return list;

    }


}
