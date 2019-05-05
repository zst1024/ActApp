package com.example.administrator.project_v11;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class Main_list_Adapter extends ArrayAdapter<Main_List>{

    public Main_list_Adapter(Context context, int resource, List<Main_List> objects) {
        super(context, resource, objects);
    }


    public View getView(int position, View convertView, ViewGroup parent){
        final Main_List main_list;
        main_list= getItem(position);
        final View listview = LayoutInflater.from(getContext()).inflate(R.layout.listview_1, parent, false);
        final ImageView imageView = (ImageView) listview.findViewById(R.id.imageView);
        final ImageView imageView2 = (ImageView) listview.findViewById(R.id.imageView2);
        TextView textView = (TextView) listview.findViewById(R.id.Main_list);
        imageView.setImageResource(main_list.getImageid());
        imageView2.setImageResource(main_list.getImageid2());
        textView.setText(main_list.getName());
        if(main_list.getName().equals("前" + Data.getV() + "天表格未完成")){
            textView.setTextColor(Color.RED);
            textView.setTextSize(20);
            ColorDrawable drawable = new ColorDrawable(0x77FFE4C4);
            listview.setBackground(drawable);

        }
        listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getP_id() == null){
                    Toast.makeText(getContext(), "请登录后再点击", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    if (main_list.getName().equals("健康自测")) {
                        intent.setClass(getContext(), CATActivity.class);
                        getContext().startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    } else if (main_list.getName().equals("用药情况")) {
                        intent.setClass(getContext(), MedicineActivity.class);
                        getContext().startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    } else if (main_list.getName().equals("昨日是否就医")) {
                        intent.setClass(getContext(), AppInfoActivity.class);
                        getContext().startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    }
                    else if (main_list.getName().equals("联系医生")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("mark", "0");
                        intent.putExtras(bundle);
                        intent.setClass(getContext(), MessageActivity.class);
                        //intent.setClass(getContext(), SendMessage.class);
                        getContext().startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    }
                    else if (main_list.getName().equals("前" + Data.getV() + "天表格未完成")){
                        Data.setD(-Data.getV());
                        Data.setV(0);
                        intent.setClass(getContext(), MainActivity.class);
                        getContext().startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    }
                }
            }
        });
        return listview;
    }
}