package org.techtown.care_cs;

import android.content.Context;
import android.widget.Toast;


public class msg {


    public static String toast(Context context , String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

        return msg;
    }
}


