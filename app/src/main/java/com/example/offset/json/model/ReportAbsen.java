package com.example.offset.json.model;

/**
 * Created by OFFSET on 2/14/2017.
 */

public class ReportAbsen {

    String Tanggal, Keterangan, JMSK, JKLR;

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String Keterangan) {
        this.Keterangan = Keterangan;
    }

    public String getTanggal() {
        return Tanggal;
    }

    public void setTanggal(String Tanggal) {
        this.Tanggal = Tanggal;
    }

    public String getJMSK() {
        return JMSK;
    }

    public void setJMSK(String JMSK) {
        this.JMSK = JMSK;
    }

    public String getJKLR() {
        return JKLR;
    }

    public void setJKLR(String JKLR) {
        this.JKLR = JKLR;
    }
}
