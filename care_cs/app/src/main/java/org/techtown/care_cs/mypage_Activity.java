package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mypage_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    Button edit_mypage,view_resum,view_matching_list;
    String Members;
    TextView user_id;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        edit_mypage=findViewById(R.id.edit_mypage);
        view_resum=findViewById(R.id.view_resum);
        view_matching_list=findViewById(R.id.view_matching_list);
        user_id=findViewById(R.id.user_id);
        user_id.setText(session.getId());

        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "메인자동로그인정보불러옴 " + Members);


        edit_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                       Intent intent = new Intent(getApplicationContext(),edit_mypage_Activity.class);
                                                    startActivity(intent);
            }
        });


        view_resum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("Y".equals(session.getResum())){
                           Intent intent = new Intent(getApplicationContext(),view_resum_Activity.class);
                                                        startActivity(intent);
                }else{

                       Intent intent = new Intent(getApplicationContext(),write_resum_Activity.class);
                                                    startActivity(intent);

                }

            }
        });

        view_matching_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),matching_list_Activity.class);
                                                startActivity(intent);

            }
        });


    }
}