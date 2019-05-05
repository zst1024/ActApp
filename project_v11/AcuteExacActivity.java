package com.example.administrator.project_v11;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AcuteExacActivity extends AppCompatActivity {
    private String Cmid;
    private String Pid;
    private String C1;
    private String C2;
    private String C3;
    private String C4;
    private String C5;
    private String C6;
    private String C7;
    private String C8;
    private String Mrc;
    private String catSum;
    private String acuteExac;
    private String id;
    private final int Return = 0;
    private final int Finish = 1;
    private final int Updata = 2;
    private ImageButton btn1, btn2, btn3;
    private RadioGroup radioGroup;
    private RadioButton rb0, rb1;
    private String str;
    private DBManager mgr;
    private CAT_MRC cat_mrc;
    private static String TAG = "AcuteExacActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acute_exac);
        ActivityManagerUtils.getInstance().addActivity(AcuteExacActivity.this);
        mgr = new DBManager(this);
        str = "";
        btn1 = (ImageButton)findViewById(R.id.AppInfo_back);
        btn2 = (ImageButton)findViewById(R.id.AcuteExac_before);
        btn3 = (ImageButton)findViewById(R.id.AcuteExac_Next);
        radioGroup = (RadioGroup)findViewById(R.id.Acute_RG);
        rb0 = (RadioButton)findViewById(R.id.Acute_rb1);
        rb1 = (RadioButton)findViewById(R.id.Acute_rb2);
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
                    str = (String) rb0.getText();
                }
                else if(checkedId == rb1.getId()){
                    str = (String) rb1.getText();
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
                Intent intent = new Intent(AcuteExacActivity.this, MRCActivity.class);
                Data.setCount(Data.getCount()-1);
                Data.setAcuteExac(null);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(AcuteExacActivity.class);
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
                    Data.setAcuteExac(str);
                    Data.setCount(Data.getCount()+1);
                    if(Data.getI() == 1)
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
                        Data.setCat1(null);
                        Data.setCat2(null);
                        Data.setCat3(null);
                        Data.setCat4(null);
                        Data.setCat5(null);
                        Data.setCat6(null);
                        Data.setCat7(null);
                        Data.setCat8(null);
                        Data.setMrc(null);
                        Data.setAcuteExac(null);
                        Data.setCount(0);
                        backhome();
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
            case Finish:
                builder.setTitle("提示");
                builder.setMessage("所有题目均已完成，是否保存并上传?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data.setAcuteExac(str);
                        Data.setId(null);
                        insertdata();
                        Data.setCat1(null);
                        Data.setCat2(null);
                        Data.setCat3(null);
                        Data.setCat4(null);
                        Data.setCat5(null);
                        Data.setCat6(null);
                        Data.setCat7(null);
                        Data.setCat8(null);
                        Data.setMrc(null);
                        Data.setAcuteExac(null);
                        Data.setCount(0);
                        Data.setI(1);
                        upload();
                        backhome();
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
                builder.setMessage("您今天已经上传一次数据，请问是否要更新您的上传结果？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data.setAcuteExac(str);
                        List<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
                        cat_mrcs = mgr.queryToCat_Mrc(Data.getD());
                        Data.setId(cat_mrcs.get(cat_mrcs.size()-1).getId());
                        delete();
                        insertdata();
                        Data.setCat1(null);
                        Data.setCat2(null);
                        Data.setCat3(null);
                        Data.setCat4(null);
                        Data.setCat5(null);
                        Data.setCat6(null);
                        Data.setCat7(null);
                        Data.setCat8(null);
                        Data.setMrc(null);
                        Data.setAcuteExac(null);
                        Data.setCount(0);
                        upload();
                        backhome();
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

    private void insertdata(){
        long startTime = Data.getDate() + Data.getD() * 24 * 60 * 60 * 1000;
        int catsum = 0;
        ArrayList<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
        catsum =  Integer.parseInt(Data.getCat1())
                + Integer.parseInt(Data.getCat2())
                + Integer.parseInt(Data.getCat3())
                + Integer.parseInt(Data.getCat4())
                + Integer.parseInt(Data.getCat5())
                + Integer.parseInt(Data.getCat6())
                + Integer.parseInt(Data.getCat7())
                + Integer.parseInt(Data.getCat8()) ;
        cat_mrc = new CAT_MRC(Data.getP_id() + startTime, Data.getP_id(), startTime,
                Data.getCat1(), Data.getCat2(), Data.getCat3(), Data.getCat4(), Data.getCat5(), Data.getCat6(), Data.getCat7(), Data.getCat8(),
                catsum+"", Data.getMrc(), Data.getAcuteExac(), Data.getId(), Data.getUnUpload());
        cat_mrcs.add(cat_mrc);
        mgr.addCat_Mrc(cat_mrcs);
    }
    private void delete(){
        mgr.deleteTodayCAT_MRC(Data.getD());
    }

    private void upload() {
        if(!Data.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，已保存结果，稍后会自动上传", Toast.LENGTH_LONG).show();
            return;
        }
        List<CAT_MRC> cat_mrcs2 = new ArrayList<CAT_MRC>();
        cat_mrcs2 = mgr.queryToCat_Mrc(Data.getD());
        for (final CAT_MRC cat_mrc1 : cat_mrcs2) {
            Cmid = cat_mrc1.getCm_id();
            Pid = cat_mrc1.getP_id();
            C1 = cat_mrc1.getCat1();
            C2 = cat_mrc1.getCat2();
            C3 = cat_mrc1.getCat3();
            C4 = cat_mrc1.getCat4();
            C5 = cat_mrc1.getCat5();
            C6 = cat_mrc1.getCat6();
            C7 = cat_mrc1.getCat7();
            C8 = cat_mrc1.getCat8();
            Mrc = cat_mrc1.getMrc();
            catSum = cat_mrc1.getCatsum();
            acuteExac = cat_mrc1.getAcuteExac().equals("是") ? "1" : "0";
            id = cat_mrc1.getId();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Post请求的url，与get不同的是不需要带参数
                        URL postUrl = new URL(Data.getUrl() + "i45/");
                        //URL postUrl = new URL("http://www.ibreathcare.cn/i45/");
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

                        String content = "id=" + Data.getId() + "&P_id=" + Pid + "&date=" + Data.longToString(cat_mrc1.getDate(), "yyyy-MM-dd")  + "&cat1=" + C1 + "&cat2=" + C2 + "&cat3=" + C3 + "&cat4=" + C4 + "&cat5=" + C5 +"&cat6="+ C6 + "&cat7=" +C7 +"&cat8=" +C8 +"&catSum=" + catSum+"&mrc=" + Mrc + "&acuteExac=" + acuteExac;
                        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                        out.writeBytes(content);
                        //流用完记得关
                        out.flush();
                        out.close();
                        //获取响应
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            Log.i(TAG, line);
                            JSONObject jsonObject = new JSONObject(line);
                            Data.setId(jsonObject.getString("id"));
                            String a = jsonObject.getString("result");
                            if(a.equals("0")) {
                                delete();
                                ArrayList<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
                                cat_mrc.setId(Data.getId());
                                cat_mrc.setState(Data.getUpload());
                                cat_mrcs.add(cat_mrc);
                                mgr.addCat_Mrc(cat_mrcs);
                                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                                uploadInfos = mgr.queryToUploadInfo(Data.getD());
                                UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                                uploadInfos.clear();
                                mgr.deleteTodayUploadInfo(Data.getD());
                                uploadInfo.setCatMrc(Data.getDate() + 24*60*60*1000*Data.getD());
                                uploadInfos.add(uploadInfo);
                                mgr.addUploadInfo(uploadInfos);
                                //解析返回值，若上传成功，则更新本地上传表，否则不更新。
                            }
                        }
                        reader.close();
                        //该干的都干完了,记得把连接断了
                        connection.disconnect();
                    } catch (Exception e) {

                    }
                }
            }).start();

        }
    }

    public void onBackPressed() {
        Data.setAcuteExac(null);
        Data.setCount(Data.getCount()-1);
        Intent intent = new Intent(AcuteExacActivity.this, MRCActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(AcuteExacActivity.class);
        super.onBackPressed();
    }
    private void backhome(){
        Intent intent = new Intent(AcuteExacActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(AcuteExacActivity.class);
    }
}