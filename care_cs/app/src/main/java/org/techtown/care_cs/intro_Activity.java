package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class intro_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                overridePendingTransition(R.anim.slid,R.anim.slid);
                startActivity(intent); //인트로 실행 후 바로 MainActivity로 넘어감.
                finish();
            }
        }, 2000); //2초 후 인트로 실행
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}