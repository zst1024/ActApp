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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AppInfoActivity extends AppCompatActivity {
    private final int Return = 0;
    private final int Finish = 1;
    private final int Updata = 2;
    private AppInfo appInfo;
    private ImageButton btn1;
    private TextView btn2;
    private RadioGroup radioGroup;
    private RadioButton rb0, rb1, rb2, rb3;
    private String str;
    private DBManager mgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ActivityManagerUtils.getInstance().addActivity(AppInfoActivity.this);
        mgr = new DBManager(this);
        str = "";
        btn1 = (ImageButton) findViewById(R.id.Appinfo_back);
        btn2 = (TextView) findViewById(R.id.Appinfo_finish);
        radioGroup = (RadioGroup)findViewById(R.id.Appinfo_RG);
        rb0 = (RadioButton)findViewById(R.id.Appinfo_rb1);
        rb1 = (RadioButton)findViewById(R.id.Appinfo_rb2);
        rb2 = (RadioButton)findViewById(R.id.Appinfo_rb3);
        rb3 = (RadioButton)findViewById(R.id.Appinfo_rb4);
        choose();
        init();
        init2();
    }

    private void choose(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                checkedId = group.getCheckedRadioButtonId();
                if(checkedId == rb0.getId()){
                    str = "-1";
                }
                else if(checkedId == rb1.getId()){
                    str = "0";
                }
                else if(checkedId == rb2.getId()){
                    str = "1";
                }
                else if(checkedId == rb3.getId()){
                    str = "2";
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
    private void init2(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "请填写题目在点击完成",Toast.LENGTH_SHORT).show();
                }
                else{
                    Data.setType(str);
                    if(Data.getM() == 1)
                        showDialog(Updata);
                    else
                        showDialog(Finish);
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
                        Data.setType(null);
                        backhome();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = builder.create();
                break;
            case Finish:
                builder.setTitle("提示");
                builder.setMessage("所有题目均已完成，是否保存并上传?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data.setM(1);
                        Data.setT_id(null);
                        if(Data.getType().equals("-1")){
                            insertdata(Data.getUpload());
                            Data.setType(null);
                            backhome();
                        }
                        else{
                            upload();
                            Data.setType(null);
                            Toast.makeText(getApplicationContext(), "请拍照您的病历",Toast.LENGTH_LONG).show();
                            camera();
                        }
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
            case Updata:
                builder.setTitle("提示");
                builder.setMessage("您今天已经上传一次数据，请问上传新的就诊记录？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = 0;
                        List<AppInfo> appInfo2 = mgr.queryToAppInfo(Data.getD());
                        for(AppInfo appInfo4 : appInfo2) {
                            if (appInfo4.getType() == Integer.parseInt(Data.getType())) {
                                Data.setT_id(appInfo4.getId());
                                delete(appInfo4.getType());
                                i = 1;
                                break;
                            }
                        }
                        if(i == 0){
                            Data.setT_id(null);
                        }
                        if(Data.getType().equals("-1")){
                            insertdata(Data.getUpload());
                            Data.setType(null);
                            backhome();
                        }
                        else{
                            upload();
                            Data.setType(null);
                            Toast.makeText(getApplicationContext(), "请点击下方的照相机拍摄您的病历",Toast.LENGTH_LONG).show();
                            camera();
                        }
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
            default:
                break;
        }
        return dialog;
    }
    private void insertdata(int ifok){
        long startTime = Data.getDate() + Data.getD() * 24 * 60 * 60 * 1000;
        int i = 0;
        try {
            i = Integer.parseInt(Data.getType());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        appInfo = new AppInfo(Data.getP_id() + startTime + Data.getType(), startTime, Data.getP_id(), i, Data.getT_id(), ifok);
        appInfos.add(appInfo);
        mgr.addAppinfo(appInfos);
    }

    private void upload(){
        insertdata(Data.getUnUpload());
        if(!Data.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，已保存结果，稍后会自动上传", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数
                    URL postUrl = new URL(Data.getUrl() + "i47/");
                    //URL postUrl = new URL("http://www.ibreathcare.cn/i47/");
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
                    // 设置是否向connection输出，因为这个是post请求，参数要放在
                    // http正文内，因此需要设为true
                    connection.setDoOutput(true);
                    // 设置连接超时时间
                    connection.setConnectTimeout(2 * 1000);
                    //设置从主机读取数据超时
                    connection.setReadTimeout(2 * 1000);
                    // Read from the connection. Default is true.
                    connection.setDoInput(true);
                    // 默认是 GET方式
                    connection.setRequestMethod("POST");
                    // Post 请求不能使用缓存
                    connection.setUseCaches(false);
                    //设置本次连接是否自动重定向
                    connection.setInstanceFollowRedirects(true);
                    // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
                    // 意思是正文是urlencoded编码过的form参数
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
                    // 要注意的是connection.getOutputStream会隐含的进行connect。
                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection
                            .getOutputStream());
                    // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致

                    String content ="id=" + Data.getT_id() + "&AI_id=" + appInfo.getAi_id() + "&date=" + Data.longToString(appInfo.getDate(), "yyyy-MM-dd") + "&P_id=" + appInfo.getP_id() +"" + "&type=" + appInfo.getType();
                    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                    out.writeBytes(content);
                    //流用完记得关
                    out.flush();
                    out.close();
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        JSONObject jsonObject = new JSONObject(line);
                        Data.setT_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        if (a.equals("0")) {
                            mgr.deleteTodayAppinfo(Data.getD(), Integer.parseInt(str));
                            ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
                            appInfo.setId(Data.getT_id());
                            appInfo.setState(Data.getUpload());
                            appInfos.add(appInfo);
                            mgr.addAppinfo(appInfos);
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(Data.getD());
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(Data.getD());
                            uploadInfo.setAppInfo(Data.getDate() + 24*60*60*1000*Data.getD());
                            uploadInfos.add(uploadInfo);
                            mgr.addUploadInfo(uploadInfos);
                        }
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                }catch(Exception e){

                }
            } }).start();
    }

    private void delete(int type){
        mgr.deleteTodayAppinfo(Data.getD(), type);
        //mgr.deleteTodayAppAttachment(Data.getD());
    }
    public void onBackPressed() {
        Data.setType(null);
        backhome();
        super.onBackPressed();
    }
    private void camera(){
        Intent intent = new Intent(AppInfoActivity.this, CameraActivity.class);
        Bundle bundle=new Bundle();
        //传递name参数为appinfo
        bundle.putString("name", "appinfo");
        Data.setS(0);
        bundle.putInt("s",Data.getS());
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(AppInfoActivity.class);
    }
    private void backhome(){
        Intent intent = new Intent(AppInfoActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(AppInfoActivity.class);
    }

}
