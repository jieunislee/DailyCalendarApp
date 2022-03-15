package com.koreais.haruproject3;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class detailActivity extends AppCompatActivity {
    ViewPager pager;
    Button btn_diary, btn_image, btn_phone, btn_place;
    TextView selected_date;

    String date = Haru_Calendar.getDate();

    int count; // 페이지 전환할 때마다 증가시킬 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pager = findViewById(R.id.pager);
        btn_diary = findViewById(R.id.btn_diary);
        btn_image = findViewById(R.id.btn_image);
        btn_phone = findViewById(R.id.btn_phone);
        btn_place = findViewById(R.id.btn_place);
        selected_date = (TextView) findViewById(R.id.selected_date);

        selected_date.setText(date);

        // PageAdapter 를 상속 받은 객체 생성
        pager.setAdapter( new MyPageAdapter( getSupportFragmentManager() ) );

        pager.setCurrentItem( PageInfo.PAGE1 );
        btn_diary.setSelected(true); // 다이어리 페이지 버튼 활성화

        pager.setOffscreenPageLimit(2);

        // 페이지 전환을 감지하여 별도의 코드를 작성할 수 있는 리스너
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                count++; // 페이지 전환 시 1씩 증가

                // 일단 모든 버튼을 비활성화
                btn_diary.setSelected(false);
                btn_image.setSelected(false);
                btn_phone.setSelected(false);
                btn_place.setSelected(false);

                // 현재 페이지의 버튼이 색만 바뀐다.
                switch ( i ) {
                    case PageInfo.PAGE1 :
                        btn_diary.setSelected(true);
                        break;
                    case PageInfo.PAGE2 :
                        btn_image.setSelected(true);
                        break;
                    case PageInfo.PAGE3 :
                        btn_phone.setSelected(true);
                        break;
                    case PageInfo.PAGE4 :
                        btn_place.setSelected(true);
                        break;

                } // switch()
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        }); // addOnPageChangeListener()

        // 버튼 클릭 시 처리되도록 리스너 set
        btn_diary.setOnClickListener(click);
        btn_image.setOnClickListener(click);
        btn_phone.setOnClickListener(click);
        btn_place.setOnClickListener(click);

    } // onCreate()

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch ( v.getId() ) {
                case R.id.btn_diary :
                    pager.setCurrentItem( PageInfo.PAGE1 );
                    break;
                case R.id.btn_image :
                    pager.setCurrentItem( PageInfo.PAGE2 );
                    break;
                case R.id.btn_phone :
                    pager.setCurrentItem( PageInfo.PAGE3 );
                    break;
                case R.id.btn_place :
                    pager.setCurrentItem( PageInfo.PAGE4 );
                    break;

            } // switch()
        } // click
    };


}
