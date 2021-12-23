package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.care_cs.MainActivity;
import org.techtown.care_cs.R;
import org.techtown.care_cs.msg;
import org.techtown.care_cs.session;
import org.techtown.care_cs.url;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class loginActivity extends AppCompatActivity {

    EditText login_id,login_pw;
    Button join_bt,find_bt,login_bt;
    ImageView login_kakao;
    CheckBox auto_bt;
    JSONObject login_jsonObject,auto_login_jsonObject;
    JSONObject php_jsonObject;
    JSONArray php_jsonArray;
    JSONArray login_jsonArray;
    String Members;
    int auto = 0;
    private static final String TAG = "Cannot invoke method length() on null object";



    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.slid,R.anim.slid);

        KakaoSdk.init(this, "7c510e8249b71fead3f19b4ff866a884");

        SharedPreferences onCreate_open = getSharedPreferences("Members", 0);
        Members = onCreate_open.getString("Members", "");
        Log.e(TAG, "자동로그인정보불러옴 " + Members);
        try {
            login_jsonArray=new JSONArray(Members);
            for (int i = 0; i < login_jsonArray.length(); i++){
                auto_login_jsonObject=login_jsonArray.getJSONObject(i);
                if(auto_login_jsonObject.getInt("자동로그인")!=0){
                    Log.e(TAG, "자동로그인설정함" );
                    session.setId(auto_login_jsonObject.getString("이메일"));
                    session.setLevel(auto_login_jsonObject.getString("회원레벨"));
                    session.setResum(auto_login_jsonObject.getString("공고여부"));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tcp",1);
                    startActivity(intent);
                    Log.e(TAG, "아이디"+session.getId() );
                }else {
                    Log.e(TAG, "자동로그인설정안함" );
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        login_id=findViewById(R.id.login_id);
        login_pw=findViewById(R.id.login_pw);
        join_bt=findViewById(R.id.join_bt);
        find_bt=findViewById(R.id.find_bt);
        login_bt=findViewById(R.id.login_bt);
        login_kakao=findViewById(R.id.login_kakao);
        auto_bt=findViewById(R.id.auto_bt);

        //   updatekakaologinui();
        auto_bt.setOnClickListener(new CheckBox.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(((CheckBox )v).isChecked()){
                    Log.e(TAG, "자동로그인켜짐" );
                    auto=1;
                    auto_bt.setChecked(true);
                }else {
                    Log.e(TAG, "꺼짐" );
                    auto=0;
                }

            }
        });

        //정보찾기
        find_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),findActivity.class);
                startActivity(intent);
            }
        });


        //로그인
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.join, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "onResponse: "+response );
                        try {
                            login_jsonArray=new JSONArray();
                            auto_login_jsonObject=new JSONObject();
                            php_jsonObject=new JSONObject();
                            php_jsonArray=new JSONArray();
                            php_jsonObject=new JSONObject(response);
                            php_jsonArray=new JSONArray(php_jsonObject.getString("response"));
                            Log.e(TAG, "onResponse: "+php_jsonArray.toString() );
                            for (int i = 0; i < php_jsonArray.length(); i++){
                                        php_jsonObject=php_jsonArray.getJSONObject(i);
                                        if("1".equals(php_jsonObject.getString("level"))){
                                            Log.e(TAG, "일반회원");
                                            session.setId(login_id.getText().toString());
                                            session.setLevel("1");
                                            session.setResum(php_jsonObject.getString("resum"));
                                            auto_login_jsonObject.put("이메일",login_id.getText().toString());
                                            auto_login_jsonObject.put("자동로그인",auto);
                                            auto_login_jsonObject.put("회원레벨","1");
                                            auto_login_jsonObject.put("공고여부",php_jsonObject.getString("resum"));
                                            login_jsonArray.put(auto_login_jsonObject);
                                            SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Members", login_jsonArray.toString());
                                            editor.commit();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            intent.putExtra("tcp",1);
                                            startActivity(intent);

                                        }else if("2".equals(php_jsonObject.getString("level"))){
                                            Log.e(TAG, "관리자" );
                                            session.setId(login_id.getText().toString());
                                            session.setLevel("2");
                                            session.setResum(php_jsonObject.getString("resum"));
                                            auto_login_jsonObject.put("이메일",login_id.getText().toString());
                                            auto_login_jsonObject.put("자동로그인",auto);
                                            auto_login_jsonObject.put("회원레벨","2");
                                            auto_login_jsonObject.put("공고여부",php_jsonObject.getString("resum"));
                                            login_jsonArray.put(auto_login_jsonObject);
                                            SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Members", login_jsonArray.toString());
                                            editor.commit();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);


                                        }else{
                                           msg.toast(getApplicationContext(),"아이디와 비밀번호를 확인하여주세요");
                                        }

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
                smpr.addStringParam("mode","login");
                smpr.addStringParam("id",login_id.getText().toString());
                smpr.addStringParam("pw",login_pw.getText().toString());

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);



            }
        });
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            //콜백으로 호출됨
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    //여기다 스타트 인텐트

                }
                if (throwable != null) {
                    //여기에토스트

                }
                updatekakaologinui();
                return null;
            }
        };


        //카카오
        login_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatekakaologinui();
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(loginActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoAccount(loginActivity.this,callback);
                }else {
                    UserApiClient.getInstance().loginWithKakaoAccount(loginActivity.this, callback);
                }

            }
        });


        //회원가입
        join_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),joinActivity.class);
                startActivity(intent);
            }
        });

    }

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void updatekakaologinui(){

        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @SuppressLint("LongLogTag")
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user!=null){
                    //로그인된상태
                    Log.e(TAG, "invoke: id" + user.getId());
                    Log.e(TAG, "invoke: email-->" + Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                    Log.e(TAG, "invoke: nickname" + user.getKakaoAccount().getProfile().getNickname());
                    Log.e(TAG, "invoke: gender" + user.getKakaoAccount().getGender());
                    Log.e(TAG, "invoke: age" + user.getKakaoAccount().getAgeRange());

                    try {
                        //이메일가져옴
                        auto_login_jsonObject=new JSONObject();
                        login_jsonArray=new JSONArray();
                        auto_login_jsonObject.put("이메일",Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                        auto_login_jsonObject.put("자동로그인",1);
                        auto_login_jsonObject.put("회원레벨","1");

                        login_jsonArray.put(auto_login_jsonObject);
                        SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Members", login_jsonArray.toString());
                        editor.commit();
                        session.setId(Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    //아닌상태
                }
                return null;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                updatekakaologinui();
                return null;
            }
        });

    }
}

