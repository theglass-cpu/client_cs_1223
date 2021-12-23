package org.techtown.care_cs;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tcp_connetion_server extends Service {

    private static final String TAG = "Cannot invoke method length() on null object";
    String id;
    PrintWriter sendWriter;
    static Socket c_socket;
  //  public static Intent serviceIntent = null;
    //jar 파일 실행명령어 java -jar test.jar

    private Thread mainThread;

    //메세지 받음
    public  class ReceiveThread extends Thread{
        public static final int MSG_RESPONSE = 12;
        private Socket c_socket;
        private String recive_msg;
        JSONArray jsonArray;
        JSONObject jsonObject;
        Context mContext;
        String from_usr;
        public static final String NOTIFICATION_CHANNEL_ID = "10001";
        private static final String TAG = "Cannot invoke method length() on null object";
        public  int count = 0;


        @SuppressLint("LongLogTag")
        @Override
        public void run() {
            super.run();
            try {
                BufferedReader tmpbuf = new BufferedReader(new InputStreamReader(c_socket.getInputStream()));
                jsonArray = new JSONArray();
                jsonObject = new JSONObject();


                while(true){

                    recive_msg = tmpbuf.readLine();
                    //여기서 채팅방으로 보내줄거야
                    Log.e(TAG, "간병인메세지받음: "+recive_msg );
                    if(recive_msg!=null){


                        chat_bind_service.recive(recive_msg);
                        jsonArray =new JSONArray(recive_msg);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("delete").equals("3")) {
                                if (jsonObject.getString("match").equals("1")) {
                                    Log.e(TAG, "(보호자코드 )간병인 이 보호자에게 수락신청");
                                    math_noti();
                                } else {
                                    Log.e(TAG, "(보호자코드 )간병인 이 보호자에게 거부신청");

                                }
                            } else {

                                from_usr = jsonObject.getString("from_user");
                                if (session.getNow_window().equals(jsonObject.getString("room_number"))) {
                                    Log.e(TAG, "현재방에있음");
                                } else {
                                    Log.e(TAG, "현재방에없음");
                                    NotificationSomethings("케어팜알림",
                                            jsonObject.getString("from_user"),
                                            jsonObject.getString("room_number"),
                                            jsonObject.getString("cs_nickname"),
                                            jsonObject.getString("delete"));

                                }

                                //NotificationSomethings("케어팜알림",jsonObject.getString("from_user"),jsonObject.getString("room_number"),jsonObject.getString("cs_nickname"));


                            }
                        }

                    }



                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }


        }

        public ReceiveThread(Context mcontext) {
            this.mContext = mcontext;
        }

        @SuppressLint("LongLogTag")
        public void NotificationSomethings(String title , String usr , String room_number , String name, String delete) {

                // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // 이동하려는 액티비티를 작성해준다.
                Intent notificationIntent = new Intent(mContext, chat_room_Activity.class);
                // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.

                notificationIntent.putExtra("room_index", room_number);
                notificationIntent.putExtra("id", usr);
                notificationIntent.putExtra("renewal_screen", "1");
                notificationIntent.putExtra("name", name);
                notificationIntent.putExtra("delete", delete);
                String[] array = usr.split("@");
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            if(delete.equals("1")){

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mes)) //BitMap 이미지 요구
                        .setContentTitle(title)
                        .setContentText(array[0] + "님 이 매칭을취소 ")

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                        .setAutoCancel(true); // 눌러야 꺼지는 설정

                //OREO API 26 이상에서는 채널 필요
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    builder.setSmallIcon(R.mipmap.ic_launcher); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    CharSequence channelName = "노티페케이션 채널";
                    String description = "오레오 이상";
                    int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                    channel.setDescription(description);

                    // 노티피케이션 채널을 시스템에 등록
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);

                } else
                    builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                assert notificationManager != null;
                notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작
            }
            else if("0".equals(delete)) {


                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mes)) //BitMap 이미지 요구
                        .setContentTitle(title)
                        .setContentText(array[0] + "님 에게서 채팅도착")

                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                        .setAutoCancel(true); // 눌러야 꺼지는 설정

                //OREO API 26 이상에서는 채널 필요
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    CharSequence channelName = "노티페케이션 채널";
                    String description = "오레오 이상";
                    int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
                    channel.setDescription(description);

                    // 노티피케이션 채널을 시스템에 등록
                    assert notificationManager != null;
                    notificationManager.createNotificationChannel(channel);

                } else
                    builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                assert notificationManager != null;
                notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작
            }
        }

        public void math_noti() {

            // 채널을 생성 및 전달해 줄수 있는 NotificationManager를 생성한다.
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            // 이동하려는 액티비티를 작성해준다.
            Intent notificationIntent = new Intent(mContext, matching_list_Activity.class);
            // 노티를 눌러서 이동시 전달할 값을 담는다. // 전달할 값을 notificationIntent에 담습니다.
            notificationIntent.putExtra("notificationId", count);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK) ;
            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, notificationIntent,  PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mes)) //BitMap 이미지 요구
                    .setContentTitle("케어팜알림 ")
                    .setContentText("보호자님과 매칭이되었어요 !")

                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                    .setAutoCancel(true); // 눌러야 꺼지는 설정

            //OREO API 26 이상에서는 채널 필요
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                builder.setSmallIcon(R.drawable.mes); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                CharSequence channelName  = "노티페케이션 채널";
                String description = "오레오 이상";
                int importance = NotificationManager.IMPORTANCE_HIGH;// 우선순위 설정

                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
                channel.setDescription(description);

                // 노티피케이션 채널을 시스템에 등록
                assert notificationManager != null;
                notificationManager.createNotificationChannel(channel);

            }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

            assert notificationManager != null;
            notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작

        }


        public Socket getC_socket() {
            return c_socket;
        }

        public void setC_socket(Socket c_socket) {
            this.c_socket = c_socket;
        }

        public String getRecive_msg() {
            return recive_msg;
        }

        public void setRecive_msg(String recive_msg) {
            this.recive_msg = recive_msg;
        }
    }


    public void Tcp_connetion() {
        new Thread(){
            @SuppressLint("LongLogTag")
            @Override
            public void run() {
                super.run();
                try {
                    Log.d(TAG, "소켓에 접속합니다." );
                    //서버소켓
                    c_socket  =new Socket("3.37.212.160",8888);

                   //로컬소켓
                  //  c_socket = new Socket("192.168.163.1",8080);
                    PrintWriter printWriter = new PrintWriter(c_socket.getOutputStream());
                    printWriter.println(session.getId());
                    printWriter.flush();

                    ReceiveThread receiveThread = new ReceiveThread(getApplicationContext());
                    receiveThread.setC_socket(c_socket);
                    receiveThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //  c_socket = new Socket("192.168.137.1",8080);

            }
        }.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("LongLogTag")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        id=session.getId();
        Log.e(TAG, "백그라운드 서비스가 시작됩니다."+id );
        Tcp_connetion();
       // serviceIntent = intent;
        Log.e(TAG, "onStartCommand: start_service" );

        return START_NOT_STICKY;


    //    return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "tcp server onDestroy: " );
      //  serviceIntent = null;



    }

    @SuppressLint("LongLogTag")
    protected void setAlarmTimer() {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, 1);
        Log.e(TAG, "setAlarmTimer: " );
        Intent intent = new Intent(getApplicationContext(), AlarmRecever.class);

        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0,intent,0);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = ReceiveThread.NOTIFICATION_CHANNEL_ID;//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.mes)) //BitMap 이미지 요구
                        .setContentTitle("Service test")
                        .setContentText(messageBody)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true) // 눌러야 꺼지는 설정
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


}