package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private ListviewAdapter adapter = null;

    private volatile int i=0;
    private volatile boolean tf = false;
    private ImageButton imageButton;
    private ListView listview;
    /*private Button btn_left;*/
    private EditText et_meg;
    private Button btn_right;
    private DBManager mgr;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.message);
        ActivityManagerUtils.getInstance().addActivity(this);
        mgr = new DBManager(this);
        initView();
        adapter = new ListviewAdapter(this);
        listview.setAdapter(adapter);
        final Intent intent = getIntent();
        if (null != intent) {
            /*Bundle bundle = getIntent().getExtras();
            type = bundle.getString("mark");
            if(type.equals("1")) {
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                Messagems message = new Messagems(Data.getTime(), content, 0);
                mgr.addMessage(message);
            }*/
            List<Messagems> messages = new ArrayList<>();
            messages = mgr.query_Message();
            for(Messagems message1 : messages){
                if(message1.getState() == 0){
                    try {
                        adapter.addDataToAdapter(new MsgInfo(message1.getMessage() + '\n' + Data.longToString(message1.getId(), "yyyy-MM-dd HH:mm:ss"), null));
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }
                else{
                    try {
                        adapter.addDataToAdapter(new MsgInfo(null, message1.getMessage() + '\n' + Data.longToString(message1.getId(), "yyyy-MM-dd HH:mm:ss")));
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }
                listview.smoothScrollToPosition(listview.getCount() - 1);
            }
        }
    }




    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        //btn_left = (Button) findViewById(R.id.btn_left);
        et_meg = (EditText) findViewById(R.id.et_meg);
        btn_right = (Button) findViewById(R.id.btn_right);

        imageButton = (ImageButton) findViewById(R.id.back);

        //btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(4);
            }
        });
    }

    @Override
    public void onClick(View v) {

        String msg = et_meg.getText().toString().trim();

        switch (v.getId()) {
            /*case R.id.btn_left:
                adapter.addDataToAdapter(new MsgInfo(msg,null));
                adapter.notifyDataSetChanged();
                break;*/
            case R.id.btn_right:
                if(!msg.equals(""))
                    showDialog(1);
                else{
                    Toast.makeText(getApplicationContext(),"留言不能为空，请重新填写", Toast.LENGTH_LONG).show();
                }
                break;
        }
        /*listview.smoothScrollToPosition(listview.getCount() - 1);
        et_meg.setText("");*/

    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 1:
                if(!Data.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，请检查网络设置并重新填写", Toast.LENGTH_LONG).show();
                    break;
                }
                uploadmessage();
                /*builder.setTitle("提示");
                builder.setMessage("是否确认发送留言？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Data.isNetworkAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，请检查网络设置并重新填写", Toast.LENGTH_LONG).show();
                            return;
                        }
                        uploadmessage();

                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = builder.create();
                */
                break;
            case 2:
                Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_LONG).show();
                Messagems message  = new Messagems(Data.getTime(), et_meg.getText().toString().trim(), 1);
                mgr.addMessage(message);
                try {
                    adapter.addDataToAdapter(new MsgInfo(null, et_meg.getText().toString().trim() + '\n' + Data.longToString(message.getId(), "yyyy-MM-dd HH:mm:ss")));
                    adapter.notifyDataSetChanged();
                }catch (Exception e){

                }
                et_meg.setText("");
                /*builder.setTitle("提示");
                builder.setMessage("发送成功");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Messagems message  = new Messagems(Data.getTime(), et_meg.getText().toString().trim(), 1);
                        mgr.addMessage(message);
                        try {
                            adapter.addDataToAdapter(new MsgInfo(null, et_meg.getText().toString().trim() + '\n' + Data.longToString(message.getId(), "yyyy-MM-dd HH:mm:ss")));
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){

                        }
                        et_meg.setText("");
                    }
                });
                dialog = builder.create();
                */
                break;
            case 3:
                builder.setTitle("提示");
                builder.setMessage("发送失败，请稍后再试");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        et_meg.setText("");
                    }
                });
                dialog = builder.create();
                break;
            case 4:
                builder.setTitle("提示");
                builder.setMessage("是否放弃编辑留言并返回主页？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MessageActivity.class);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog = builder.create();
                break;
            default:
                break;
        }
        return dialog;
    }

    private void uploadmessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数

                    String content = "test=" + "abc" + "&P_id=" + Data.getP_id() + "&date=" + Data.longToString(Data.getDate() + Data.getD()* 24*60*60*1000, "yyyy-MM-dd")  +"&content=" + et_meg.getText().toString();
                    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                    content = java.net.URLDecoder.decode(content,"utf-8");
                    URL postUrl = new URL(Data.getUrl() + "i55/");
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


                    out.writeUTF(content);

                    //流用完记得关
                    out.flush();

                    out.close();
                    /*i = connection.getResponseCode();
                    if(i == 200)
                        tf = true;
                    else
                        ;*/
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i("messageactivity",line);
                        JSONObject jsonObject = new JSONObject(line);
                        i = jsonObject.getInt("result");
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                    if(i == 0){
                        Message msg = mHandler.obtainMessage();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                    else{
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {

                }
            }
        }).start();

    }
    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2:
                    showDialog(2);
                    break;
                case 3:
                    showDialog(3);
                    break;
            }
        }
    };
    public void onBackPressed() {
        Intent intent = new Intent(MessageActivity.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(MessageActivity.class);
        super.onBackPressed();
    }

}
