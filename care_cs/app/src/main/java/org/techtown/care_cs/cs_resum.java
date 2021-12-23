package org.techtown.care_cs;

public class cs_resum {

    String email;
    String receipt;
    String lo;
    String index;

    public cs_resum(String email, String receipt, String lo, String index) {
        this.email = email;
        this.receipt = receipt;
        this.lo = lo;
        this.index = index;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
