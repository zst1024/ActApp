package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class MsgInfo {
    private String left_text;
    private String right_text;
    public MsgInfo(String left_text, String right_text) {
        this.left_text = left_text;
        this.right_text = right_text;
    }

    public String getLeft_text() {
        return left_text;
    }

    public void setLeft_text(String left_text) {
        this.left_text = left_text;
    }

    public String getRight_text() {
        return right_text;
    }

    public void setRight_text(String right_text) {
        this.right_text = right_text;
    }
}
