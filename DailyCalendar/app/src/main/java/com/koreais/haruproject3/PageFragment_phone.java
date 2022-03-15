package com.koreais.haruproject3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PageFragment_phone extends Fragment {

    CallLogActivity calllog; // 통화 기록 리스트를 생성해주는 객체
    ListView mycallLog;
    ArrayList<CallLogVO> list = null;
    ArrayList<CallLogVO> selected_list = new ArrayList<>();
    ViewAdapter adapter;
    TextView phone_txt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.activy_page_flagment_phone, container, false);

        Log.d("mylog", "PageFragment_phone, onCreateView() call");
        calllog = new CallLogActivity(getContext());

        adapter = null;
        list = calllog.callLog(); // 리스트 객체 가져오기

        phone_txt = layout.findViewById(R.id.phone_txt);

        // 선택된 날짜에 해당되는 통화 기록 객체를 selected_list 에 저장하기
        for (int i = 0; i < list.size(); i++) {
            if ( list.get(i).getDate().equals(Haru_Calendar.getDate()) ) {
                selected_list.add(list.get(i));
            }
        }

        mycallLog = layout.findViewById(R.id.mycallLog); // 리스트뷰 객체 가져오기

        if( selected_list.size() == 0 ) {
            phone_txt.setText("No Call Log");
        }
        else {
            if (adapter == null) {
                adapter = new ViewAdapter(getActivity(), R.layout.activity_call_log, selected_list, mycallLog);

                mycallLog.setAdapter(adapter);
            }
        }

        return layout;
    }

}
