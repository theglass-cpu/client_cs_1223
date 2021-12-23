package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RebootRecever extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, Tcp_connetion_server.class);
            context.startService(in);
        } else {
            Intent in = new Intent(context, Tcp_connetion_server.class);
            context.startService(in);
        }
    }
}
