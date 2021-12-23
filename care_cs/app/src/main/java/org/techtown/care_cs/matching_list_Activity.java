package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

public class matching_list_Activity extends AppCompatActivity {

    RecyclerView match_recy_view;
    JSONObject request_jsonObject;
    JSONArray request_jsonarray;
    static ArrayList<match_item> match_itemArrayList;
    @SuppressLint("StaticFieldLeak")
    static match_list_Adapter match_list_adapter;
    private static final String TAG = "Cannot invoke method length() on null object";
    ImageView list_back_space;


    public void layout(){
       match_recy_view=findViewById(R.id.match_recy_view);
        match_recy_view.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        match_recy_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        match_itemArrayList=new ArrayList<>();
        list_back_space=findViewById(R.id.list_back_space);
        match_list_adapter= new match_list_Adapter(getApplicationContext(),match_itemArrayList);
        match_recy_view.setAdapter(match_list_adapter);
        request_jsonarray=new JSONArray();
        request_jsonObject=new JSONObject();
    }

    public void volley_request(){

        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
        @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
        @Override
        public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
            try {
                request_jsonObject=new JSONObject(response);
                request_jsonarray=new JSONArray(request_jsonObject.getString("response"));
                for (int i = 0; i < request_jsonarray.length(); i++){
                    request_jsonObject=request_jsonarray.getJSONObject(i);
                          match_itemArrayList.add(new match_item(
                                  request_jsonObject.getString("cs_id"),
                                  request_jsonObject.getString("cs_name"),
                                  request_jsonObject.getString("cl_id"),
                                  request_jsonObject.getString("index"),
                                  request_jsonObject.getString("cs_resum_index"),
                                  request_jsonObject.getString("cl_request_index")
                          ));
                          match_list_adapter.notifyDataSetChanged();
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
                smpr.addStringParam("mode","match_list");
                smpr.addStringParam("id",session.getId());

                //요청객체를 서버로 보낼 우체통 같은 객체 생성
                RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(smpr);

    }

    public void click(){
        list_back_space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_list);
        layout();
        volley_request();
        click();


        match_list_adapter.setOnitemClicklistenter(new match_list_Adapter.OnitemClicklistener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onitemClick(View v, int pos) {
                match_item dict = match_itemArrayList.get(pos);
                  Intent intent = new Intent(getApplicationContext(),chat_room_Activity.class);
                  intent.putExtra("id",dict.getCl_id());
                  intent.putExtra("room_index",dict.getRoom_index());
                Log.e(TAG, "onitemClick: "+dict.getCs_name() );
                  intent.putExtra("name",dict.getCs_name());
                  intent.putExtra("renewal_screen","1");
                intent.putExtra("delete","0");
                                               startActivity(intent);

            }
        });

    }
}