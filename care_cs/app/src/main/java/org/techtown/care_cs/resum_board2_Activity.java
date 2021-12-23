package org.techtown.care_cs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class resum_board2_Activity extends AppCompatActivity {

    Button resum_bord_3, lo_bt, cf;
    TextView lo, cf_count, title;
    String data, resum;
    String[] mobNum;
    private static final String TAG = "Cannot invoke method length() on null object";
    int code = 3;
    RadioGroup radioGroup2;
    RadioButton car_Box1, car_Box2, car_Box3, car_Box;
    CheckBox cate_Box1, cate_Box2, cate_Box3, cate_Box4, cate_Box_etc;
    EditText car_Box4_ed, cate_Box_etc_ed;
    int cate_etc;
    JSONObject resum_jsonObject;
    JSONArray resum_jsonArray;
    ClipData clipData;
    ArrayList<String> uriparse = new ArrayList<>();
    Uri uri;
    String st_card;
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private ActivityResultLauncher<Intent> resultLauncher;
    ArrayList<String> card = new ArrayList<>();
    JSONObject document_jsonObject;
    JSONArray document_jsonArray;
    CheckBox place_box;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resum_board2);


        SharedPreferences onCreate_open = getSharedPreferences("resum", 0);
        resum = onCreate_open.getString("resum", "");
        Log.e(TAG, "자동로그인정보불러옴 " + resum);

        cate_etc = 0;
        resum_jsonArray = new JSONArray();
        resum_jsonObject = new JSONObject();
        document_jsonObject = new JSONObject();
        document_jsonArray =new JSONArray();

        try {
            resum_jsonObject=new JSONObject(resum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        radioGroup2 = findViewById(R.id.radioGroup2);
        cf_count = findViewById(R.id.cf_count);
        car_Box1 = findViewById(R.id.car_Box1);
        car_Box2 = findViewById(R.id.car_Box2);
        car_Box3 = findViewById(R.id.car_Box3);
        car_Box = findViewById(R.id.car_Box);
        title = findViewById(R.id.title);
        place_box=findViewById(R.id.place_box);
        cate_Box1 = findViewById(R.id.cate_Box1);
        cate_Box2 = findViewById(R.id.cate_Box2);
        cate_Box3 = findViewById(R.id.cate_Box3);
        cate_Box4 = findViewById(R.id.cate_Box4);
        cate_Box_etc = findViewById(R.id.cate_Box_etc);
        cate_Box_etc_ed = findViewById(R.id.cate_Box_etc_ed);
        lo = findViewById(R.id.lo);
        lo_bt = findViewById(R.id.lo_bt);
        resum_bord_3 = findViewById(R.id.resum_bord_3);
        cf = findViewById(R.id.cf);
        radioGroup2.check(car_Box.getId());
        try {
            resum_jsonObject.put("경력", "01");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lo.setText("");
        cf_count.setText("");
        cate_Box_etc_ed.setText("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionResult = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 10);
            }
        } else {
            //cv.setVisibility(View.VISIBLE);
        }

        place_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    Log.e(TAG, "지역상관없음" );
                    lo.setText(place_box.getText().toString());
                    title.setVisibility(View.GONE);
                    place_box.setChecked(true);

                    try {
                        resum_jsonObject.put("지역","n");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                }
            }
        });


        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.car_Box1:
                        Log.e(TAG, "1년");
                        try {
                            resum_jsonObject.put("경력", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.car_Box2:
                        Log.e(TAG, "3년");
                        try {
                            resum_jsonObject.put("경력", "3");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.car_Box3:
                        Log.e(TAG, "5년");
                        try {
                            resum_jsonObject.put("경력", "5");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.car_Box:
                        Log.e(TAG, "1년미만");
                        try {
                            resum_jsonObject.put("경력", "01");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });


        cate_Box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "사회복지사 자격증있음");
                    Log.e(TAG, "onClick: " + cate_Box1.getText().toString());
                    card.add("|"+cate_Box1.getText().toString());

                } else {
                    Log.e(TAG, "취소");
                    card.remove("|"+cate_Box1.getText().toString()); //지울Obj를 직접 지정

                }

            }
        });

        cate_Box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "간호조무사 자격증있음");
                    Log.e(TAG, "onClick: " + cate_Box2.getText().toString());
                    card.add("|"+cate_Box2.getText().toString());

                } else {
                    Log.e(TAG, "취소");
                    card.remove("|"+cate_Box2.getText().toString()); //지울Obj를 직접 지정

                }

            }
        });

        cate_Box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "물리치료사 자격증있음");
                    Log.e(TAG, "onClick: " + cate_Box3.getText().toString());
                    card.add("|"+cate_Box3.getText().toString());

                } else {
                    Log.e(TAG, "취소");
                    card.remove("|"+cate_Box3.getText().toString()); //지울Obj를 직접 지정

                }

            }
        });

        cate_Box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "요양 자격증있음");
                    Log.e(TAG, "onClick: " + cate_Box4.getText().toString());
                    card.add("|"+cate_Box4.getText().toString());

                } else {
                    Log.e(TAG, "취소");
                    card.remove("|"+cate_Box4.getText().toString()); //지울Obj를 직접 지정

                }

            }
        });


        cate_Box_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Log.e(TAG, "기타 자격증있음");
                    Log.e(TAG, "onClick: " + cate_Box_etc.getText().toString());
                    cate_etc = 1;

                } else {
                    Log.e(TAG, "취소");
                    cate_etc = 0;
                }

            }
        });
        cf.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                setResult(-1, intent);
                resultLauncher.launch(intent);


            }
        });


        lo_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.i("주소설정페이지", "주소입력창 클릭");
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if (status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {

                    Log.i("주소설정페이지", "주소입력창 클릭");
                    Intent i = new Intent(getApplicationContext(), AddressApiActivity.class);
                    // 화면전환 애니메이션 없애기
                    overridePendingTransition(0, 0);
                    // 주소결과
                    resultLauncher.launch(i);

                } else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //액티비티 콜백 함수
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint({"LongLogTag", "SetTextI18n"})
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent i = result.getData();
                        if (result.getResultCode() == 5) {
                            place_box.toggle();

                            Bundle extras = i.getExtras();
                            data = extras.getString("data");
                            mobNum = data.split("\\|");
                            data = mobNum[1];
                            Log.e(TAG, "onActivityResu!?!!?!?!?lt: " + extras.getString("data"));
                            lo.setText(data);
                            title.setVisibility(View.GONE);

                            try {
                                resum_jsonObject.put("지역", data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if (result.getResultCode() == -1) {
                            // ArrayList<Uri> uri = new ArrayList<>();
                            uriparse = new ArrayList<>();
                            assert i != null;
                            clipData = i.getClipData();
                            cf_count.setText("첨부한서류 " + clipData.getItemCount() + " 개");
                            Log.e(TAG, "onActivityResult: " + clipData.getItemCount());
                            for (int k = 0; k < clipData.getItemCount(); k++) {
                                uriparse.add(getRealPathFromUri(clipData.getItemAt(k).getUri()));
                            }
                            for (int j = 0; j < uriparse.size(); j++){
                                try {
                                    document_jsonObject.put(j+"첨부서류",uriparse.get(j));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e(TAG, "첨부서류절대경로: "+uriparse.get(j));
                            }
                            try {
                                resum_jsonObject.put("df_count", uriparse.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            document_jsonArray.put(document_jsonObject);
                            Log.e(TAG, "첨부서류 파일나눴음: "+document_jsonArray.toString());
                            SharedPreferences sharedPreferences = getSharedPreferences("document", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("document", document_jsonArray.toString());
                            editor.commit();
                        }

                    }
                });

        resum_bord_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (lo.getText().toString() == null || lo.getText().toString().equals("")) {
                    msg.toast(getApplicationContext(), "장소를 선택하여주세요");
                } else {
                    if (cate_etc == 0) {
                        Log.e(TAG, "onClick: 기타자격증ㄴㄴ" );
                        cate_Box_etc_ed.setText("");
                        for (int i = 0; i < card.size(); i++) {
                            if(card.get(i).toString().contains("|기타-")){
                                Log.e(TAG, "onClick: "+card.get(i).toString() );
                                Log.e(TAG, "기타자격증해제했는데있음 이거 삭제해야함" );
                                card.remove(i);
                            }
                        }
                        st_card="";
                        for (int i = 0; i < card.size(); i++){
                            Log.e(TAG, "전처리끝낸거"+card.get(i));
                                st_card +=card.get(i);
                            Log.e(TAG, "자격증합한거: "+st_card);

                        }
                        try {
                           // resum_jsonArray=new JSONArray(resum);
                            resum_jsonObject.put("자격증",st_card);
                            Log.e(TAG, "onClick: "+resum_jsonObject.toString());
                         //   resum_jsonArray.put(resum_jsonObject);
                         //   Log.e(TAG, "onClick: "+resum_jsonArray.toString() );

                            SharedPreferences sharedPreferences = getSharedPreferences("resum", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("resum", resum_jsonObject.toString());
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), resum_board3_Activity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        if (cate_Box_etc_ed.getText().toString().equals("") || cate_Box_etc_ed.getText().toString() == null) {
                            msg.toast(getApplicationContext(), "기타항목을 입력하여주세요");
                        } else {
                            Log.e(TAG, "onClick: 기타장격증잇음" );
                            card.add("|기타-"+cate_Box_etc_ed.getText().toString());
                            st_card="";
                            for (int i = 0; i < card.size(); i++) {
                                Log.e(TAG, "!!!: " + card.get(i));
                                st_card +=card.get(i);
                                Log.e(TAG, "자격증합한거: "+st_card);
                            }
                            try {
                              //  resum_jsonArray=new JSONArray(resum);
                                resum_jsonObject.put("자격증",st_card);
                           //     resum_jsonArray.put(resum_jsonObject);
                          //      Log.e(TAG, "onClick: "+resum_jsonArray.toString() );
                                Log.e(TAG, "onClick: "+resum_jsonObject.toString());

                                SharedPreferences sharedPreferences = getSharedPreferences("resum", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("resum", resum_jsonObject.toString());
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), resum_board3_Activity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }

            }
        });


    }

    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}