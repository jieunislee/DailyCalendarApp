package com.koreais.haruproject3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koreais.haruproject3.decorators.EventDecorator;
import com.koreais.haruproject3.decorators.OneDayDecorator;
import com.koreais.haruproject3.decorators.SaturdayDecorator;
import com.koreais.haruproject3.decorators.SundayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class Haru_Calendar extends AppCompatActivity {

    private static final String TAG = "Haru_Calendar";

    private FloatingActionButton fab;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private ListView listView;
    MaterialCalendarView materialCalendarView;
    //String time,kcal,menu;
    public static String selectedDate;
    String selectedDateForDB;
    View my_view;
    TextView view_title;
    boolean isUp;
    static DatabaseHelper mDatabaseHelper;
    Set<String> eventData;
    String[] eventArr;
    TextView txt_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        my_view = findViewById(R.id.my_view);
        my_view.setVisibility(View.INVISIBLE);
        view_title = findViewById(R.id.view_title);
        isUp = false;
        listView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        txt_btn = findViewById(R.id.txt_btn);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);


        materialCalendarView.state().edit()
                .setMinimumDate(CalendarDay.from(1900, 1, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2100, 12, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        insertEventsToHash();

        eventArr  = new String[eventData.size()];
        eventData.toArray(eventArr);

        int random = eventData.size();
        String rs = String.valueOf(random);

        Log.d(TAG, rs);

        //String[] result = {"2017,03,18","2017,04,18","2017,05,18","2017,06,18"};

        new ApiSimulator(eventArr).executeOnExecutor(Executors.newSingleThreadExecutor());

        // 날짜가 눌리면 Toast로 해당 요일 보여주기
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                String str_Month = "";
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                if( Month < 10 ) {
                    str_Month = "0" + Month;
                }

                selectedDate = Year + "-" + str_Month + "-" + Day;
                selectedDateForDB = Year + "," + Month + "," + Day;
                Log.d(TAG, selectedDateForDB);
                mDatabaseHelper.passDate(selectedDateForDB);



                Log.i(TAG, selectedDate + "");

                if(isUp){
                    slideDown(my_view);
                    materialCalendarView.setTileHeight(200);

                }else{
                    slideUp(my_view);
                    materialCalendarView.setTileHeight(150);
                    view_title.setText(selectedDate);
                    populateListView();
                    txt_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Haru_Calendar.this, detailActivity.class);
                            startActivity(i);
                        }
                    });

                }
                isUp = !isUp;

            }
        });



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                materialCalendarView.clearSelection();
                showAddItemDialog(Haru_Calendar.this);
            }
        });


    } // onCreate()


    private void showAddItemDialog(Context c) {

        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView viewSelectedDate = new TextView(c);
        viewSelectedDate.setText(selectedDate);
        layout.addView(viewSelectedDate);

        final EditText taskEditText = new EditText(c);
        taskEditText.setHint("일정 입력");
        layout.addView(taskEditText);



        AlertDialog.Builder dialog = new AlertDialog.Builder(c)
                .setTitle("할 일")
                .setView(layout)
                
        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //final String  result = "일정 : " + selectedDate + "\n" + "설명 : " + taskEditText.getText();
                        //toastMessage(result);
                        String todo = taskEditText.getText().toString();
                        if(taskEditText.length() != 0){
                            AddData(selectedDateForDB,todo);
                            taskEditText.setText("");
                        } else {
                            toastMessage("할 일을 적어주세요~");
                        }
                    }
                })
        
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel
            }
        });
        dialog.create();
        dialog.show();
    } // showAddItemDialog()

    public void slideUp(View view){
        view.setSystemUiVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(),0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    } // slideUp()

    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    } // slideDown()

    public void toastMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    } // toastMessage()


    public void AddData(String date, String todo){
        boolean insertData = mDatabaseHelper.addData(date, todo);

        if(insertData){
            toastMessage("Data successfully inserted");
        }else{
            toastMessage("Failed to insert Data...!!!");
        }
    } // AddData()

    private void populateListView(){
        Log.d(TAG,"populateListView: Displaying Data in ListView");

        // get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(2));
        }

        // Create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        // set an onItemClickListener to the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String todo = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You clicked on " + todo);

                Cursor data = mDatabaseHelper.getItemID(todo); // get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is : " + itemID);

                }
            }
        });
    } // populateListView()

    private void insertEventsToHash(){
        Log.d(TAG,"populateEvents: adding events' dates to hashset");
        Cursor data = mDatabaseHelper.getItemDate();
        eventData = new HashSet<>();
        while(data.moveToNext()){
            eventData.add(data.getString(1));
        }

    } //insertEventsToHash()


    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] event_result;

        public ApiSimulator(String[] event_result){
            this.event_result = event_result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            //calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();

            for (int i = 0; i < event_result.length; i++) {

                CalendarDay day = CalendarDay.from(calendar);
                String[] event = event_result[i].split(",");
                int event_year = Integer.parseInt(event[0]);
                int event_month = Integer.parseInt(event[1]);
                int event_day = Integer.parseInt(event[2]);

                dates.add(day);
                calendar.set(event_year, (event_month-1), event_day);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));
        }
    }

    public static String getDate() {
        return selectedDate;
    }

} // Class Calendar
