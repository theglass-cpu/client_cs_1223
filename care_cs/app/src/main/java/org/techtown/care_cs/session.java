package org.techtown.care_cs;

public class session {
    static  String id;
    static  String level;
    static  String resum;
    static  String cs_socket;
    static  String now_window = "0";
    static  String new_msg ="0<0";

    public static String getNow_window() {
        return now_window;
    }

    public static void setNow_window(String now_window) {
        session.now_window = now_window;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        session.id = id;
    }

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        session.level = level;
    }

    public static String getResum() {
        return resum;
    }

    public static void setResum(String resum) {
        session.resum = resum;
    }


    public static String getCs_socket() {
        return cs_socket;
    }

    public static void setCs_socket(String cs_socket) {
        session.cs_socket = cs_socket;
    }

    public static String getNew_msg() {
        return new_msg;
    }

    public static void setNew_msg(String new_msg) {
        session.new_msg = new_msg;
    }
}
