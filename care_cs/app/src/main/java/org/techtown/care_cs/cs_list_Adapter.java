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

public class cs_list_Adapter extends RecyclerView.Adapter<cs_list_Adapter.CustomViewHolder> {


    private static final String TAG = "board_Adapter";
    static ArrayList<cs_resum> cs_resumArrayList;
    static Context bContext;



    public void setArrayList(ArrayList<cs_resum> list, Context context) {
        this.cs_resumArrayList = list;
        this.bContext = context;
    }


    public cs_list_Adapter(Context context, ArrayList<cs_resum> list) {

        this.bContext = context;
        this.cs_resumArrayList = list;
        return;
    }


    public interface OnitemClicklistener{
        void onitemClick(View v, int pos);
    }


    private OnitemClicklistener mListener=null;

    public void setOnitemClicklistenter(OnitemClicklistener listener)
    {
        this.mListener=listener;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cs_resum_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        bContext = viewGroup.getContext();


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, @SuppressLint("RecyclerView") int position) {
        viewholder.email.setText(cs_resumArrayList.get(position).getEmail());
        if("Y".equals(cs_resumArrayList.get(position).receipt)){
            viewholder.receipt.setText("처리완료");
        }
        if("n".equals(cs_resumArrayList.get(position).lo)){
            viewholder.lo.setText("지역상관없음");
        }else {
            viewholder.lo.setText(cs_resumArrayList.get(position).getLo());
        }



        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos =position;
                if(pos!=RecyclerView.NO_POSITION){
                    if(mListener!=null){
                        mListener.onitemClick(v,pos);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() { return cs_resumArrayList.size(); }


    public static class  CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView email;
        protected TextView receipt;
        protected TextView lo;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.email=itemView.findViewById(R.id.email);
            this.receipt=itemView.findViewById(R.id.receipt);
            this.lo=itemView.findViewById(R.id.lo);

        }
    }

}
