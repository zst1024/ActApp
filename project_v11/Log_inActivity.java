package com.example.administrator.project_v11;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jpushdemo.ExampleUtil;
import com.jpushdemo.Logger;

import java.io.DataOutputStream;
import java.util.ArrayList;

import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class Log_inActivity extends InstrumentedActivity {

    private EditText editText1, editText2;
    private Button button;
    private TextView imageButton, textView;
    private DBManager mgr;
    private int j;
    private static final int UserNameWrong = 0;
    private static final int PassWordWrong = 1;
    private static final int NetWrong = 2;
    private static final int Successful = 3;

    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_log_in);
        ActivityManagerUtils.getInstance().addActivity(Log_inActivity.this);
        mgr = new DBManager(this);
        button = (Button) findViewById(R.id.Log_button);
        imageButton = (TextView) findViewById(R.id.Log_back);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        textView = (TextView) findViewById(R.id.textView13);
        editText1.setHint("请输入用户名:");
        editText2.setHint("请输入密码:");
        j = -3;
        init();
    }
    private void init(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText1.getText().toString().equals("") && !editText2.getText().toString().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(!Data.isNetworkAvailable(getApplicationContext())) {
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = NetWrong;
                                    mHandler.sendMessage(msg);
                                    return;
                                }
                                // Post请求的url，与get不同的是不需要带参数
                                URL postUrl = new URL(Data.getUrl() + "i49/");
                                //URL postUrl = new URL("http://www.ibreathcare.cn/i49/");
                                // 打开连接
                                HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
                                // 设置是否向connection输出，因为这个是post请求，参数要放在
                                // http正文内，因此需要设为true
                                connection.setDoOutput(true);
                                // Read from the connection. Default is true.
                                connection.setDoInput(true);
                                // 设置连接超时时间
                                connection.setConnectTimeout(2 * 1000);
                                //设置从主机读取数据超时
                                connection.setReadTimeout(2 * 1000);
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

                                String content = "P_id=" + editText1.getText().toString() + "&password=" + editText2.getText().toString();
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
                                    j = jsonObject.getInt("result");
                                }
                                reader.close();
                                //该干的都干完了,记得把连接断了
                                connection.disconnect();
                                if (j == 0) {
                                    //setTag(editText1.getText().toString());
                                    setAlias(editText1.getText().toString());
                                    ArrayList<User> users = new ArrayList<User>();
                                    User user = new User(editText1.getText().toString(), Data.getDate());
                                    users.add(user);
                                    mgr.addUser(users);
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = Successful;
                                    mHandler.sendMessage(msg);
                                }
                                else if(j == -1){
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = UserNameWrong;
                                    mHandler.sendMessage(msg);
                                }
                                else if(j == -2){
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = PassWordWrong;
                                    mHandler.sendMessage(msg);
                                }
                                else{
                                    Message msg = mHandler.obtainMessage();
                                    msg.what = NetWrong;
                                    mHandler.sendMessage(msg);
                                }
                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }).start();
                }
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_inActivity.this, SettingActivity.class);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(Log_inActivity.class);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Log_inActivity.this, Sign_inActivity.class);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(Log_inActivity.class);
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(Log_inActivity.this, SettingActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(Log_inActivity.class);
        super.onBackPressed();
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case UserNameWrong:
                builder.setTitle("提示");
                builder.setMessage("用户名不存在，请重新输入");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText1.setText("");
                        editText2.setText("");
                        editText1.setHint("请输入用户名:");
                        editText2.setHint("请输入密码:");
                    }
                });
                /*builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*//*
                    }
                });*/
                dialog = builder.create();
                break;
            case PassWordWrong:
                builder.setTitle("提示");
                builder.setMessage("密码错误，请重新输入");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText1.setText("");
                        editText2.setText("");
                        editText1.setHint("请输入用户名:");
                        editText2.setHint("请输入密码:");
                    }
                });
               /* builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*//*
                    }
                });*/
                dialog = builder.create();
                break;
            case NetWrong:
                builder.setTitle("提示");
                builder.setMessage("网络出现异常，请检查您的网络设置");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                /*builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*//*
                    }
                });*/
                dialog = builder.create();
                break;
            case Successful:
                builder.setTitle("提示");
                builder.setMessage("登陆成功");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Log_inActivity.this, MainActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(Log_inActivity.class);
                    }
                });
                /*builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*//*
                    }
                });*/
                dialog = builder.create();
                break;
            default:
                break;
        }
        return dialog;
    }

    private static final String TAG = "Login";
    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case UserNameWrong:
                    showDialog(UserNameWrong);
                    break;
                // 定位完成
                case PassWordWrong:
                    showDialog(PassWordWrong);
                    break;
                //停止定位
                case NetWrong:
                    showDialog(NetWrong);
                    break;
                case MSG_SET_ALIAS:
                    Logger.d(TAG, "Set alias in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    Log.d(TAG,"Finish");
                    break;

                case MSG_SET_TAGS:
                    Logger.d(TAG, "Set tags in handler.");
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;
                default:
                    showDialog(Successful);
                    break;
            }
        }
    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set alias success";
                    Logger.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias due to timeout. Try again after 10s.";
                    Logger.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 10);
                    } else {
                        Logger.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Logger.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag success";
                    Logger.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set tags due to timeout. Try again after 10s.";
                    Logger.i(TAG, logs);
                    if (ExampleUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 10);
                    } else {
                        Logger.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Logger.e(TAG, logs);
            }

            ExampleUtil.showToast(logs, getApplicationContext());
        }

    };
    /*private void setTag(String string) {

        String tag = string.trim();

        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tag)) {
            //Toast.makeText(.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tag.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!ExampleUtil.isValidTagAndAlias(sTagItme)) {
                //Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));

    }*/

    private void setAlias(String string) {

        String alias = string.trim();
        if (TextUtils.isEmpty(alias)) {
            //Toast.makeText(Log_inActivity.this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            //Toast.makeText(Log_inActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }
}
