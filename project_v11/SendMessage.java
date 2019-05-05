package com.example.administrator.project_v11;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class SendMessage extends AppCompatActivity implements OnClickListener {


    private EditText editText1;
    private ImageButton  back;
    private TextView send;
    private volatile int i=0;
    private volatile boolean k = false,tf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        ActivityManagerUtils.getInstance().addActivity(SendMessage.this);
        send = (TextView) findViewById(R.id.textView6);
        back = (ImageButton) findViewById(R.id.back);
        editText1 = (EditText) findViewById(R.id.editText);
        init();
        editText1.setHint("请输入你想联系医生的话");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(SendMessage.this, MainActivity.class);
                //实现跳转
                startActivity(i);
                ActivityManagerUtils.getInstance().finishActivityclass(SendMessage.class);
            }
        });


    }

    private void init(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Data.isNetworkAvailable(getApplicationContext())) {
                    Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，请检查网络设置并重新填写", Toast.LENGTH_LONG).show();
                    return;
                }
                uploadmessage();
                if(tf)
                    Toast.makeText(getApplicationContext(), "发送成功",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "发送失败",Toast.LENGTH_LONG).show();
                        /*if(flag==1) {
                            uploadaudio();
                        }
                        else{
                            uploadmessage();
                        }
                        flag=0;*/
                Intent i = new Intent(SendMessage.this, SendMessage.class);
                //实现跳转
                startActivity(i);
                ActivityManagerUtils.getInstance().finishActivityclass(SendMessage.class);
                //showDialog(1);
            }
        });
    }

    /*protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (id) {
            case 1:
                builder.setTitle("提示");
                builder.setMessage("是否确认发送留言？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Data.isNetworkAvailable(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，请检查网络设置并重新填写", Toast.LENGTH_LONG).show();
                            return;
                        }
                        uploadmessage();
                        while(true){
                            if(k)
                                break;
                        }
                        if(tf)
                            Toast.makeText(getApplicationContext(), "发送成功",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "发送失败",Toast.LENGTH_LONG).show();
                        /*if(flag==1) {
                            uploadaudio();
                        }
                        else{
                            uploadmessage();
                        }
                        flag=0;*/
    /*
                        Intent i = new Intent(SendMessage.this, SendMessage.class);
                        //实现跳转
                        startActivity(i);
                        ActivityManagerUtils.getInstance().finishActivityclass(SendMessage.class);
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
    */

    public void onClick(View v) {
        //switch (v.getId()) {
        //   case R.id.audio: //调用Android自带的音频录制应用
        //      Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        //     startActivityForResult(intent, 1);
        // }
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    private void uploadmessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数

                    String content = "test=" + "abc" + "&P_id=" + Data.getP_id() + "&date=" + Data.longToString(Data.getDate() + Data.getD()* 24*60*60*1000, "yyyy-MM-dd")  +"&content=" + editText1.getText().toString();
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
                    i = connection.getResponseCode();
                    if(i == 200)
                        tf = true;
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                    k = true;
                } catch (Exception e) {
                }
            }
        }).start();

    }
    public void onBackPressed() {
        Intent intent = new Intent(SendMessage.this, MainActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(SendMessage.class);
        super.onBackPressed();
    }
}





