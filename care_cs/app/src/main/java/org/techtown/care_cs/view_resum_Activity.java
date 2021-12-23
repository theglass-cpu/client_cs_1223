package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class view_resum_Activity extends AppCompatActivity {

    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
    private static final String TAG = "Cannot invoke method length() on null object";
    ImageView resum_profile;
    TextView resum_status;
    TextView resum_name, resum_sx, resum_age, resum_lo, resum_level, resum_cf, resum_wh,
            resum_ml, resum_mv, resum_df, text_resum;

    Button resum_del, view_df;
    String Members,cs_id;
    String ch1, ch2, ch3, index;
    ImageView imageView;
    JSONObject sh_jsonObject;
    JSONArray sh_jsonArray;


    int a = 0;

    @SuppressLint({"LongLogTag", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resum);

        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);

        resum_profile = findViewById(R.id.resum_profile);
        resum_status = findViewById(R.id.resum_status);
        resum_name = findViewById(R.id.resum_name);
        resum_sx = findViewById(R.id.resum_sx);
        resum_age = findViewById(R.id.resum_age);
        resum_lo = findViewById(R.id.resum_lo);
        resum_level = findViewById(R.id.resum_level);
        resum_cf = findViewById(R.id.resum_cf);
        resum_wh = findViewById(R.id.resum_wh);
        resum_ml = findViewById(R.id.resum_ml);
        resum_mv = findViewById(R.id.resum_mv);
        resum_df = findViewById(R.id.resum_df);
        resum_del = findViewById(R.id.resum_del);
        text_resum = findViewById(R.id.text_resum);
        view_df = findViewById(R.id.view_df);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dial_log);
        ch1 = "스스로 가능하신 분";
        ch2 = "전지적 도움이 필요하신 분";
        ch3 = "둘다 상관없음";
        php_jsonArray = new JSONArray();
        php_jsonObject = new JSONObject();

        text_resum.setText(session.getId() + "님의 공고");

        sh_jsonArray = new JSONArray();
        sh_jsonObject = new JSONObject();
        try {
            sh_jsonArray = new JSONArray(Members);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
            @SuppressLint({"LongLogTag", "UseCompatLoadingForDrawables", "CheckResult", "SetTextI18n"})
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response);
                try {
                    php_jsonObject = new JSONObject(response);
                    php_jsonArray = new JSONArray(php_jsonObject.getString("response"));
                    for (int i = 0; i < php_jsonArray.length(); i++) {
                        php_jsonObject = php_jsonArray.getJSONObject(i);

                    }

                    if("N".equals(php_jsonObject.getString("ok"))){

                    }else{
                        resum_status.setText("게시완료");
                    }
                    cs_id=php_jsonObject.getString("id");
                    resum_name.setText(php_jsonObject.getString("name"));
                    if ("".equals(php_jsonObject.getString("pp")) || null == php_jsonObject.getString("pp")) {
                        resum_profile.setImageDrawable(getResources().getDrawable(R.drawable.user));
                    } else {

                        Glide.with(getApplicationContext()).load(url.ip + php_jsonObject.getString("pp")).into(resum_profile);
                    }
                    if ("1".equals(php_jsonObject.getString("sx"))) {
                        resum_sx.setText("남자");
                    } else {
                        resum_sx.setText("여자");
                    }
                    resum_age.setText(php_jsonObject.getString("age"));
                    if ("n".equals(php_jsonObject.getString("lo"))) {
                        resum_lo.setText("지역 상관없음");
                    } else {
                        resum_lo.setText(php_jsonObject.getString("lo"));
                    }
                    if ("1".equals(php_jsonObject.getString("level"))) {
                        resum_level.setText(php_jsonObject.getString("level") + " 년 이하 ");
                    } else {
                        resum_level.setText(php_jsonObject.getString("level") + " 년");
                    }

                    resum_cf.setText(php_jsonObject.getString("cf"));
                    index = php_jsonObject.getString("index");
                    Log.e(TAG, "글번호" + index);

                    if ("1".equals(php_jsonObject.getString("wh"))) {
                        resum_wh.setText(ch1);
                    } else if ("2".equals(php_jsonObject.getString("wh"))) {
                        resum_wh.setText(ch2);
                    } else {
                        resum_wh.setText(ch3);
                    }

                    if ("1".equals(php_jsonObject.getString("mv"))) {
                        resum_mv.setText(ch1);
                    } else if ("2".equals(php_jsonObject.getString("mv"))) {
                        resum_mv.setText(ch2);
                    } else {
                        resum_mv.setText(ch3);
                    }
                    if ("1".equals(php_jsonObject.getString("ml"))) {
                        resum_ml.setText(ch1);
                    } else if ("2".equals(php_jsonObject.getString("ml"))) {
                        resum_ml.setText(ch2);
                    } else {
                        resum_ml.setText(ch3);
                    }

                    resum_df.setText(php_jsonObject.getString("df") + "개");
                    a = Integer.parseInt(php_jsonObject.getString("df"));
                    Log.e(TAG, "첨부서류" + a);
                    if (0 >= a) {
                        view_df.setVisibility(View.GONE);
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
        smpr.addStringParam("mode", "load_cs_resum");
        smpr.addStringParam("id", session.getId());


        //요청객체를 서버로 보낼 우체통 같은 객체 생성
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smpr);


        //취소
        resum_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: " + response);
                        session.setResum("N");
                        try {
                            for (int i = 0; i < sh_jsonArray.length(); i++) {
                                sh_jsonObject = sh_jsonArray.getJSONObject(i);
                                if(sh_jsonObject.getString("이메일").equals(session.getId())){
                                    sh_jsonObject.put("공고여부","N");
                                    sh_jsonArray.put(i,sh_jsonObject);
                                    Log.e(TAG, "onResponse: "+sh_jsonArray.toString() );
                                    SharedPreferences sph = getSharedPreferences("Members", 0);
                                    SharedPreferences.Editor editor1 = sph.edit();
                                    editor1.putString("Members", sh_jsonArray.toString());
                                    editor1.commit();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        msg.toast(getApplicationContext(),"접수취소 완료");

                        Intent intent = new Intent(getApplicationContext(), mypage_Activity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                smpr.addStringParam("mode", "delete");
                smpr.addStringParam("index", index);
                smpr.addStringParam("id", session.getId());

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);

            }
        });

        view_df.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), view_df_Activity.class);
                intent.putExtra("갯수", a);
                intent.putExtra("cs_id", cs_id);
                startActivity(intent);

            }
        });

    }
}