package com.example.lab9_sqlite;

public class CongViec {
    private final int IdCV;
    private final String TenCV;

    public CongViec(int idCV, String tenCV) {
        IdCV = idCV;
        TenCV = tenCV;
    }

    public int getIdCV() {
        return IdCV;
    }

    public String getTenCV() {
        return TenCV;
    }

}