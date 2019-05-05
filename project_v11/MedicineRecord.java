package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class MedicineRecord {
    private String mr_id;
    private String mc_id;
    private String medicine;
    private int state;

    public MedicineRecord(String mr_id, String mc_id, String medicine, int state) {
        this.mr_id = mr_id;
        this.mc_id = mc_id;
        this.medicine = medicine;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMr_id() {
        return mr_id;
    }

    public void setMr_id(String mr_id) {
        this.mr_id = mr_id;
    }

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
}
