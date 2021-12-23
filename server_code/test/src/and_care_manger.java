
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class and_care_manger extends Thread {

    private Socket m_socket;

    //보낸사람
    private String user;

    //받을사람
    private String to_user;

    //메세지
    private String msg;


    //방번호
    private String room_number;


    JSONObject jsonObject;
    JSONArray jsonArray;


    @Override
    public void run() {
        jsonArray=new JSONArray();
        jsonObject=new JSONObject();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            user=bufferedReader.readLine();
            and_care_server_test.socketHashMap.put(user,m_socket);
            System.out.println("지정된소켓번호 출력"+and_care_server_test.socketHashMap.get(user));
            //클라이언트 id 랑 소켓 값담음
            String text;
            while(true){
                text = bufferedReader.readLine();
                System.out.println(text);
                JDBC jdbc = new JDBC();
                jsonArray=new JSONArray(text);
                for (int i = 0; i < jsonArray.length(); i++){
                    jsonObject=jsonArray.getJSONObject(i);

                    if(jsonObject.getString("delete").equals("0")) {
                        msg = jsonObject.getString("msg");
                        user = jsonObject.getString("from_user");
                        to_user = jsonObject.getString("to_user");
                        room_number = jsonObject.getString("room_number");

                        jdbc.update_msg(to_user, user, room_number, msg);
                    }
                    if(jsonObject.getString("delete").equals("1")){
                        user = jsonObject.getString("from_user");
                        to_user = jsonObject.getString("to_user");
                        room_number = jsonObject.getString("room_number");
                        System.out.println("매칭취소");
                        jdbc.review( room_number );
                    }if(jsonObject.getString("delete").equals("3")){

                        user = jsonObject.getString("from_user");
                        //간병인아이디
                        to_user=jsonObject.getString("to_user");
                        //보호자 ID
                        jdbc.math(user,to_user,jsonObject.getString("match"),jsonObject.getString("index"),jsonObject.getString("match_cs"));
                    }


                    PrintWriter printWriter = new PrintWriter(and_care_server_test.socketHashMap.get(to_user).getOutputStream());
                    printWriter.println(jsonArray.toString());
                    printWriter.flush();

                }


            }


        } catch (IOException | JSONException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        super.run();
    }

    public void setSocket(Socket m_socket) {
        this.m_socket = m_socket;
    }
}
