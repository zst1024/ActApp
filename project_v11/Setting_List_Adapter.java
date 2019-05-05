package com.example.administrator.project_v11;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class Setting_List_Adapter extends ArrayAdapter<Setting_List> {
    public Setting_List_Adapter(Context context, int resource, List<Setting_List> objects) {
        super(context, resource, objects);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final Setting_List setting_list = getItem(position);
        View listview = LayoutInflater.from(getContext()).inflate(R.layout.listview_1, parent, false);
        ImageView imageView = (ImageView) listview.findViewById(R.id.imageView);
        TextView textView = (TextView) listview.findViewById(R.id.Main_list);
        imageView.setImageResource(setting_list.getImageid());
        textView.setText(setting_list.getName());
        textView.setTextSize(20);
        textView.setText(setting_list.getName());
        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (setting_list.getName().equals("登录/注册")) {
                    intent.setClass(getContext(),Sign_inActivity.class);
                    getContext().startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(SettingActivity.class);

                } else if (setting_list.getName().equals("设置闹钟(用药提醒)")) {
                    getContext().startActivity(new Intent(AlarmClock.ACTION_SET_ALARM));
                    ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);

                } else if (setting_list.getName().equals("选择url")) {
                    intent.setClass(getContext(),ChooseURL.class);
                    getContext().startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(SettingActivity.class);
                }
            }
        });
        return listview;
    }
}