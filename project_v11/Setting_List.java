package com.example.administrator.project_v11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class Setting_List {
    private String name;
    private int imageid;
    public Setting_List(String name, int imageid){
        this.name = name;
        this.imageid = imageid;
    }
    public static List<Setting_List> getAllsetting(){
        List<Setting_List> setting = new ArrayList<Setting_List>();
        if(Data.getP_id() == null)
            setting.add(new Setting_List("登录/注册",R.drawable.patient));
        else
            setting.add(new Setting_List("用户: " + Data.getP_id(),R.drawable.patient));
        setting.add(new Setting_List("设置闹钟(用药提醒)",R.drawable.alarm));
        setting.add(new Setting_List("选择url",R.drawable.boy));
        return setting;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
