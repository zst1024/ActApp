package com.example.administrator.project_v11;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class CameraActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "HelloCamera";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static String name;
    private String type;
    private int t;
    private ImageButton takePicBtn = null;

    private ImageView imageView = null;
    private TextView textView = null;
    private Uri fileUri;
    private ImageButton Take_Photo;
    private ImageButton back;
    private TextView sure;

    private DBManager mgr;
    private ArrayList<AppAttachment> appAttachments ;
    private AppAttachment appAttachment;

    private ArrayList<MedicineAttachment> medicineAttachments ;
    private MedicineAttachment medicineAttachment;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ActivityManagerUtils.getInstance().addActivity(CameraActivity.this);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView2);
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        type = bundle.getString("name");
        t = bundle.getInt("s");
        if(t == 0){
            textView.setText("请拍摄您的病历:");
        } else if (t == 1) {
            textView.setText("请拍摄换药前的药品:");
        } else if (t == 2) {
            textView.setText("请拍摄换药后的药品:");
        } else {
            textView.setText("请拍摄停药前的药品");
        }

        mgr = new DBManager(this);
        Take_Photo = (ImageButton) findViewById(R.id.Cmrbutton);
        Take_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, 1);
                }catch (Throwable e){

                }
            }
        });
        back = (ImageButton) findViewById(R.id.camera_2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(type.equals("appinfo")) {
                    Intent intent = new Intent(CameraActivity.this, AppInfoActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(CameraActivity.this, MedicineActivity.class);
                    startActivity(intent);
                }
                Data.setS(0);
                //实现跳转
                ActivityManagerUtils.getInstance().finishActivityclass(CameraActivity.class);
            }
        });
        sure = (TextView) findViewById(R.id.camera_1);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(appAttachment == null && medicineAttachment == null){
                    Toast.makeText(getApplicationContext(), "还未完成拍照", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(t == 0 || t == 2 || t == 3) {
                        Intent i = new Intent(CameraActivity.this, MainActivity.class);
                        //实现跳转
                        startActivity(i);
                        ActivityManagerUtils.getInstance().finishActivityclass(CameraActivity.class);
                        Data.setS(0);
                    }
                    else{
                        Intent i = new Intent(CameraActivity.this, CameraActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name", type);
                        Data.setS(2);
                        bundle.putInt("s",Data.getS());
                        i.putExtras(bundle);
                        //实现跳转
                        startActivity(i);
                        ActivityManagerUtils.getInstance().finishActivityclass(CameraActivity.class);
                    }
                    ActivityManagerUtils.getInstance().finishActivityclass(CameraActivity.class);
                }
            }
        });
        takePicBtn = (ImageButton) findViewById(R.id.Cmrbutton);
        takePicBtn.setOnClickListener(takePiClickListener);


    }
    public void onBackPressed() {
        if(type.equals("appinfo")) {
            Intent intent = new Intent(CameraActivity.this, AppInfoActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(CameraActivity.this, MedicineActivity.class);
            startActivity(intent);
        }
        ActivityManagerUtils.getInstance().finishActivityclass(CameraActivity.class);
        super.onBackPressed();
    }
    private final OnClickListener takePiClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Log.d(LOG_TAG, "Take Picture Button Click");
            // 利用系统自带的相机应用:拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // create a file to save the image
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            Date now = new Date();
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(now);
            long startTime = cal.getTimeInMillis();
            if(type.equals("appinfo")) {
                List<AppInfo> appInfos = new ArrayList<AppInfo>();
                appInfos = mgr.queryToAppInfo(Data.getD());
                appAttachments = new ArrayList<AppAttachment>();
                String filestr = fileUri.toString();
                filestr = filestr.substring(7, filestr.length());
                appAttachment = new AppAttachment(Data.getP_id() + startTime, name, Data.getP_id(), appInfos.get(appInfos.size() - 1).getAi_id(), filestr, Data.getUnUpload());
                appAttachments.add(appAttachment);
                mgr.addAppAttachment(appAttachments);
                Log.i(LOG_TAG, appAttachment.getAi_id());
            }
            else{
                List<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
                medicineChanges = mgr.queryToMedicineChange(Data.getD());
                medicineAttachments = new ArrayList<MedicineAttachment>();
                String filestr = fileUri.toString();
                filestr = filestr.substring(7, filestr.length());
                medicineAttachment = new MedicineAttachment(Data.getP_id() + startTime, name, Data.getP_id(), medicineChanges.get(medicineChanges.size() - 1).getMc_id(), filestr, Data.getUnUpload(), t == 2 ? 1 : 0);
                medicineAttachments.add(medicineAttachment);
                mgr.addMedicineAttachment(medicineAttachments);
            }
            // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
            // set the image file name
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    };

    private final OnClickListener takeVideoClickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            Log.d(LOG_TAG, "Take Video Button Click");
            // 摄像
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            // create a file to save the video
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
            // set the image file name
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // set the video image quality to high
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);

            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    };

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type)
    {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try
        {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment.getExternalStorageDirectory()+ "/MyCameraApp");

            Log.d(LOG_TAG, "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                // 在SD卡上创建文件夹需要权限：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d(LOG_TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        name = "IMG_" + timeStamp + ".jpg";
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(LOG_TAG, "onActivityResult: requestCode: " + requestCode
                + ", resultCode: " + requestCode + ", data: " + data);
        // 如果是拍照
        if (CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE == requestCode)
        {
            Log.d(LOG_TAG, "CAPTURE_IMAGE");

            if (RESULT_OK == resultCode)
            {
                Log.d(LOG_TAG, "RESULT_OK");

                // Check if the result includes a thumbnail Bitmap
                if (data != null)
                {
                    // 没有指定特定存储路径的时候
                    Log.d(LOG_TAG,
                            "data is NOT null, file on default position.");

                    // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                    // Image captured and saved to fileUri specified in the
                    // Intent
                    Toast.makeText(this, "Image saved to:\n" + data.getData(),
                            Toast.LENGTH_LONG).show();

                    if (data.hasExtra("data"))
                    {
                        Bitmap thumbnail = data.getParcelableExtra("data");
                        imageView.setImageBitmap(thumbnail);
                    }
                }
                else
                {

                    Log.d(LOG_TAG,
                            "data IS null, file saved on target position.");
                    // If there is no thumbnail image data, the image
                    // will have been stored in the target output URI.

                    // Resize the full image to fit in out image view.
                    int width = imageView.getWidth();
                    int height = imageView.getHeight();

                    BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

                    factoryOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(fileUri.getPath(), factoryOptions);

                    int imageWidth = factoryOptions.outWidth;
                    int imageHeight = factoryOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(imageWidth / width, imageHeight
                            / height);

                    // Decode the image file into a Bitmap sized to fill the
                    // View
                    factoryOptions.inJustDecodeBounds = false;
                    factoryOptions.inSampleSize = scaleFactor;
                    factoryOptions.inPurgeable = true;

                    Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                            factoryOptions);

                    imageView.setImageBitmap(bitmap);
                }
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // User cancelled the image capture
            }
            else
            {
                // Image capture failed, advise user
            }
        }
        // 如果是录像
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
        {
            Log.d(LOG_TAG, "CAPTURE_VIDEO");

            if (resultCode == RESULT_OK)
            {
            }
            else if (resultCode == RESULT_CANCELED)
            {
                // User cancelled the video capture
            }
            else
            {
                // Video capture failed, advise user
            }
        }
        uploadform();
    }


    public void uploadform(){
        if(!Data.isNetworkAvailable(this)) {
            Toast.makeText(getApplicationContext(),"因网络问题暂时无法上传，已保存结果，稍后会自动上传", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "";
                    String newName = "";
                    String uploadFile = "";
                    if(!type.equals("appinfo")) {
                        content ="i53/?"+ "MC_id=" + medicineAttachment.getMc_id() + "&sign=" + (t == 1 | t == 3 ? 0 : 1) + "&date=" + Data.longToString(Data.getDate()+Data.getD()*60*60*24*1000, "yyyy-MM-dd");
                        newName = medicineAttachment.getName();
                        uploadFile = medicineAttachment.getDir();
                    }

                    // Post请求的url，与get不同的是不需要带参数
                    else{
                        content ="i50/?"+"AI_id=" + appAttachment.getAi_id();

                        newName = appAttachment.getName();
                        uploadFile = appAttachment.getDir();
                    }

                    URL postUrl = new URL(Data.getUrl() +content);
                    Log.i("test", postUrl.toString());
                    //URL postUrl = new URL("http://www.ibreathcare.cn/i50/"+content);
                    String end = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
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
                    // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
                    // 要注意的是connection.getOutputStream会隐含的进行connect。


                    // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致

            /* 设置传送的method=POST */
                    connection.setRequestMethod("POST");
            /* setRequestProperty */

                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);


                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection
                            .getOutputStream());
                    //String content = "name=" + appAttachment.getName() + "&P_id=" + appAttachment.getP_id() + "&AI_id=" + appAttachment.getAi_id() + "&D_id=" + "0" + "&description=" + "from app";
                    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面


                    /*out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"name\""  + end);
                    out.writeBytes(appAttachment.getName() + end);
                    out.writeBytes(twoHyphens + boundary + end);
                    out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"P_id\""  + end);
                    out.writeBytes(appAttachment.getP_id() + end);
                    out.writeBytes(twoHyphens + boundary + end);
                    out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"AI_id\""  + end);

                    out.writeBytes(appAttachment.getAi_id() + end);
                    out.writeBytes(twoHyphens + boundary + end);
                    out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"D_id\""  + end);
                    out.writeBytes("0" + end);
                    out.writeBytes(twoHyphens + boundary + end);
                    out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"Description\""  + end);
                    out.writeBytes("from app" + end);
                    out.writeBytes(twoHyphens + boundary + end);
                    */


                    // out.writeBytes(twoHyphens + boundary + end);
//          ds.writeBytes(pidString);
//          ds.writeBytes(end);
                    out.writeBytes("Content-Disposition: form-data; "
                            + "name=\"upload_file\";filename=\"" + newName + "\"" + end);
                    out.writeBytes(end);
                    //* 取得文件的FileInputStream *//*
                    FileInputStream fStream = new FileInputStream(uploadFile);
                    //* 设置每次写入1024bytes *//*
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    //* 从文件读取数据至缓冲区 *//*
                    while ((length = fStream.read(buffer)) != -1) {
                        //* 将资料写入DataOutputStream中 *//*
                        out.write(buffer, 0, length);
                    }
                    out.writeBytes(end);
                    out.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    //* close streams *//*
                    fStream.close();
                    out.flush();
                    /*Log.i(LOG_TAG, "no1");
            *//* 取得Response内容 *//*
                    InputStream is = connection.getInputStream();
                    Log.i(LOG_TAG, is.toString());
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                    Log.i(LOG_TAG, b.toString());
                    Log.i(LOG_TAG, "no2");
            *//* 将Response显示于Dialog *//*
                    // showDialog("上传成功" + b.toString().trim());
                    System.out.println("---------" + b.toString().trim());*/

            /* 关闭DataOutputStream */
                    out.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i(LOG_TAG, line);
                        JSONObject jsonObject = new JSONObject(line);
                        String a = jsonObject.getString("result");
                        if(a.equals("0")) {
                            if(type.equals("appinfo")){
                                List<AppInfo> appInfos = new ArrayList<AppInfo>();
                                appInfos = mgr.queryToAppInfo(Data.getD());
                                mgr.deleteTodayAppAttachment(appInfos.get(appInfos.size()-1).getAi_id());
                                appAttachments.clear();
                                appAttachment.setState(Data.getUpload());
                                appAttachments.add(appAttachment);
                                mgr.addAppAttachment(appAttachments);
                            }
                            else{
                                mgr.deleteTodayMedicineAttachment(Data.getD());
                                medicineAttachments.clear();
                                medicineAttachment.setState(Data.getUpload());
                                medicineAttachments.add(medicineAttachment);
                                mgr.addMedicineAttachment(medicineAttachments);
                            }
                        }
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();

                } catch (Exception e) {
                   e.getMessage();
                }
            }
        }).start();

    }

}