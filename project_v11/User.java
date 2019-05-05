package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class User {
    private String P_id;
    private Long date;

    public User(String p_id, Long date) {
        P_id = p_id;
        this.date = date;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getP_id() {
        return P_id;
    }

    public void setP_id(String p_id) {
        P_id = p_id;
    }
}
