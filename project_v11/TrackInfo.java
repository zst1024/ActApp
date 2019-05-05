package com.example.administrator.project_v11;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class TrackInfo {
    private String ti_id;
    private String p_id;
    private Long date;
    private String name;
    private String dir;
    private int state;

    public TrackInfo(String ti_id, String p_id, Long date, String name, String dir, int state) {
        this.ti_id = ti_id;
        this.p_id = p_id;
        this.date = date;
        this.name = name;
        this.dir = dir;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTi_id() {
        return ti_id;
    }

    public void setTi_id(String ti_id) {
        this.ti_id = ti_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
