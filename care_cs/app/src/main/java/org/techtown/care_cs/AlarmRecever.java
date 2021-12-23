package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmRecever extends BroadcastReceiver {
    private static final String TAG = "Cannot invoke method length() on null object";
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e(TAG, "RestartService로감: " );
            Intent in = new Intent(context, Tcp_connetion_server.class);
            context.startService(in);
        } else {
            Intent in = new Intent(context, Tcp_connetion_server.class);
            Log.e(TAG, "Tcp_connetion_server로감: " );
            context.startService(in);
        }
    }
}
