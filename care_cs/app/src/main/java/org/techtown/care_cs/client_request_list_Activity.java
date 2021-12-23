package org.techtown.care_cs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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

public class client_request_list_Activity extends AppCompatActivity {

    private static final String TAG = "Cannot invoke method length() on null object";
    JSONArray matching_jsonArray;
    JSONObject matching_jsonObject;
    ArrayList<request_item> request_itemArrayList;
    RecyclerView matching_recy;
    static client_rquest_list_Adapter client_rquest_list_Adapter;
    LinearLayout no_resum;
    String user_id;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        matching_recy = findViewById(R.id.matching_recy);
        no_resum = findViewById(R.id.no_resum);
        matching_recy.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        matching_recy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        request_itemArrayList = new ArrayList<>();
        client_rquest_list_Adapter = new client_rquest_list_Adapter(getApplicationContext(), request_itemArrayList);
        matching_recy.setAdapter(client_rquest_list_Adapter);


        matching_jsonArray = new JSONArray();
        matching_jsonObject = new JSONObject();

        if (session.getResum().equals("N")) {

        } else {
            no_resum.setVisibility(View.GONE);
            Log.e(TAG, "onCreate: " + session.getResum());
            SimpleMultiPartRequest smpr = new SimpleMultiPartRequest(Request.Method.POST, url.resum, new Response.Listener<String>() {
                @SuppressLint({"LongLogTag", "NotifyDataSetChanged"})
                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        matching_jsonObject = new JSONObject(response);
                        matching_jsonArray = new JSONArray(matching_jsonObject.getString("response"));
                        for (int i = 0; i < matching_jsonArray.length(); i++) {
                            matching_jsonObject = matching_jsonArray.getJSONObject(i);
                            user_id=matching_jsonObject.getString("id");
                            request_itemArrayList.add(new request_item
                                    (matching_jsonObject.getString("id"),
                                            matching_jsonObject.getString("index"),
                                            matching_jsonObject.getString("write_date"),
                                            matching_jsonObject.getString("date")
                                    ));

                        }
                        client_rquest_list_Adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            //먼저...내가 신청한 간병인신청 지역과 상대방이 일치하는 지역으로 출력
            smpr.addStringParam("mode", "request_list");
            smpr.addStringParam("id", session.getId());
            smpr.addStringParam("user_id",user_id );


            //요청객체를 서버로 보낼 우체통 같은 객체 생성
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(smpr);


            client_rquest_list_Adapter.setOnitemClicklistenter(new client_rquest_list_Adapter.OnitemClicklistener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onitemClick(View v, int pos) {
                    request_item dict = request_itemArrayList.get(pos);
                    Log.e(TAG, "클릭함: " + dict.getCs_index());
                       Intent intent = new Intent(getApplicationContext(),client_request_detail_Activity.class);
                                                    intent.putExtra("layoutrespone","0");
                                                    intent.putExtra("index",dict.getCs_index());
                                                    startActivity(intent);
                }
            });
        }
    }
}