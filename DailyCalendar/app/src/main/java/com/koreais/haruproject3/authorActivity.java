package com.koreais.haruproject3;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class authorActivity extends AppCompatActivity {
    boolean isFirst = true; // true면 최초 실행, false면 최초 아님

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED ) {
            // 통화 기록 읽기 권한 없을 때
            setPermission();
            return;
        }

        loadFirst();
        saveFirst();

        Intent i = new Intent(authorActivity.this, Haru_Calendar.class);
        startActivity(i);
        finish();

    } // onCreate()

    //--------------------------------------------------------------------
    // 권한체크코드
    //--------------------------------------------------------------------
    private void setPermission() {
        TedPermission.with(this)
                .setPermissionListener( permissionListener )
                .setDeniedMessage("통화 기록 접근 권한을 수락하세요.\n\n[설정] -> [권한]에서 활성화할 수 있습니다.")
                .setPermissions( Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE )
                .check();
    } // setPermission()

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            // 모든 권한이 다 수락된 경우 수행되는 메서드

            // 메인 액티비티를다시 구동한다.
            Intent i = new Intent(authorActivity.this, authorActivity.class);
            startActivity(i);
            finish();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            // 하나라도 수락하지 않은 권한이 있는 경우 수행되는 메서드
            // deniedPermissions --> 수락되지 않은 권한 목록이 있는 리스트

            finish(); // 현재 액티비티 종료 (그냥 앱 종료)
        }
    };
    // 권한 체크 끝
    //------------------------------------------------------------------------------

    // 최초 구동 여부를 xml 에서 읽는다.
    private void loadFirst() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        isFirst = pref.getBoolean("save", true); // save 키값이 없으면 기본 값 true (최초)
    } // loadFirst()

    // DB 파일을 내부저장소에 이동시켰다면 isFirst 는 false 이다. (해당 값을 기록)
    private void saveFirst() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("save", isFirst);
        edit.commit();
    } // saveFirst()

}
