package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class Messagems {
    private long id;
    private String message;
    private int state;

    public Messagems(long id, String message, int state) {
        this.id = id;
        this.message = message;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
