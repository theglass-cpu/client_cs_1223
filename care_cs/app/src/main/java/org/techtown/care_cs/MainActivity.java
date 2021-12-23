package org.techtown.care_cs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button resum_list_bt,matc_list_bt,mypage_bt;
    private static final String TAG = "Cannot invoke method length() on null object";
    String members;
    JSONArray level_jsonArray;
    JSONObject level_jsonObject;
    LinearLayout list,admin_list;
    TextView title;
    int a = 0;
    public Intent serviceIntent;



    public void tcp_connetion_service (){
        //service 연결
        Intent intent = new Intent(getApplicationContext(),Tcp_connetion_server.class);
        startService(intent);
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"LongLogTag", "BatteryLife"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resum_list_bt=findViewById(R.id.resum_list_bt);
        admin_list=findViewById(R.id.admin_list);
        matc_list_bt=findViewById(R.id.matc_list_bt);
        mypage_bt=findViewById(R.id.mypage_bt);
        list=findViewById(R.id.list);
        title=findViewById(R.id.title);
        a=getIntent().getIntExtra("tcp",1);
        if(a==1){
            Log.e(TAG, "서버 최초한번실행: " );
            tcp_connetion_service();
        }

        //메인에서 서비스 스타트

      //  restart();
        Log.e(TAG, "메인: "+session.getLevel() );
        Log.e(TAG, "메인"+session.getResum());





        if("1".equals(session.getLevel())){
            //일반회원이면 공고내역 안보이게
            admin_list.setVisibility(View.GONE);
        }else{
            list.setVisibility(View.GONE);
        }


        //공고 신청내역 
        resum_list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   Intent intent = new Intent(getApplicationContext(),resum_list_Activity.class);
                                                startActivity(intent);

            }
        });


        //보호자의요청들
        matc_list_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(), client_request_list_Activity.class);
                                                startActivity(intent);

            }
        });

        //회원정보
        mypage_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                startActivity(intent);

            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "메인 onDestroy: " );
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }
}