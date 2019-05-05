package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class PmExposure {
    private String pe_id;
    private String p_id;
    private Long date;
    private String exposure;

    public PmExposure(String pe_id, String p_id, Long date, String exposure) {
        this.pe_id = pe_id;
        this.p_id = p_id;
        this.date = date;
        this.exposure = exposure;
    }

    public String getPe_id() {
        return pe_id;
    }

    public void setPe_id(String pe_id) {
        this.pe_id = pe_id;
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

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }
}
