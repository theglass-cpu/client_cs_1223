package org.techtown.care_cs;

public class request_item {

    String cs_id;
    String cs_index;
    String write_date;
    String date;

    public request_item(String cs_id, String cs_index, String write_date, String date) {
        this.cs_id = cs_id;
        this.cs_index = cs_index;
        this.write_date = write_date;
        this.date = date;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCs_index() {
        return cs_index;
    }

    public void setCs_index(String cs_index) {
        this.cs_index = cs_index;
    }

    public String getWrite_date() {
        return write_date;
    }

    public void setWrite_date(String write_date) {
        this.write_date = write_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}