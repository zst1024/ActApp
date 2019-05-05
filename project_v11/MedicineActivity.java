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

public class MedicineActivity extends AppCompatActivity{
    private List<MedicineRegular> medicineRegulars;
    private MedicineRegular medicineRegular ;
    private List<MedicineChange>  medicineChanges;
    private MedicineChange medicineChange ;
    private final int Return = 0;
    private final int Finish = 1;
    private final int Updata = 2;
    private final int Stop = 3;
    private ImageButton btn1;
    private TextView btn2;
    private RadioGroup radioGroup, radioGroup2;
    private RadioButton rb0, rb1, rb2, rb3, rb4;
    private String str, str2;
    private DBManager mgr;
    private double exposure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        ActivityManagerUtils.getInstance().addActivity(MedicineActivity.this);
        mgr = new DBManager(this);
        str = "";
        str2 = "";
        btn1 = (ImageButton) findViewById(R.id.Medicine_return);
        btn2 = (TextView) findViewById(R.id.Medicine_finish);
        radioGroup = (RadioGroup)findViewById(R.id.RadioGroup4);
        radioGroup2 = (RadioGroup)findViewById(R.id.RadioGroup5);
        rb0 = (RadioButton)findViewById(R.id.radioButton18);
        rb1 = (RadioButton)findViewById(R.id.radioButton19);
        rb2 = (RadioButton)findViewById(R.id.radioButton20);
        rb3 = (RadioButton)findViewById(R.id.radioButton21);
        rb4 = (RadioButton)findViewById(R.id.radioButton22);
        choose2();
        choose1();
        init();
        init2();
    }
    private void choose1(){
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
                else if(checkedId == rb4.getId()){
                    str = (String) rb4.getText();
                }
            }
        });
    }
    private void choose2(){
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                checkedId = group.getCheckedRadioButtonId();
                if(checkedId == rb2.getId()){
                    str2 = (String) rb2.getText();
                }
                else if(checkedId == rb3.getId()){
                    str2 = (String) rb3.getText();
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
                if(str.equals("") || str2.equals("")){
                    Toast.makeText(getApplicationContext(), "请将问题回答完整",Toast.LENGTH_SHORT).show();
                }
                else{
                    Data.setRegular(str2.equals("是")? "1" : "0");
                    Data.setChange(str.equals("继续用药") ? "0" : str.equals("更换用药") ? "1" : "2");
                    if(!str.equals("停止用药")) {
                        Data.setS(1);
                        if (Data.getN() == 1)
                            showDialog(Updata);
                        else
                            showDialog(Finish);
                    }
                    else{
                        Data.setS(3);
                        showDialog(Stop);
                    }
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
                        Data.setRegular(null);
                        Data.setChange(null);
                        Intent intent = new Intent(MedicineActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MedicineActivity.class);
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
                builder.setMessage("所有题目均已完成，是否确认填写并保存");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data.setR_id(null);
                        Data.setC_id(null);
                        insertdata();
                        upload();
                        Data.setRegular(null);
                        Data.setChange(null);
                        Data.setN(1);
                        if(!str.equals("继续用药")){
                            //getPm();
                            Toast.makeText(getApplicationContext(), "请点击上方的照相机拍摄药品",Toast.LENGTH_LONG).show();
                            camera();
                        }
                        else{
                            Intent intent = new Intent(MedicineActivity.this, MainActivity.class);
                            startActivity(intent);
                            ActivityManagerUtils.getInstance().finishActivityclass(MedicineActivity.class);
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
                if(str.equals("是"))
                    builder.setMessage("您今天已经填写过一次题目，请问是否要更新您的回答并弹出照相机?");
                else
                    builder.setMessage("您今天已经填写过一次题目，请问是否要更新您的回答并保存？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
                        medicineChanges = mgr.queryToMedicineChange(Data.getD());
                        Data.setC_id(medicineChanges.get(medicineChanges.size()-1).getId());
                        List<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
                        medicineRegulars = mgr.queryToMedicineRegular(Data.getD());
                        Data.setR_id(medicineRegulars.get(medicineRegulars.size()-1).getId());
                        delete();
                        insertdata();
                        upload();
                        Data.setRegular(null);
                        Data.setChange(null);
                        if(!str.equals("继续用药")){
                            Toast.makeText(getApplicationContext(), "请点击上方的照相机拍摄药品",Toast.LENGTH_LONG).show();
                            camera();
                        }
                        else{
                            Intent intent = new Intent(MedicineActivity.this,MainActivity.class);
                            startActivity(intent);
                            ActivityManagerUtils.getInstance().finishActivityclass(MedicineActivity.class);
                        }
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        */
                    }
                });
                dialog = builder.create();
                break;
            case Stop:
                builder.setTitle("提示");
                builder.setMessage("您确定已经停止用药？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Data.getN() == 1){
                            List<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
                            medicineChanges = mgr.queryToMedicineChange(Data.getD());
                            Data.setC_id(medicineChanges.get(medicineChanges.size()-1).getId());
                            List<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
                            medicineRegulars = mgr.queryToMedicineRegular(Data.getD());
                            Data.setR_id(medicineRegulars.get(medicineRegulars.size()-1).getId());
                            delete();
                            insertdata();
                            upload();
                            Data.setRegular(null);
                            Data.setChange(null);
                        }
                        else{
                            Data.setR_id(null);
                            Data.setC_id(null);
                            insertdata();
                            upload();
                            Data.setRegular(null);
                            Data.setChange(null);
                            Data.setN(1);
                        }
                        Toast.makeText(getApplicationContext(), "请点击上方的照相机拍摄停用的药品",Toast.LENGTH_LONG).show();
                        camera();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                        */
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
        medicineRegulars = new ArrayList<MedicineRegular>();
        medicineRegular = new MedicineRegular(Data.getP_id() + startTime, Data.getRegular(), Data.getP_id(), startTime, Data.getR_id(),Data.getUnUpload());
        medicineChanges = new ArrayList<MedicineChange>();
        medicineRegulars.add(medicineRegular);
        medicineChange = new MedicineChange(Data.getP_id() + startTime, Data.getChange(), Data.getP_id(), startTime, Data.getC_id(), Data.getUnUpload());
        medicineChanges.add(medicineChange);
        mgr.addMedicineRegular(medicineRegulars);
        mgr.addMedicineChange(medicineChanges);

    }
    private void delete(){
        mgr.deleteTodayMedicineRegular(Data.getD());
        mgr.deleteTodayMedicineChange(Data.getD());
    }
    public void onBackPressed() {
        Data.setType(null);
        Intent intent = new Intent(MedicineActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(MedicineActivity.class);
        super.onBackPressed();
    }
    private void upload(){
        if(!Data.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，已保存结果，稍后会自动上传", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数
                    URL postUrl = new URL(Data.getUrl() + "i52/");
                    //URL postUrl = new URL("http://www.ibreathcare.cn/i52/");
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

                    String content = "id=" + Data.getC_id() +"&MC_id=" + medicineChange.getMc_id() + "&change=" + medicineChange.getChange() + "&P_id=" + medicineChange.getP_id() + "&date=" + Data.longToString(medicineChange.getDate(), "yyyy-MM-dd") ;
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
                        Data.setC_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        Log.d("zst",line);
                        if(a.equals("0")) {
                            mgr.deleteTodayMedicineChange(Data.getD());
                            ArrayList<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
                            medicineChange.setId(Data.getC_id());
                            medicineChange.setState(Data.getUpload());
                            medicineChanges.add(medicineChange);
                            mgr.addMedicineChange(medicineChanges);
                            //解析返回值，若上传成功，则更新本地上传表，否则不更新。
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(Data.getD());
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(Data.getD());
                            uploadInfo.setMedicineCha(Data.getDate() + 24*60*60*1000*Data.getD());
                            uploadInfos.add(uploadInfo);
                            mgr.addUploadInfo(uploadInfos);
                        }
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                } catch (Exception e) {

                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数
                    URL postUrl = new URL(Data.getUrl() + "i48/");
                    // URL postUrl = new URL("http://www.ibreathcare.cn/i48/");
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

                    String content ="id="  + Data.getR_id() + "&regular=" + medicineRegular.getRegular() + "&P_id=" + medicineRegular.getP_id() + "&date=" + Data.longToString(medicineRegular.getDate(), "yyyy-MM-dd") ;
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
                        Data.setR_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        Log.d("zst",line);
                        if(a.equals("0")) {
                            mgr.deleteTodayMedicineRegular(Data.getD());
                            ArrayList<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
                            medicineRegular.setId(Data.getR_id());
                            medicineRegular.setState(Data.getUpload());
                            medicineRegulars.add(medicineRegular);
                            mgr.addMedicineRegular(medicineRegulars);
                            //解析返回值，若上传成功，则更新本地上传表，否则不更新。
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(Data.getD());
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(Data.getD());
                            uploadInfo.setMedicineReg(Data.getDate() + 24*60*60*1000*Data.getD());
                            uploadInfos.add(uploadInfo);
                            mgr.addUploadInfo(uploadInfos);
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
    private void camera(){
        Intent intent = new Intent(MedicineActivity.this, CameraActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("s",Data.getS());
        bundle.putString("name", "medicine");
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(MedicineActivity.class);
    }
}