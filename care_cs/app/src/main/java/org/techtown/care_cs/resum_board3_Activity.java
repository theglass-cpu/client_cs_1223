package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class resum_board3_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    String resum, document ;
    RadioGroup radioGroup, radioGroup2, radioGroup3;
    RadioButton radio_bt1, radio_bt2, radio_bt3;
    RadioButton radio_bt11, radio_bt12, radio_bt13;
    RadioButton radio_bt21, radio_bt22, radio_bt23;
    JSONObject resum_jsonObject, document_jsonObject,respone_jsonObject;
    JSONArray resum_jsonArray, document_jsonArray,respone_jsonArray;
    JSONObject sh_jsonObject;
    JSONArray sh_jsonArray;



    Button result_bt;
    int profile = 0;
    int df = 0;
    String document_count,Members ;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resum_board3);

        result_bt = findViewById(R.id.result_bt);

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        radioGroup3 = findViewById(R.id.radioGroup3);

        radio_bt1 = findViewById(R.id.radio_bt1);
        radio_bt2 = findViewById(R.id.radio_bt2);
        radio_bt3 = findViewById(R.id.radio_bt3);

        radio_bt11 = findViewById(R.id.radio_bt11);
        radio_bt12 = findViewById(R.id.radio_bt12);
        radio_bt13 = findViewById(R.id.radio_bt13);

        radio_bt21 = findViewById(R.id.radio_bt21);
        radio_bt22 = findViewById(R.id.radio_bt22);
        radio_bt23 = findViewById(R.id.radio_bt23);

        radioGroup.check(radio_bt1.getId());
        radioGroup2.check(radio_bt11.getId());
        radioGroup3.check(radio_bt21.getId());


        SharedPreferences onCreate_open0 = getSharedPreferences("Members", 0);
        Members = onCreate_open0.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);

        SharedPreferences onCreate_open = getSharedPreferences("resum", 0);
        resum = onCreate_open.getString("resum", "");
        Log.e(TAG, "이력서정보가져옴 " + resum);

        SharedPreferences open = getSharedPreferences("document", 0);
        document = open.getString("document", "");

        Log.e(TAG, "첨부서류가져옴 " + document);

        document_jsonArray = new JSONArray();
        document_jsonObject = new JSONObject();
        resum_jsonArray = new JSONArray();
        resum_jsonObject = new JSONObject();
        sh_jsonArray = new JSONArray();
        sh_jsonObject = new JSONObject();
        try {
            sh_jsonArray = new JSONArray(Members);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            resum_jsonObject = new JSONObject(resum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            resum_jsonObject.put("목욕", 1);
            resum_jsonObject.put("식사", 1);
            resum_jsonObject.put("이동", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_bt1:
                        try {
                            resum_jsonObject.put("목욕", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "목욕양치: 스스로");
                        break;

                    case R.id.radio_bt2:
                        Log.e(TAG, "목욕양치: 도움");
                        try {
                            resum_jsonObject.put("목욕", 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.radio_bt3:
                        Log.e(TAG, "목욕양치: 상관x");
                        try {
                            resum_jsonObject.put("목욕", 3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;


                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_bt11:
                        Log.e(TAG, "식사: 스스로");
                        try {
                            resum_jsonObject.put("식사", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case R.id.radio_bt12:
                        Log.e(TAG, "식사 도움");
                        try {
                            resum_jsonObject.put("식사", 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case R.id.radio_bt13:
                        Log.e(TAG, "식사 상관x");
                        try {
                            resum_jsonObject.put("식사", 3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;


                }
            }
        });


        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_bt21:
                        Log.e(TAG, "이동 스스로");
                        try {
                            resum_jsonObject.put("이동", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case R.id.radio_bt22:
                        Log.e(TAG, "이동 도움");
                        try {
                            resum_jsonObject.put("이동", 2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case R.id.radio_bt23:
                        Log.e(TAG, "이동 상관x");
                        try {
                            resum_jsonObject.put("이동", 3);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;


                }
            }
        });


        result_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "이력서" + resum_jsonObject.toString());
                try {
                    if(resum_jsonObject.getString("프로필사진").equals("")||resum_jsonObject.getString("프로필사진")==null){
                                profile=0;
                        Log.e(TAG, "프로필없음" );
                    }else {
                        profile=1;
                        Log.e(TAG, "프로필있음" );
                    }

                    if (document == null || document.equals("")) {
                        Log.e(TAG, "첨부서류없음");
                        df = 0;
                    } else {
                        df = 1;
                        Log.e(TAG, "서류있음");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e(TAG, "onResponse: " + response);
                        try {
                            respone_jsonObject=new JSONObject();
                            respone_jsonObject=new JSONObject(response);
                            boolean success = respone_jsonObject.getBoolean("success");
                            if(success){
                                      msg.toast(getApplicationContext(),"간병인 공고제출 완료");
                                      session.setResum("Y");

                                      try{
                                          for (int i = 0; i < sh_jsonArray.length(); i++) {
                                              sh_jsonObject = sh_jsonArray.getJSONObject(i);
                                              if(sh_jsonObject.getString("이메일").equals(session.getId())){
                                                  sh_jsonObject.put("공고여부","Y");
                                                  sh_jsonArray.put(i,sh_jsonObject);
                                                  Log.e(TAG, "onResponse: "+sh_jsonArray.toString() );
                                                  SharedPreferences sph = getSharedPreferences("Members", 0);
                                                  SharedPreferences.Editor editor1 = sph.edit();
                                                  editor1.putString("Members", sh_jsonArray.toString());
                                                  editor1.commit();
                                              }

                                          }

                                      }catch (Exception e){

                                      }


                                         Intent intent = new Intent(getApplicationContext(),mypage_Activity.class);
                                                                      startActivity(intent);
                            }else{
                                msg.toast(getApplicationContext(),"제출실패");
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
                try {
                    smpr.addStringParam("mode", "resum_up");
                    smpr.addStringParam("id", resum_jsonObject.getString("아이디"));
                    smpr.addStringParam("sx", resum_jsonObject.getString("성별"));
                    smpr.addStringParam("ppyn", String.valueOf(profile));
                    smpr.addStringParam("name", resum_jsonObject.getString("이름"));
                    smpr.addStringParam("age", resum_jsonObject.getString("나이"));
                    smpr.addStringParam("lo", resum_jsonObject.getString("지역"));
                    smpr.addStringParam("level", resum_jsonObject.getString("경력"));
                    smpr.addStringParam("cf", resum_jsonObject.getString("자격증"));
                    smpr.addStringParam("wh", resum_jsonObject.getString("목욕"));
                    smpr.addStringParam("ml", resum_jsonObject.getString("식사"));
                    smpr.addStringParam("mv", resum_jsonObject.getString("이동"));
                    smpr.addStringParam("df", String.valueOf(df));
                    if(profile==0){
                        smpr.addStringParam("profile", resum_jsonObject.getString("프로필사진"));
                    }else {

                        smpr.addFile("profile", resum_jsonObject.getString("프로필사진"));
                    }

                    if(df==0){
                        Log.e(TAG, "첨부서류없음" );
                    }else {
                        document_jsonArray = new JSONArray();
                        document_jsonArray = new JSONArray(document);
                        Log.e(TAG, "몇개: "+resum_jsonObject.getString("df_count"));
                        document_count=resum_jsonObject.getString("df_count");
                        smpr.addStringParam("df_count", document_count.toString());
                        int a = Integer.parseInt(document_count);
                      for (int i = 0; i < a; i++){
                          document_jsonObject=document_jsonArray.getJSONObject(0);
                          Log.e(TAG, "서륭: "+document_jsonObject.getString(i+"첨부서류"));
                          smpr.addFile("df"+i, document_jsonObject.getString(i+"첨부서류"));
                      }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);


                SharedPreferences pref = getSharedPreferences("resum", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("resum");
                editor.commit();

                SharedPreferences pref1 = getSharedPreferences("document", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = pref1.edit();
                editor1.remove("document");
                editor1.commit();
            }
        });


    }
}