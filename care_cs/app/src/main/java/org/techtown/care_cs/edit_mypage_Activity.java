package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class edit_mypage_Activity extends AppCompatActivity {

    TextView id;
    EditText pw, new_pw, new_pw2;
    Button result_bt;
    private static final String TAG = "Cannot invoke method length() on null object";
    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
  


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mypage);
        id = findViewById(R.id.id);
        pw = findViewById(R.id.pw);
        new_pw = findViewById(R.id.new_pw);
        new_pw2 = findViewById(R.id.new_pw2);
        result_bt = findViewById(R.id.result_bt);

        id.setText(session.getId());
        php_jsonArray=new JSONArray();
        php_jsonObject=new JSONObject();

        result_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pw.getText().toString() == null || pw.getText().toString().equals("")) {
                    msg.toast(getApplicationContext(), "공백이 존재합니다");
                } else {
                    if (new_pw.getText().toString() == null || new_pw.getText().toString().equals("")) {
                        msg.toast(getApplicationContext(), "공백이 존재합니다");
                    } else {

                        if(new_pw.getText().toString().equals(new_pw2.getText().toString())){

                            SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onResponse(String response) {
                                    Log.e(TAG, "onResponse: " + response);

                                    try {
                                        php_jsonObject=new JSONObject(response);
                                        boolean success =php_jsonObject.getBoolean("success");

                                        if(success){
                                            msg.toast(getApplicationContext(),"변경완료");
                                            //섀어드에서 자동로그인 변경 메인으로
                                        }else{
                                            msg.toast(getApplicationContext(),"현재 비밀번호가 맞지않습니다");
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            smpr.addStringParam("mode", "new_pw");
                            smpr.addStringParam("pw", pw.getText().toString());
                            smpr.addStringParam("pw2", new_pw.getText().toString());
                            smpr.addStringParam("id", id.getText().toString());

                            //요청객체를 서버로 보낼 우체통 같은 객체 생성
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(smpr);

                        }else {
                            msg.toast(getApplicationContext(),"새로운 비밀번호가 맞지않습니다");
                        }


                    }
                }


            }
        });


    }
}