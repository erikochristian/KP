package com.example.offset.json.model;

/**
 * Created by OFFSET on 1/17/2017.
 */

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class Post {
    String NIK, NAMA, TGL_LHR, KELAMIN, AGAMA, BAGIAN, GOLJAMKER, UMR, SPSI, JAMSOS, DSU, BPJS, BERAS, DOMISILI;

    public String getAGAMA() {
        return AGAMA;
    }

    public void setAGAMA(String AGAMA) {
        this.AGAMA = AGAMA;
    }

    public String getGOLJAMKER() {
        return GOLJAMKER;
    }

    public void setGOLJAMKER(String GOLJAMKER) {
        this.GOLJAMKER = GOLJAMKER;
    }

    public String getUMR() {
        return UMR;
    }

    public void setUMR(String UMR) {
        this.UMR = UMR;
    }

    public String getSPSI() {
        return SPSI;
    }

    public void setSPSI(String SPSI) {
        this.SPSI = SPSI;
    }

    public String getJAMSOS() {
        return JAMSOS;
    }

    public void setJAMSOS(String JAMSOS) {
        this.JAMSOS = JAMSOS;
    }

    public String getDSU() {
        return DSU;
    }

    public void setDSU(String DSU) {
        this.DSU = DSU;
    }

    public String getBPJS() {
        return BPJS;
    }

    public void setBPJS(String BPJS) {
        this.BPJS = BPJS;
    }

    public String getBERAS() {
        return BERAS;
    }

    public void setBERAS(String BERAS) {
        this.BERAS = BERAS;
    }

    public String getBAGIAN() {
        return BAGIAN;
    }

    public void setBAGIAN(String BAGIAN) {
        this.BAGIAN = BAGIAN;
    }

    public String getNAMA() {
        return NAMA;
    }

    public void setNAMA(String NAMA) {
        this.NAMA = NAMA;
    }

    public String getTGL_LHR() {
        return TGL_LHR;
    }

    public void setTGL_LHR(String TGL_LHR) {
        this.TGL_LHR = TGL_LHR;
    }

    public String getKELAMIN() {
        return KELAMIN;
    }

    public void setKELAMIN(String KELAMIN) {
        this.KELAMIN = KELAMIN;
    }

    public String getDOMISILI() {
        return DOMISILI;
    }

    public void setDOMISILI(String DOMISILI) {
        this.DOMISILI = DOMISILI;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }
}