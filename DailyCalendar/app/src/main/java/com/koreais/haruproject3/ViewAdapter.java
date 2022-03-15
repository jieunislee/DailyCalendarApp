package com.koreais.haruproject3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAdapter extends ArrayAdapter<CallLogVO>{
    Context context;
    ArrayList<CallLogVO> list;
    CallLogVO vo;
    int resource;

    public ViewAdapter(Context context, int resource, ArrayList<CallLogVO> list, ListView mycallLog) {
        super(context, resource, list);

        Log.d("mylog", "ViewAdapter, ViewAdapter() call");

        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView
            (int position, View convertView, ViewGroup parent) {
        // xml 문서 (book_item.xml)를 객체화 시켜주는 클래스
        LayoutInflater linf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // resource는 생성자에서 book_item.xml 의 id 값을 전달받았었다.
        convertView = linf.inflate(resource, null);


        vo = list.get(position);

        // activity_call_log.xml 뷰에 연결하기
        TextView date = convertView.findViewById(R.id.date);
        TextView call_type = convertView.findViewById(R.id.call_type);
        TextView name = convertView.findViewById(R.id.name);
        TextView phone = convertView.findViewById(R.id.phone);
        TextView duration = convertView.findViewById(R.id.duration);

        date.setText(vo.getDate());
        call_type.setText(vo.getCall_type());
        name.setText(vo.getName());
        phone.setText(vo.getPhone());
        duration.setText(vo.getDuration());


        return convertView;
    }
}
