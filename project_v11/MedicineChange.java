package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class MedicineChange {
    private String mc_id;
    private String change;
    private String p_id;
    private Long date;
    private String id;
    private int state;

    public MedicineChange(String mc_id, String change, String p_id, Long date, String id, int state) {
        this.mc_id = mc_id;
        this.change = change;
        this.p_id = p_id;
        this.date = date;
        this.id = id;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
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
}
