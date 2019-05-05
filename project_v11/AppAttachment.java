package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class AppAttachment {
    private String aa_id;
    private String name;
    private String p_id;
    private String ai_id;
    private String dir;
    private int state;

    public AppAttachment(String aa_id, String name, String p_id, String ai_id, String dir, int state) {
        this.aa_id = aa_id;
        this.name = name;
        this.p_id = p_id;
        this.ai_id = ai_id;
        this.dir = dir;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getAa_id() {
        return aa_id;
    }

    public void setAa_id(String aa_id) {
        this.aa_id = aa_id;
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

    public String getAi_id() {
        return ai_id;
    }

    public void setAi_id(String ai_id) {
        this.ai_id = ai_id;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
