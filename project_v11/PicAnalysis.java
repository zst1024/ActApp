package com.example.administrator.project_v11;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class PicAnalysis extends AppCompatActivity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private TextView mTextView ;
    private ImageView mImageView;
    private DBManager mgr;
    private ImageButton button, button2, mButton;
    private MedicineRecord medicineRecord = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_analysis);
        ActivityManagerUtils.getInstance().addActivity(PicAnalysis.this);
        mgr = new DBManager(this);
        mTextView = (TextView) findViewById(R.id.result);
        mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
        button = (ImageButton) findViewById(R.id.button);
        button2 = (ImageButton) findViewById(R.id.button2);
        mButton = (ImageButton) findViewById(R.id.Start);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PicAnalysis.this, MedicineActivity.class);
                startActivity(intent);
                ActivityManagerUtils.getInstance().finishActivityclass(PicAnalysis.class);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(medicineRecord == null){
                    Toast.makeText(getApplicationContext(), "还未完成扫码", Toast.LENGTH_SHORT).show();
                }
                else {
                    medicineRecord = null;
                    Intent intent = new Intent(PicAnalysis.this, MainActivity.class);
                    startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(PicAnalysis.class);
                }
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PicAnalysis.this, PicTaking.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){
                    Bundle bundle = data.getExtras();
                    mTextView.setText(bundle.getString("result"));
                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                    Date now = new Date();
                    GregorianCalendar cal = new GregorianCalendar();
                    cal.setTime(now);
                    long startTime = cal.getTimeInMillis();
                    List<MedicineChange> medicineChanges= new ArrayList<MedicineChange>();
                    medicineChanges = mgr.queryToMedicineChange(Data.getD());
                    ArrayList<MedicineRecord> medicineRecords = new ArrayList<MedicineRecord>();
                    medicineRecord = new MedicineRecord(Data.getP_id() + startTime, medicineChanges.get(medicineChanges.size() - 1).getMc_id(), bundle.getString("result"), Data.getUnUpload());
                    medicineRecords.add(medicineRecord);
                    mgr.addMedicineRecord(medicineRecords);
                    mTextView.append(" " + startTime);
                    upload();
                }
                break;
        }
    }
    private void upload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数
                  //  URL postUrl = new URL("http://192.168.0.121:8001/i53/");
                    URL postUrl = new URL("http://www.ibreathcare.cn/i53/");
                    // 打开连接
                    HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
                    // 设置是否向connection输出，因为这个是post请求，参数要放在
                    // http正文内，因此需要设为true
                    connection.setDoOutput(true);
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

                    String content ="&MC_id=" + medicineRecord.getMc_id() + "&medicine=" + medicineRecord.getMedicine();
                    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                    out.writeBytes(content);
                    //流用完记得关
                    out.flush();
                    out.close();
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        mgr.deleteTodayMedicineRecord(Data.getD());
                        medicineRecord.setState(Data.getUpload());
                        List<MedicineRecord> medicineRecords = new ArrayList<MedicineRecord>();
                        medicineRecords.add(medicineRecord);
                        mgr.addMedicineRecord(medicineRecords);
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                } catch (Exception e) {

                }
            }
        }).start();
    }
    public void onBackPressed() {
        Intent intent = new Intent(PicAnalysis.this, MedicineActivity.class);
        startActivity(intent);
        ActivityManagerUtils.getInstance().finishActivityclass(PicAnalysis.class);
        super.onBackPressed();
    }
}
