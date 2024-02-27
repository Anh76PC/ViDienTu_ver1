package com.example.do_an.model;

public class User {
    String id;
    String MaTTCN;
    long Soduvi;
    long Sodutuithantai;
    Boolean IsLocked;

    public User() {}

    public User(String id, String MaTTCN, long Soduvi, long Sodutuithantai, Boolean IsLocked) {
        this.id = id;
        this.MaTTCN = MaTTCN;
        this.Soduvi = Soduvi;
        this.Sodutuithantai = Sodutuithantai;
        this.IsLocked = IsLocked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaTTCN() {
        return MaTTCN;
    }

    public void setMaTTCN(String maTTCN) {
        MaTTCN = maTTCN;
    }

    public long getSoduvi() {
        return Soduvi;
    }

    public void setSoduvi(long soduvi) {
        Soduvi = soduvi;
    }

    public long getSodutuithantai() {
        return Sodutuithantai;
    }

    public void setSodutuithantai(long sodutuithantai) {
        Sodutuithantai = sodutuithantai;
    }

    public boolean isLocked() {
        return IsLocked;
    }

    public void setLocked(boolean locked) {
        IsLocked = locked;
    }
}
