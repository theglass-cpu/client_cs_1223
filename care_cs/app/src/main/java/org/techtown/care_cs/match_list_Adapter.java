package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class match_list_Adapter extends RecyclerView.Adapter<match_list_Adapter.CustomViewHolder> {

    private static final String TAG = "board_Adapter";
    static ArrayList<match_item> match_itemArrayList;
    static Context bContext;
    public static final int MSG_RESPONSE = 12;


    public void setArrayList(ArrayList<match_item> list, Context context) {
        this.match_itemArrayList = list;
        this.bContext = context;
    }


    public match_list_Adapter(Context context, ArrayList<match_item> list) {

        this.bContext = context;
        this.match_itemArrayList = list;
        return;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private match_list_Adapter.OnitemClicklistener mListener = null;

    public void setOnitemClicklistenter(match_list_Adapter.OnitemClicklistener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.match_item, viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();

        return viewHolder;
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        String nickname = match_itemArrayList.get(position).getCl_id();
        String[] array = nickname.split("@");
        viewHolder.client_id.setText(array[0] + "님 ");
    //    viewHolder.new_msg.setText();

//        session.getNew_msg();
//        String[]msg = session.getNew_msg().split("<");
//
//
//        if(match_itemArrayList.get(position).getRoom_index().equals(msg[0])){
//            Log.e(TAG, "onBindViewHolder: 방이같을때" );
//            if(msg[1].equals("0")){
//                Log.e(TAG, "새로온메세지는 0 개에요" );
//            }else{
//                Log.e(TAG, ""+msg[0]+"번방 새로운메세지 "+msg[1]+"개" );
//            }
//        }


        viewHolder.request_detail_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: "+match_itemArrayList.get(position).getCl_request_index());
                   Intent intent = new Intent(bContext,client_request_detail_Activity.class);
                                 intent.putExtra("index",match_itemArrayList.get(position).getCl_request_index());
                                 intent.putExtra("layoutrespone","1");
                                                bContext.startActivity(intent);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;
                if (pos != RecyclerView.NO_POSITION) {
                    if (mListener != null) {
                        mListener.onitemClick(v, pos);
                    }
                }
            }
        });




    }





    @Override
    public int getItemCount() {
        return match_itemArrayList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView client_id,new_msg;
        protected Button request_detail_bt;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.client_id = itemView.findViewById(R.id.client_id);
            this.request_detail_bt = itemView.findViewById(R.id.request_detail_bt);
         //   this.new_msg = itemView.findViewById(R.id.new_msg);
//            request_detail_bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e(TAG, "onClick: " +getA);
//                }
//            });
        }
    }
}
