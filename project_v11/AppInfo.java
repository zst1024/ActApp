package com.example.administrator.project_v11;

public class AppInfo {
    private String ai_id;
    private Long date;
    private String p_id;
    private int type;
    private String id;
    private int state;

    public AppInfo(String ai_id, Long date, String p_id, int type, String id, int state) {
        this.ai_id = ai_id;
        this.date = date;
        this.p_id = p_id;
        this.type = type;
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

    public String getAi_id() {
        return ai_id;
    }

    public void setAi_id(String ai_id) {
        this.ai_id = ai_id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
