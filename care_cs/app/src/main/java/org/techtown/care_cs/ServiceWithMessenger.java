package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;

public class ServiceWithMessenger extends Service {

    private ArrayList<Messenger> clientList = new ArrayList<>();

    static final int MSG_REGISTER_CLIENT = 44;      // 클라이언트 추가
    static final int MSG_UNREGISTER_CLIENT = 56;    // 클라이언트 제거
    static final int MSG_SET_VALUE = 77;            //
    static final int SEND = 11;
    private static final String TAG = "Cannot invoke method length() on null object";
    private String send = null;

    //클라이언트에 보내는 Message의 what에 해당하는 값
    public static final int MSG_RESPONSE = 12;

    int mValue = 0;

    class IncomingHandler extends Handler {



        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage( Message msg ){

            switch( msg.what ){
                case MSG_REGISTER_CLIENT:           // 클라이언트 추가
                    clientList.add( msg.replyTo );
                    break;

                case MSG_UNREGISTER_CLIENT:         // 클라이언트 제거
                    clientList.remove( msg.replyTo );
                    break;

                case SEND:
                    Bundle bundle = msg.getData();
                    send = bundle.getString("send");
                    Log.e(TAG, "ServiceWithMessenger/send : "+bundle.getString("send"));
                    receive(send);

                    break;

                default:
                    super.handleMessage( msg );
            }

        }
    }


    public void receive(String send2){

        new Thread(new Runnable() {
            @Override
            public void run() {

                // while(true) 를통해 자바 소켓에서 데이터를 받아오는 역할을 할 수도 있다.
                if(!send2.equals(null)) {
                    for (int i = clientList.size() - 1; i >= 0; i--) {
                        try {

                            // 클라이언트에게 전송
                            Message message = Message.obtain(null, MSG_RESPONSE);
                            Bundle bundle = message.getData();
                            bundle.putString("response", send2);
                            clientList.get(i).send(message);

                        } catch (RemoteException e) {
                            clientList.remove(i);
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();

    }


    private Messenger messenger = new Messenger(new IncomingHandler());

    public void onCreate(){
        super.onCreate();

    }

    public ServiceWithMessenger() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
