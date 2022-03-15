package com.koreais.haruproject3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.koreais.haruproject3.Haru_Calendar.mDatabaseHelper;

public class PageFragment_diary extends Fragment {

    private static final String TAG = "PageFragment_diary";

    ListView myDiaryLog;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = (LinearLayout)inflater.inflate(R.layout.activy_page_flagment_diary, container, false);

        Log.d(TAG, "PageFragment_diary: Displaying Data in ListView");

        myDiaryLog = layout.findViewById(R.id.myDiarylog);

        Cursor data = Haru_Calendar.mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            listData.add(data.getString(2));
        }

        ListAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listData);
        myDiaryLog.setAdapter(adapter);

        myDiaryLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(getActivity(), EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    startActivity(editScreenIntent);
                }
                else {
                    Toast.makeText(getActivity(), "No events associated this id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return layout;
    }



} // PageFragment_diary()
