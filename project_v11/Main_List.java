package com.example.administrator.project_v11;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class Main_List {
    private String name;
    private int imageid;
    private int imageid2;

    public Main_List(String name, int imageid, int imageid2) {
        this.name = name;
        this.imageid = imageid;
        this.imageid2 = imageid2;
    }

    public static List<Main_List> getAllList() {
        List<Main_List> list = new ArrayList<Main_List>();
        if (Data.getV() != 0){
            list.add(new Main_List("前" + Data.getV() + "天问卷未完成",R.drawable.warnning1, R.drawable.touming));
        }
        //查本地表，确定用户前几天的填表情况，若存在未填或者为成功上传的表，则给出提醒
        if (Data.getI() == 1)
            list.add(new Main_List("健康自测", R.drawable.date, R.drawable.finish));
        else
            list.add(new Main_List("健康自测", R.drawable.date, R.drawable.unfinish));
        if (Data.getN() == 1)
            list.add(new Main_List("用药情况", R.drawable.medicine, R.drawable.finish));
        else
            list.add(new Main_List("用药情况", R.drawable.medicine, R.drawable.unfinish));
        if (Data.getM() == 1)
            list.add(new Main_List("昨日是否就医", R.drawable.doctor, R.drawable.finish));
        else
            list.add(new Main_List("昨日是否就医", R.drawable.doctor, R.drawable.unfinish));
        list.add(new Main_List("联系医生", R.drawable.message, R.drawable.touming));
        return list;
    }

    public String getName() {
        return this.name;
    }

    public int getImageid() {
        return this.imageid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public int getImageid2() {
        return this.imageid2;
    }

    public void setImageid2(int imageid2) {
        this.imageid2 = imageid2;
    }
}
