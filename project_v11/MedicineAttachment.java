package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class MedicineAttachment {
    private String ma_id;
    private String name;
    private String p_id;
    private String mc_id;
    private String dir;
    private int sign;
    private int state;

    public MedicineAttachment(String ma_id, String name, String p_id, String mc_id, String dir, int state, int sign) {
        this.ma_id = ma_id;
        this.name = name;
        this.p_id = p_id;
        this.mc_id = mc_id;
        this.dir = dir;
        this.state = state;
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public String getMa_id() {
        return ma_id;
    }

    public void setMa_id(String ma_id) {
        this.ma_id = ma_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
