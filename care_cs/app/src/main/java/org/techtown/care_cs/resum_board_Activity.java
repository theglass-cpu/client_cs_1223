package org.techtown.care_cs;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class resum_board_Activity extends AppCompatActivity {

    Button resum_bord_2,resum_pt_bt;
    private static final String TAG = "Cannot invoke method length() on null object";
    private ActivityResultLauncher<Intent> resultLauncher;
    ImageView resum_profile;
    Uri uri;
    JSONObject resum_jsonObject;
    JSONArray resum_jsonArray;
    RadioGroup radioGroup;
    RadioButton radio_bt1,radio_bt2;
    EditText age_edit,name;
    ClipData clipData;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resum_board);

        resum_pt_bt=findViewById(R.id.resum_pt_bt);
        resum_bord_2=findViewById(R.id.resum_bord_2);
        resum_profile=findViewById(R.id.resum_profile);
        radioGroup =findViewById(R.id.radioGroup);
        radio_bt1=findViewById(R.id.radio_bt1);
        radio_bt2=findViewById(R.id.radio_bt2);
        age_edit=findViewById(R.id.age_edit);
        name=findViewById(R.id.name);
        radioGroup.check(radio_bt1.getId());
        resum_jsonObject= new JSONObject();
        resum_jsonArray =new JSONArray();

        try {
            resum_jsonObject.put("아이디",session.getId());
            resum_jsonObject.put("프로필사진","");
            resum_jsonObject.put("성별","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,10);
            }
        }else{
            //cv.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case  R.id.radio_bt1:
                        try {
                            Log.e(TAG, "onCheckedChanged: 남" );
                            resum_jsonObject.put("성별","1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case  R.id.radio_bt2:
                        Log.e(TAG, "onCheckedChanged: 여" );
                        try {
                            resum_jsonObject.put("성별","2");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });




        resum_pt_bt.setOnClickListener(new View.OnClickListener() {
     
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 사진첩클릭" );

                @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
           //  다중이미지하고싶을때   intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                resultLauncher.launch(intent);
            }
        });
        
        
        resum_bord_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString() == null || name.getText().toString().equals("")) {
                    msg.toast(getApplicationContext(), "이름 을 입력해주세요");
                } else {
                    if (age_edit.getText().toString() == null || age_edit.getText().toString().equals("")) {
                        msg.toast(getApplicationContext(), "생년월일 을 입력해주세요");
                    } else {
                        try {
                            resum_jsonObject.put("이름", name.getText().toString());
                            resum_jsonObject.put("나이", age_edit.getText().toString());
                         //   resum_jsonArray.put(resum_jsonObject);
                            SharedPreferences sharedPreferences = getSharedPreferences("resum", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("resum", resum_jsonObject.toString());
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), resum_board2_Activity.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "onClick: " + resum_jsonArray.toString());
                        Log.e(TAG, "전부작성");
                    }

                }
            }
        });



        //액티비티 콜백 함수
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.e(TAG, "사진첩 나와서 실행됨onActivityResult: " );
                            Intent i =result.getData();
                            assert i != null;
                            uri = i.getData();
                            clipData = i.getClipData();
                            resum_profile.setImageURI(uri);
                            try {
                                resum_jsonObject.put("프로필사진",getRealPathFromUri(uri));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });

    }





    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        assert cursor != null;
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 10 :
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) //사용자가 허가 했다면
                {
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();

                }else{//거부했다면
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}