package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class client_rquest_list_Adapter extends RecyclerView.Adapter<client_rquest_list_Adapter.CustomViewHolder> {

    private static final String TAG = "board_Adapter";
    static ArrayList<request_item> request_itemArrayList;
    static Context bContext;

    public void setArrayList(ArrayList<request_item> list, Context context) {
        this.request_itemArrayList = list;
        this.bContext = context;
    }


    public client_rquest_list_Adapter(Context context, ArrayList<request_item> list) {

        this.bContext = context;
        this.request_itemArrayList = list;
        return;
    }

    public interface OnitemClicklistener {
        void onitemClick(View v, int pos);
    }

    private OnitemClicklistener mListener = null;


    public void setOnitemClicklistenter(OnitemClicklistener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();


        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CustomViewHolder viewholder, @SuppressLint("RecyclerView") int position) {
        String nickname = request_itemArrayList.get(position).getCs_id();
        String[] array = nickname.split("@");
        viewholder.cs_id.setText(array[0] + "님 이 간병인을 요청 하셨습니다.");
        viewholder.cs_deadline.setText("게시일 : "+ request_itemArrayList.get(position).getWrite_date());
        viewholder.cs_date.setText("원하는 날 : " + request_itemArrayList.get(position).getDate());

        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;
                if(pos !=RecyclerView.NO_POSITION){
                    if (mListener != null) {
                        mListener.onitemClick(v, pos);
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {  return request_itemArrayList.size(); }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView cs_id;
        protected TextView cs_deadline;
        protected TextView cs_date;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cs_id = itemView.findViewById(R.id.cs_id);
            this.cs_deadline = itemView.findViewById(R.id.cs_deadline);
            this.cs_date = itemView.findViewById(R.id.cs_date);

        }
    }

}
