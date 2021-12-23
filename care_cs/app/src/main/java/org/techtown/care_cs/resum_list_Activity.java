package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class resum_list_Activity extends AppCompatActivity {

    Button resum_up_bt;
    private static final String TAG = "Cannot invoke method length() on null object";
    RecyclerView cs_resum_recyclerView;
    private ArrayList<cs_resum> cs_resumArrayList;
    static cs_list_Adapter cs_list_adapter;
    JSONArray resum_list_jsonArray;
    JSONObject resum_list_jsonObject;
    int a =0;




    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resum_list);


        cs_resum_recyclerView=findViewById(R.id.cs_resum_recyclerView);
        cs_resum_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        cs_resumArrayList=new ArrayList<>();
        cs_list_adapter = new cs_list_Adapter(getApplicationContext(),cs_resumArrayList);
        cs_resum_recyclerView.setAdapter(cs_list_adapter);



        resum_list_jsonArray=new JSONArray();
        resum_list_jsonObject=new JSONObject();


        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
            try {
                resum_list_jsonObject=new JSONObject(response);
                resum_list_jsonArray=new JSONArray(resum_list_jsonObject.getString("response"));
                for (int i = 0; i < resum_list_jsonArray.length(); i++){
                    resum_list_jsonObject=resum_list_jsonArray.getJSONObject(i);
                    a=1;
                    cs_resumArrayList.add(new cs_resum
                              (resum_list_jsonObject.getString("id").trim(),
                              resum_list_jsonObject.getString("receipt").trim(),
                               resum_list_jsonObject.getString("lo").trim(),
                               resum_list_jsonObject.getString("index").trim()));

                }
                cs_list_adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
                }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

                }
                });
                smpr.addStringParam("mode","load_list");
                smpr.addStringParam("id",session.getId());

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);


             cs_list_adapter.setOnitemClicklistenter(new cs_list_Adapter.OnitemClicklistener() {
                 @SuppressLint("LongLogTag")
                 @Override
                 public void onitemClick(View v, int pos) {
                     cs_resum dict =cs_resumArrayList.get(pos);
                     Log.e(TAG, "클릭함"+dict.getIndex());
                     Intent intent = new Intent(getApplicationContext(),admin_resum_view_Activity.class);
                     intent.putExtra("index",dict.getIndex());
                     startActivity(intent);




                 }
             });


        Log.e(TAG, "엥: "+a);




    }
}