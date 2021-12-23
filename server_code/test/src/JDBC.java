

import java.sql.*;
import java.time.LocalDate;

public class JDBC {

    Connection con;
    Statement stmt;
    ResultSet rs;

    String url = "jdbc:mysql://3.37.212.160:3306/care?allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&useSSL=false";
    String id = "server";
    String pw = "985621aA";

    String to_user;
    String from_user;
    String room_number;
    String msg;


    public JDBC() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, id, pw);

        stmt = con.createStatement();


    }

    public void math(String cs_id ,String cl_id ,String match ,String index ,String usre) throws SQLException {

        if(usre.equals("cs")) {
            System.out.println("간병인의 매칭 취소및 수락 ");
            String sql = "select * from care_cs_resum where cs_id = '" + cs_id + "' ";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String cs_name = rs.getString("cs_name");
                if (match.equals("0")) {
                    String cs_insert = "insert into matching_tb  ( cs_id , cl_id , cl_index , cs_name , cs_accept , request_status) "
                            + "VALUES ('" + cs_id + "', '" + cl_id + "','" + index + "','" + cs_name + "','1' ,'0')";
                    stmt.execute(cs_insert);

                } else {
                    String cs_insert = "insert into matching_tb  ( cs_id , cl_id , cl_index , cs_name , cs_accept , request_status) "
                            + "VALUES ('" + cs_id + "', '" + cl_id + "','" + index + "','" + cs_name + "','2' ,'0')";
                    stmt.execute(cs_insert);
                }

            }
        }else{
            System.out.println("보호자의 매칭 취소및 수락 ");
            String sql = "select * from care_cs_resum where cs_id = '" + cl_id + "' ";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            LocalDate todaysDate = LocalDate.now();
            while(rs.next()){
                String cs_index = rs.getString("cs_index");
                String cs_name = rs.getString("cs_name");
                if(match.equals("1")){
                    String cl_match = "UPDATE matching_tb SET cl_accept ='1' , cs_resum_index = '"+cs_index+"' WHERE cl_index = '"+index+"' ";
                    stmt.execute(cl_match);
                    String room_insert = "insert into chating_room  " +
                            "(  cs_id , cs_name  , cs_resum_index , cl_id , cl_request_index , room_date) "
                            + "VALUES ('" + cl_id + "', '" +cs_name  + "','" + cs_index + "','" + cs_id +  "','" + index + "' ,'"+todaysDate+"' )";
                    stmt.execute(room_insert);

                }else{
                    String cl_match = "UPDATE matching_tb SET cl_accept ='2' , cs_resum_index = '"+cs_index+"' WHERE cl_index = '"+index+"' ";
                    stmt.execute(cl_match);
                }
            }
        }

    }



    public void review(String room_number ) throws SQLException {

        con = DriverManager.getConnection(url, id, pw);

        stmt = con.createStatement();

        String sql = "UPDATE chating_room SET tb_status ='3' WHERE room_index = '"+room_number+"' ";

        stmt = con.createStatement();
        stmt.execute(sql);

        System.out.println("매칭취소 업데이트");
    }

    public void update_msg(String to_user ,String from_user ,String room_number ,String msg){
        try{
            //커넥션을 가져온다.
            con = DriverManager.getConnection(url, id, pw);

            stmt = con.createStatement();


            String sql = "INSERT INTO chating_msg (to_user, from_user, room_index, room_msg) "
                    + "VALUES ('" + to_user + "', '" + from_user + "','" + room_number + "','"+msg+"')";

            //     stmt = con.createStatement();
            stmt.execute(sql);

            System.out.println("채팅업데이트");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
