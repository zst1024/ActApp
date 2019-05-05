package com.example.administrator.project_v11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity {
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imageButton = (ImageButton)findViewById(R.id.back);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(SettingActivity.class);
            }
        });
        ActivityManagerUtils.getInstance().addActivity(SettingActivity.this);
        Setting_List_Adapter setting_list_adapter = new Setting_List_Adapter(this, R.layout. listview_1, Setting_List.getAllsetting());
        ListView listView = (ListView) findViewById(R.id.listview2);
        //设置listView的Adapter
        listView.setAdapter(setting_list_adapter);
    }
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(SettingActivity.class);
        super.onBackPressed();
    }
}
