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
        Log.e(TAG, "?????????????????????????????? " + Members);
        try {
            login_jsonArray=new JSONArray(Members);
            for (int i = 0; i < login_jsonArray.length(); i++){
                auto_login_jsonObject=login_jsonArray.getJSONObject(i);
                if(auto_login_jsonObject.getInt("???????????????")!=0){
                    Log.e(TAG, "????????????????????????" );
                    session.setId(auto_login_jsonObject.getString("?????????"));
                    session.setLevel(auto_login_jsonObject.getString("????????????"));
                    session.setResum(auto_login_jsonObject.getString("????????????"));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("tcp",1);
                    startActivity(intent);
                    Log.e(TAG, "?????????"+session.getId() );
                }else {
                    Log.e(TAG, "???????????????????????????" );
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
                    Log.e(TAG, "?????????????????????" );
                    auto=1;
                    auto_bt.setChecked(true);
                }else {
                    Log.e(TAG, "??????" );
                    auto=0;
                }

            }
        });

        //????????????
        find_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),findActivity.class);
                startActivity(intent);
            }
        });


        //?????????
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
                                            Log.e(TAG, "????????????");
                                            session.setId(login_id.getText().toString());
                                            session.setLevel("1");
                                            session.setResum(php_jsonObject.getString("resum"));
                                            auto_login_jsonObject.put("?????????",login_id.getText().toString());
                                            auto_login_jsonObject.put("???????????????",auto);
                                            auto_login_jsonObject.put("????????????","1");
                                            auto_login_jsonObject.put("????????????",php_jsonObject.getString("resum"));
                                            login_jsonArray.put(auto_login_jsonObject);
                                            SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Members", login_jsonArray.toString());
                                            editor.commit();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            intent.putExtra("tcp",1);
                                            startActivity(intent);

                                        }else if("2".equals(php_jsonObject.getString("level"))){
                                            Log.e(TAG, "?????????" );
                                            session.setId(login_id.getText().toString());
                                            session.setLevel("2");
                                            session.setResum(php_jsonObject.getString("resum"));
                                            auto_login_jsonObject.put("?????????",login_id.getText().toString());
                                            auto_login_jsonObject.put("???????????????",auto);
                                            auto_login_jsonObject.put("????????????","2");
                                            auto_login_jsonObject.put("????????????",php_jsonObject.getString("resum"));
                                            login_jsonArray.put(auto_login_jsonObject);
                                            SharedPreferences sharedPreferences = getSharedPreferences("Members", 0);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Members", login_jsonArray.toString());
                                            editor.commit();
                                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(intent);


                                        }else{
                                           msg.toast(getApplicationContext(),"???????????? ??????????????? ?????????????????????");
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

                //??????????????? ????????? ?????? ????????? ?????? ?????? ??????
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);



            }
        });
        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            //???????????? ?????????
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    //????????? ????????? ?????????

                }
                if (throwable != null) {
                    //??????????????????

                }
                updatekakaologinui();
                return null;
            }
        };


        //?????????
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


        //????????????
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
                    //??????????????????
                    Log.e(TAG, "invoke: id" + user.getId());
                    Log.e(TAG, "invoke: email-->" + Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                    Log.e(TAG, "invoke: nickname" + user.getKakaoAccount().getProfile().getNickname());
                    Log.e(TAG, "invoke: gender" + user.getKakaoAccount().getGender());
                    Log.e(TAG, "invoke: age" + user.getKakaoAccount().getAgeRange());

                    try {
                        //??????????????????
                        auto_login_jsonObject=new JSONObject();
                        login_jsonArray=new JSONArray();
                        auto_login_jsonObject.put("?????????",Objects.requireNonNull(user.getKakaoAccount()).getEmail());
                        auto_login_jsonObject.put("???????????????",1);
                        auto_login_jsonObject.put("????????????","1");

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
                    //????????????
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

