package com.example.administrator.project_v11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseURL extends AppCompatActivity {
    private ImageButton btn1;
    private TextView btn2;
    private RadioGroup radioGroup;
    private RadioButton rb0, rb1;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_url);
        ActivityManagerUtils.getInstance().addActivity(ChooseURL.this);
        radioGroup = (RadioGroup)findViewById(R.id.RadioGroup5);
        rb0 = (RadioButton)findViewById(R.id.radioButton20);
        rb1 = (RadioButton)findViewById(R.id.radioButton21);
        btn1 = (ImageButton) findViewById(R.id.URL_return);
        btn2 = (TextView) findViewById(R.id.URL_finish);
        str = "";
        choose();
        init();
    }
    private void choose(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                checkedId = group.getCheckedRadioButtonId();
                if(checkedId == rb0.getId()){
                    str = "http://192.168.1.210:8080/";
                }
                else if(checkedId == rb1.getId()){
                    str = "http://www.ibreathcare.cn/";
                }
            }
        });
    }
    private void init(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setUrl("http://www.ibreathcare.cn/");
                Intent i = new Intent(ChooseURL.this, SettingActivity.class);
                startActivity(i);
                ActivityManagerUtils.getInstance().finishActivity(ChooseURL.this);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!str.equals("")){
                    Toast.makeText(getApplicationContext(), "设置URL为：" + str,Toast.LENGTH_LONG).show();
                    Data.setUrl(str);
                    Intent i = new Intent(ChooseURL.this, SettingActivity.class);
                    startActivity(i);
                    ActivityManagerUtils.getInstance().finishActivity(ChooseURL.this);
                }
                else{
                    Toast.makeText(getApplicationContext(), "请选择URL",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onBackPressed() {
        Data.setUrl("http://www.ibreathcare.cn/");
        Intent intent = new Intent(ChooseURL.this, SettingActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(MessageActivity.class);
        super.onBackPressed();
    }
}
