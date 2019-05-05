package com.example.administrator.project_v11;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MRCActivity extends AppCompatActivity {
    private final int Return = 0;
    private ImageButton btn1, btn2, btn3;
    private RadioGroup radioGroup;
    private RadioButton rb0, rb1, rb2, rb3, rb4;
    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrc);
        ActivityManagerUtils.getInstance().addActivity(MRCActivity.this);

        str = "";
        btn1 = (ImageButton) findViewById(R.id.MRC_back);
        btn2 = (ImageButton) findViewById(R.id.MRC_before);
        btn3 = (ImageButton) findViewById(R.id.MRC_next);
        radioGroup = (RadioGroup)findViewById(R.id.RadioGroup1);
        rb0 = (RadioButton)findViewById(R.id.radioButton7);
        rb1 = (RadioButton)findViewById(R.id.radioButton8);
        rb2 = (RadioButton)findViewById(R.id.radioButton9);
        rb3 = (RadioButton)findViewById(R.id.radioButton10);
        rb4 = (RadioButton)findViewById(R.id.radioButton11);

        choose();
        init();
        init1();
        init2();
    }
    private void choose(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                checkedId = group.getCheckedRadioButtonId();
                if(checkedId == rb0.getId()){
                    str = "0";
                }
                else if(checkedId == rb1.getId()){
                    str = "1";
                }
                else if(checkedId == rb2.getId()){
                    str = "2";
                }
                else if(checkedId == rb3.getId()){
                    str = "3";
                }
                else if(checkedId == rb4.getId()){
                    str = "4";
                }
            }
        });
    }

    private void init(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Return);
            }
        });
    }

    private void init1(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MRCActivity.this, CATActivity.class);
                Data.setCount(Data.getCount()-1);
                Data.setMrc(null);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(MRCActivity.class);
            }
        });
    }
    private void init2(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "请做出选择",Toast.LENGTH_SHORT).show();
                }
                else{
                    Data.setMrc(str);
                    Data.setCount(Data.getCount()+1);
                    Intent intent = new Intent(MRCActivity.this, AcuteExacActivity.class);
                    startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(MRCActivity.class);
                }
            }
        });
    }
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case Return:
                builder.setTitle("提示");
                builder.setMessage("是否放弃答题并返回主界面？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data.setCat1(null);
                        Data.setCat2(null);
                        Data.setCat3(null);
                        Data.setCat4(null);
                        Data.setCat5(null);
                        Data.setCat6(null);
                        Data.setCat7(null);
                        Data.setCat8(null);
                        Data.setMrc(null);
                        Data.setCount(0);
                        Intent intent = new Intent(MRCActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MRCActivity.class);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*/
                    }
                });
                dialog = builder.create();
                break;
        }
        return dialog;
    }
    public void onBackPressed() {
        Data.setMrc(null);
        Data.setCount(Data.getCount()-1);
        Intent intent = new Intent(MRCActivity.this, CATActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(MRCActivity.class);
        super.onBackPressed();
    }
}
