package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class MedicineRegular {
    private String mr_id;
    private String regular;
    private String p_id;
    private Long date;
    private String id;
    private int state;

    public MedicineRegular(String mr_id, String regular, String p_id, Long date, String id, int state) {
        this.mr_id = mr_id;
        this.regular = regular;
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

    public String getMr_id() {
        return mr_id;
    }

    public void setMr_id(String mr_id) {
        this.mr_id = mr_id;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
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
