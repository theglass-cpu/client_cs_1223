

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class and_care_server_test {
    public static HashMap<String, Socket> socketHashMap;


    public static void tcp_server() throws IOException {
        ServerSocket s_socket = new ServerSocket(8888);
        System.out.println("사용자 기다리는중!!");
        while(true){
            Socket c_socket = s_socket.accept();
            System.out.println("사용자 들어옴");
            and_care_manger c_thread = new and_care_manger();
            c_thread.setSocket(c_socket);
            c_thread.start();

        }
    }

    public static void main(String[] args) throws IOException {
        socketHashMap = new HashMap<>();


        tcp_server();


    }
}
