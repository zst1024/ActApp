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
import android.widget.TextView;
import android.widget.Toast;

public class CATActivity extends AppCompatActivity {

    private final int Return = 0;
    private ImageButton btn1, btn2, btn3;
    private RadioGroup radioGroup;
    private TextView tx1,tx2;
    private RadioButton rb0, rb1, rb2, rb3, rb4, rb5;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        ActivityManagerUtils.getInstance().addActivity(CATActivity.this);

        str = "";
        btn1 = (ImageButton) findViewById(R.id.CAT_back);
        btn2 = (ImageButton) findViewById(R.id.Before);
        btn3 = (ImageButton) findViewById(R.id.Next);
        tx1 = (TextView)findViewById(R.id.Question1);
        tx2 = (TextView)findViewById(R.id.Question2);
        radioGroup = (RadioGroup)findViewById(R.id.RadioGroup);
        rb0 = (RadioButton)findViewById(R.id.radioButton);
        rb1 = (RadioButton)findViewById(R.id.radioButton2);
        rb2 = (RadioButton)findViewById(R.id.radioButton3);
        rb3 = (RadioButton)findViewById(R.id.radioButton4);
        rb4 = (RadioButton)findViewById(R.id.radioButton5);
        rb5 = (RadioButton)findViewById(R.id.radioButton6);

        choose();
        show();
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
                    str = (String) rb0.getText();
                }
                else if(checkedId == rb1.getId()){
                    str = (String) rb1.getText();
                }
                else if(checkedId == rb2.getId()){
                    str = (String) rb2.getText();
                }
                else if(checkedId == rb3.getId()){
                    str = (String) rb3.getText();
                }
                else if(checkedId == rb4.getId()){
                    str = (String) rb4.getText();
                }
                else if(checkedId == rb5.getId()){
                    str = (String) rb5.getText();
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
                if(Data.getCount() == 0){
                    Toast.makeText(getApplicationContext(), "已经到第一题啦", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(CATActivity.this, CATActivity.class);
                    Data.setCount(Data.getCount()-1);
                    switch (Data.getCount()){
                        case 0:
                            Data.setCat1(null);
                            break;
                        case 1:
                            Data.setCat2(null);
                            break;
                        case 2:
                            Data.setCat3(null);
                            break;
                        case 3:
                            Data.setCat4(null);
                            break;
                        case 4:
                            Data.setCat5(null);
                            break;
                        case 5 :
                            Data.setCat6(null);
                            break;
                        case 6:
                            Data.setCat7(null);
                            break;
                    }
                    startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
                }
            }
        });
    }
    private void init2(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "请填写题目再点击",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Data.getCount() == 7) {
                        Data.setCat8(str);
                        Data.setCount(Data.getCount() + 1);
                        Intent intent = new Intent(CATActivity.this, MRCActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
                    } else {
                        Intent intent = new Intent(CATActivity.this, CATActivity.class);
                        switch (Data.getCount()) {
                            case 0:
                                Data.setCat1(str);
                                break;
                            case 1:
                                Data.setCat2(str);
                                break;
                            case 2:
                                Data.setCat3(str);
                                break;
                            case 3:
                                Data.setCat4(str);
                                break;
                            case 4:
                                Data.setCat5(str);
                                break;
                            case 5:
                                Data.setCat6(str);
                                break;
                            case 6:
                                Data.setCat7(str);
                                break;
                        }
                        Data.setCount(Data.getCount() + 1);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
                    }
                }
            }
        });
    }
    private void show(){
        switch (Data.getCount()){
            case 0:
                tx1.setText("我从不咳嗽");
                tx2.setText("我总是咳嗽");
                btn2.setClickable(false);
                break;
            case 1:
                tx1.setText("我肺里一点痰都没有");
                tx2.setText("我肺里有很多痰");
                break;
            case 2:
                tx1.setText("我一点也没有胸闷的感觉");
                tx2.setText("我有很重的胸闷的感觉");
                break;
            case 3:
                tx1.setText("当我在爬坡或爬一层楼梯时我并不感觉喘不过气来");
                tx2.setText("当我在爬坡或爬一层楼梯时我感觉非常喘不过气来");
                break;
            case 4:
                tx1.setText("我在家里的任何活动都不受慢阻肺的影响");
                tx2.setText("我在家里的任何活动都很受慢阻肺的影响");
                break;
            case 5:
                tx1.setText("尽管我有肺病我还是有信心外出");
                tx2.setText("因为我有肺病对于外出我完全没有信心");
                break;
            case 6:
                tx1.setText("我睡得好");
                tx2.setText("因为我有肺病我睡得不好");
                break;
            case 7:
                tx1.setText("我精力旺盛");
                tx2.setText("我一点精力也没有");
                break;
        }
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
                        Data.setCount(0);
                        Intent intent = new Intent(CATActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
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
        if(Data.getCount() == 0){
            Intent intent = new Intent(CATActivity.this, MainActivity.class);
            Data.setCat1(null);
            Data.setCount(0);
            startActivity(intent);
            ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
        }
        else{
            Data.setCount(Data.getCount()-1);
            switch (Data.getCount()){
                case 0:
                    Data.setCat1(null);
                    break;
                case 1:
                    Data.setCat2(null);
                    break;
                case 2:
                    Data.setCat3(null);
                    break;
                case 3:
                    Data.setCat4(null);
                    break;
                case 4:
                    Data.setCat5(null);
                    break;
                case 5 :
                    Data.setCat6(null);
                    break;
                case 6:
                    Data.setCat7(null);
                    break;
            }
            Intent intent = new Intent(CATActivity.this, CATActivity.class);
            startActivity(intent);
            ActivityManagerUtils.getInstance().finishActivityclass(CATActivity.class);
        }
        super.onBackPressed();
    }
}
