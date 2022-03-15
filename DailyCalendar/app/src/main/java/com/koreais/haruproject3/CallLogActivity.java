package com.koreais.haruproject3;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CallLogActivity {

    Context context = null;

    public CallLogActivity( Context context ) {
        this.context = context;
    }

    CallLogVO vo;

    public static final String MESSAGE_TYPE_INBOX = "1";
    public static final String MESSAGE_TYPE_SENT = "2";
    public static final String MESSAGE_TYPE_CONVERSATIONS = "3";
    //public static final String MESSAGE_TYPE_NEW = "new";

    final static private String[] CALL_PROJECTION = { CallLog.Calls.TYPE,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER,
            CallLog.Calls.DATE, CallLog.Calls.DURATION };

    private Cursor getCallHistoryCursor() {
        Cursor cursor = null;

        try {
            cursor = ((Activity)context).managedQuery(CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
                    null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
//            cursor = managedQuery(CallLog.Calls.CONTENT_URI, CALL_PROJECTION,
//                    null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        } catch( Exception e ) {

            Log.d("mylog", "CallLogActivity, getCallHistoryCursor() 에러 : " + e.getMessage());
        }

        return cursor;
    }

    public ArrayList<CallLogVO> callLog() {
        Log.d("mylog", "CallLogActivity, callLog() call");
        int callcount = 0;
        String callname = "";
        String calltype = "";
        String calllog = "";

        ArrayList<CallLogVO> list = new ArrayList<>();

        Cursor curCallLog = getCallHistoryCursor();

        if( curCallLog.moveToFirst() && curCallLog.getCount() > 0 ) {

            while( curCallLog.isAfterLast() == false ) {
                vo = new CallLogVO();

                if( curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_INBOX) ) {
                    calltype = "수신";
                }
                else if( curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_SENT) ) {
                    calltype = "발신";
                }
                else if( curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.TYPE)).equals(MESSAGE_TYPE_CONVERSATIONS) ) {
                    calltype = "부재중";
                }

                if( curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME)) == null ) {
                    callname = "NO Name";
                }
                else {
                    callname = curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.CACHED_NAME));
                }


                // 객체 vo에 저장
                vo.setCall_type(calltype);
                vo.setName(callname);
                vo.setPhone( curCallLog.getString(curCallLog.getColumnIndex(CallLog.Calls.NUMBER)) );

                int duration = curCallLog.getInt(curCallLog.getColumnIndex(CallLog.Calls.DURATION));
                long Timemillis = duration * 1000;
                Date date = new Date(Timemillis);
                String timeString = new SimpleDateFormat("HH:mm:ss").format(date);
                vo.setDuration( timeString );

                long dateTimeMillis = curCallLog.getLong(curCallLog.getColumnIndex(CallLog.Calls.DATE));
                date = new Date(dateTimeMillis);
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
                vo.setDate( dateString );

                // 리스트에 추가
                list.add(vo);

                curCallLog.moveToNext();

                callcount++;

            } // while
        } //if

        return list;
    } // callLog()

}
