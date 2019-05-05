package com.example.administrator.project_v11;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.cxmscb.cxm.processproject.aidl.ProcessService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class FirstService extends Service {
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
    private MyBinder binder; //绑定服务需要Binder进行交互
    private MyConn conn;
    private DBManager mgr;
    private static final String TAG = "FirstService";

    private Intent alarmIntent1 = null;
    private PendingIntent alarmPi1 = null;
    private AlarmManager alarm1 = null;

    private boolean mReflectFlg = false;

    private static final int NOTIFICATION_ID = 1; // 如果id设置为0,会导致不能设置为前台service
    private static final Class<?>[] mSetForegroundSignature = new Class[] {
            boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[] {
            int.class, Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[] {
            boolean.class};

    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        stopForegroundCompat(NOTIFICATION_ID);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        if(conn==null)
            conn = new MyConn();
        alarmIntent1 = new Intent();
        alarmIntent1.setAction("START");
        mgr = new DBManager(this);
        alarmPi1 = PendingIntent.getBroadcast(this, 0, alarmIntent1, 0);
        mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mStartForeground = ForegroundService.class.getMethod("startForeground", mStartForegroundSignature);
            mStopForeground = ForegroundService.class.getMethod("stopForeground", mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }

        try {
            mSetForeground = getClass().getMethod("setForeground",
                    mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "OS doesn't have Service.startForeground OR Service.setForeground!");
        }
        createSerive();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction("START");
        registerReceiver(alarmReceiver1, filter1);
        alarm1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarm1 != null) {
                alarm1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(), 60*1000*60*3, alarmPi1);
        }
    }
    public void uploadfile(int i){
        final int x = i;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content ="P_id=" + Data.getP_id() + "&date=" + Data.longToString(Data.getDate(), "yyyy-MM-dd");
                    URL postUrl = new URL(Data.getUrl() + "i54/?" + content);
                    //URL postUrl = new URL("http://www.ibreathcare.cn/i54/"+content);
                    String newName =  Data.longToString(Data.getDate()- 60*60*24*1000 * x,"yyyy-MM-dd") + ".txt";
                    String uploadFile = "/sdcard/Test/" +  Data.longToString(Data.getDate()- 60*60*24*1000 * x,"yyyy-MM-dd") + ".txt";
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
                            + "name=\"myfile\";filename=\"" + newName + "\"" + end);
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

            /* 取得Response内容 */
                    /*InputStream is = connection.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
            *//* 将Response显示于Dialog *//*
                    // showDialog("上传成功" + b.toString().trim());
                    System.out.println("---------" + b.toString().trim());*/

            /* 关闭DataOutputStream */
                    out.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i(TAG, line);
                        JSONObject jsonObject = new JSONObject(line);
                        String a = jsonObject.getString("result");
                        if(a.equals("0")){
                            mgr.deleteTodayTrackInfo(-x);
                            List<TrackInfo> trackInfos = new ArrayList<>();
                            Long starttime= Data.getDate() - 60*60*24*1000*x;
                            TrackInfo trackInfo = new TrackInfo(Data.getP_id()+starttime, Data.getP_id(), starttime,
                                    Data.longToString(starttime,"yyyy-MM-dd") + ".txt",
                                    "/sdcard/Test/" +  Data.longToString(starttime,"yyyy-MM-dd") + ".txt", Data.getUpload());
                            trackInfos.add(trackInfo);
                            mgr.addTrackInfo(trackInfos);
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(-x);
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(-x);
                            uploadInfo.setTrackInfo(Data.getDate() - 60*60*24*1000*x);
                            uploadInfos.add(uploadInfo);
                            mgr.addUploadInfo(uploadInfos);
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
    private void upload(int i) {
        List<CAT_MRC> cat_mrcs2 = new ArrayList<CAT_MRC>();
        cat_mrcs2 = mgr.queryToCat_Mrc(-i);
        final int x = i;
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

                        String content = "id=" + null + "&P_id=" + Pid + "&date=" + Data.longToString(cat_mrc1.getDate(), "yyyy-MM-dd")  + "&cat1=" + C1 + "&cat2=" + C2 + "&cat3=" + C3 + "&cat4=" + C4 + "&cat5=" + C5 +"&cat6="+ C6 + "&cat7=" +C7 +"&cat8=" +C8 +"&catSum=" + catSum+"&mrc=" + Mrc + "&acuteExac=" + acuteExac;
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
                                mgr.deleteTodayCAT_MRC(-x);
                                ArrayList<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
                                cat_mrc1.setId(Data.getId());
                                cat_mrc1.setState(Data.getUpload());
                                cat_mrcs.add(cat_mrc1);
                                mgr.addCat_Mrc(cat_mrcs);
                                List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                                uploadInfos = mgr.queryToUploadInfo(-x);
                                UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                                uploadInfos.clear();
                                mgr.deleteTodayUploadInfo(-x);
                                uploadInfo.setCatMrc(Data.getDate() - 24*60*60*1000*x);
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
    private void uploadmc(int i){
        List<MedicineChange>medicineChanges = mgr.queryToMedicineChange(-i);
        final int x = i;
        final MedicineChange medicineChange = medicineChanges.get(medicineChanges.size()-1);
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

                    String content = "id=" + null +"&MC_id=" + medicineChange.getMc_id() + "&change=" + medicineChange.getChange() + "&P_id=" + medicineChange.getP_id() + "&date=" + Data.longToString(medicineChange.getDate(), "yyyy-MM-dd") ;
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
                        Data.setC_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        if(a.equals("0")) {
                            mgr.deleteTodayMedicineChange(-x);
                            ArrayList<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
                            medicineChange.setId(Data.getC_id());
                            medicineChange.setState(Data.getUpload());
                            medicineChanges.add(medicineChange);
                            mgr.addMedicineChange(medicineChanges);
                            //解析返回值，若上传成功，则更新本地上传表，否则不更新。
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(-x);
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(-x);
                            uploadInfo.setMedicineCha(Data.getDate() - 24*60*60*1000*x);
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
    private void uploadmr(int i){
        List<MedicineRegular> medicineRegulars = mgr.queryToMedicineRegular(-i);
        final MedicineRegular medicineRegular = medicineRegulars.get(medicineRegulars.size()-1);
        final int x = i;
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

                    String content ="id="  + null + "&regular=" + medicineRegular.getRegular() + "&P_id=" + medicineRegular.getP_id() + "&date=" + Data.longToString(medicineRegular.getDate(), "yyyy-MM-dd") ;
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
                        Data.setR_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        if(a.equals("0")) {
                            mgr.deleteTodayMedicineRegular(-x);
                            ArrayList<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
                            medicineRegular.setId(Data.getR_id());
                            medicineRegular.setState(Data.getUpload());
                            medicineRegulars.add(medicineRegular);
                            mgr.addMedicineRegular(medicineRegulars);
                            //解析返回值，若上传成功，则更新本地上传表，否则不更新。
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(-x);
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(-x);
                            uploadInfo.setMedicineReg(Data.getDate() - 24*60*60*1000*x);
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
    private void uploadai(int i){
        List<AppInfo> appInfos = mgr.queryToAppInfo(-i);
        final AppInfo appInfo = appInfos.get(appInfos.size()-1);
        final int x = i;
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

                    String content ="id=" + null + "&AI_id=" + appInfo.getAi_id() + "&date=" + Data.longToString(appInfo.getDate(), "yyyy-MM-dd") + "&P_id=" + appInfo.getP_id() +"" + "&type=" + appInfo.getType();
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
                        Data.setT_id(jsonObject.getString("id"));
                        String a = jsonObject.getString("result");
                        if (a.equals("0")) {
                            mgr.deleteTodayAppinfo(-x, appInfo.getType());
                            ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
                            appInfo.setId(Data.getT_id());
                            appInfo.setState(Data.getUpload());
                            appInfos.add(appInfo);
                            mgr.addAppinfo(appInfos);
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(-x);
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(-x);
                            uploadInfo.setAppInfo(Data.getDate() - 24*60*60*1000*x);
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
    public void uploadform(String i, MedicineAttachment ma, AppAttachment aa){
        final MedicineAttachment medicineAttachment = ma;
        final AppAttachment appAttachment = aa;
        final String type = i;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content = "";
                    String newName = "";
                    String uploadFile = "";
                    if(!type.equals("appinfo")) {
                        try {
                            String data = medicineAttachment.getMa_id().substring(12);
                            content = "i53/?" + "MC_id=" + medicineAttachment.getMc_id() + "&sign=" + medicineAttachment.getSign() + "&date=" + Data.longToString(Long.parseLong(data.trim()), "yyyy-MM-dd");
                            newName = medicineAttachment.getName();
                            uploadFile = medicineAttachment.getDir();
                        }catch (Exception e){
                            Log.i(TAG, e.getMessage());
                        }
                    }

                    // Post请求的url，与get不同的是不需要带参数
                    else{
                        content ="i50/?"+"AI_id=" + appAttachment.getAi_id();
                        newName = appAttachment.getName();
                        uploadFile = appAttachment.getDir();
                    }
                    Log.i(TAG, appAttachment.getAa_id());
                    URL postUrl = new URL(Data.getUrl() +content);
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
            /* 取得Response内容 */
                   /* InputStream is = connection.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
            *//* 将Response显示于Dialog *//*
                    // showDialog("上传成功" + b.toString().trim());
                    System.out.println("---------" + b.toString().trim());*/

            /* 关闭DataOutputStream */
                    out.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        Log.i(TAG, line);
                        JSONObject jsonObject = new JSONObject(line);
                        String a = jsonObject.getString("result");
                        if(a.equals("0")) {
                            if(type.equals("appinfo")){
                                try{
                                    String data = appAttachment.getAa_id().substring(12);
                                    int temp = (int)((Data.getDate() -  Long.parseLong(data.trim())) / (24*60*60*1000));
                                    List<AppInfo> appInfos = new ArrayList<AppInfo>();
                                    appInfos = mgr.queryToAppInfo(-temp);
                                    mgr.deleteTodayAppAttachment(appInfos.get(appInfos.size()-1).getAi_id());
                                    List<AppAttachment> appAttachments = new ArrayList<AppAttachment>();
                                    appAttachment.setState(Data.getUpload());
                                    appAttachments.add(appAttachment);
                                    mgr.addAppAttachment(appAttachments);
                                }catch (Exception e){
                                    e.getMessage();
                                }
                            }
                            else{
                                try{
                                    String data = medicineAttachment.getMa_id().substring(12);
                                    int temp = (int)((Data.getDate() -  Long.parseLong(data.trim())) / (24*60*60*1000));
                                    mgr.deleteTodayMedicineAttachment(-temp);
                                    List<MedicineAttachment> medicineAttachments = new ArrayList<MedicineAttachment>();
                                    medicineAttachment.setState(Data.getUpload());
                                    medicineAttachments.add(medicineAttachment);
                                    mgr.addMedicineAttachment(medicineAttachments);
                                }catch (Exception e){
                                    e.getMessage();
                                }
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
    private BroadcastReceiver alarmReceiver1= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("START")) {
                try {
                    if(!Data.isNetworkAvailable(getApplicationContext()))
                        return;
                    if(Data.getP_id() == null){
                        List<User> users = new ArrayList<User>();
                        users = mgr.query_User();
                        if(users.size() > 0){
                            Data.setP_id(users.get(users.size() - 1).getP_id());
                        }
                    }
                    List<UploadInfo> UploadInfos = new ArrayList<>();
                    UploadInfos = mgr.query_UploadInfo();
                    int min, max;
                    min = (int)Long.parseLong(UploadInfos.get(0).getUi_id().substring(12));
                    max = min;
                    for(int i = 0; i < UploadInfos.size(); i++){
                       if((int)Long.parseLong(UploadInfos.get(i).getUi_id().substring(12)) > max) {
                           max = (int) Long.parseLong(UploadInfos.get(i).getUi_id().substring(12));
                       }
                       if((int)Long.parseLong(UploadInfos.get(i).getUi_id().substring(12)) < min){
                           min = (int)Long.parseLong(UploadInfos.get(i).getUi_id().substring(12));
                       }
                    }
                    int c = (max - min) / (24*60*60*1000);

                    while(c >= 0) {
                        List<UploadInfo> uploadInfos = new ArrayList<>();
                        uploadInfos = mgr.queryToUploadInfo(-c);
                        if (uploadInfos.size() != 0) {
                            if (uploadInfos.get(uploadInfos.size() - 1).getTrackInfo() == Data.getDate() - c * 24* 60 * 60 *1000) {

                            } else {
                                List<TrackInfo> trackInfos = new ArrayList<>();
                                trackInfos = mgr.queryToTrackInfo(-c);
                                if (trackInfos.size() != 0) {
                                    if (trackInfos.get(trackInfos.size() - 1).getState() == Data.getUnUpload()) {
                                        Log.i(TAG, "trackinfo");
                                        uploadfile(c);
                                    }
                                }
                                /*long i = (Data.getDate() - uploadInfos.get(uploadInfos.size() - 1).getTrackInfo()) / (60 * 60 * 1000 * 24);
                                for (int x = (int) i - 1; x >= 0; x--) {
                                    List<TrackInfo> trackInfos = new ArrayList<>();
                                    trackInfos = mgr.queryToTrackInfo(x);
                                    if (trackInfos != null) {
                                        if (trackInfos.get(trackInfos.size() - 1).getState() == Data.getUnUpload()) {
                                            uploadfile(x);
                                        }
                                    }
                                }*/
                            }
                            if (uploadInfos.get(uploadInfos.size() - 1).getCatMrc() == Data.getDate()- c * 24* 60 * 60 *1000) {

                            } else {
                                List<CAT_MRC> cat_mrcs = new ArrayList<>();
                                cat_mrcs = mgr.queryToCat_Mrc(-c);
                                if (cat_mrcs.size() != 0) {
                                    if (cat_mrcs.get(cat_mrcs.size() - 1).getState() == Data.getUnUpload()) {
                                        Log.i(TAG, "cat");
                                        upload(c);
                                    }
                                }
                                /*long i = (Data.getDate() - uploadInfos.get(uploadInfos.size() - 1).getCatMrc()) / (60 * 60 * 1000 * 24);
                                for (int x = (int) i - 1; x >= 0; x--) {
                                    List<CAT_MRC> cat_mrcs = new ArrayList<>();
                                    cat_mrcs = mgr.queryToCat_Mrc(x);
                                    if (cat_mrcs != null) {
                                        if (cat_mrcs.get(cat_mrcs.size() - 1).getState() == Data.getUnUpload()) {
                                            upload(x);
                                        }
                                    }
                                }*/
                            }
                            if (uploadInfos.get(uploadInfos.size() - 1).getMedicineCha() == Data.getDate()- c * 24* 60 * 60 *1000) {

                            } else {
                                List<MedicineChange> medicineChanges = new ArrayList<>();
                                medicineChanges = mgr.queryToMedicineChange(-c);
                                if (medicineChanges.size() != 0) {
                                    if (medicineChanges.get(medicineChanges.size() - 1).getState() == Data.getUnUpload()) {
                                        Log.i(TAG, "medicine");
                                        uploadmc(c);
                                    }
                                }
                                List<MedicineAttachment> medicineAttachments = mgr.queryToMedicineAttachment(-c);
                                if (medicineAttachments.size() != 0) {
                                    for (int i = 0; i < medicineAttachments.size(); i++) {
                                        if (medicineAttachments.get(i).getState() == Data.getUnUpload()) {
                                            Log.i(TAG, "medicineattchment");
                                            uploadform("medicine", medicineAttachments.get(i), null);
                                        }
                                    }
                                }
                                /*long i = (Data.getDate() - uploadInfos.get(uploadInfos.size() - 1).getMedicineCha()) / (60 * 60 * 1000 * 24);
                                for (int x = (int) i - 1; x >= 0; x--) {
                                    List<MedicineChange> medicineChanges = new ArrayList<>();
                                    medicineChanges = mgr.queryToMedicineChange(x);
                                    if (medicineChanges != null) {
                                        if (medicineChanges.get(medicineChanges.size() - 1).getState() == Data.getUnUpload()) {
                                            uploadmc(x);
                                        }
                                    }
                                }*/
                            }
                            if (uploadInfos.get(uploadInfos.size() - 1).getMedicineReg() == Data.getDate()- c * 24* 60 * 60 *1000) {

                            } else {
                                List<MedicineRegular> medicineRegulars = new ArrayList<>();
                                medicineRegulars = mgr.queryToMedicineRegular(-c);
                                if (medicineRegulars.size() != 0) {
                                    if (medicineRegulars.get(medicineRegulars.size() - 1).getState() == Data.getUnUpload()) {
                                        Log.i(TAG, "medicine2");
                                        uploadmr(c);
                                    }
                                }
                                /*long i = (Data.getDate() - uploadInfos.get(uploadInfos.size() - 1).getMedicineReg()) / (60 * 60 * 1000 * 24);
                                for (int x = (int) i - 1; x >= 0; x--) {
                                    List<MedicineRegular> medicineRegulars = new ArrayList<>();
                                    medicineRegulars = mgr.queryToMedicineRegular(x);
                                    if (medicineRegulars != null) {
                                        if (medicineRegulars.get(medicineRegulars.size() - 1).getState() == Data.getUnUpload()) {
                                            uploadmr(x);
                                        }
                                    }
                                }*/
                            }
                            if (uploadInfos.get(uploadInfos.size() - 1).getAppInfo() == Data.getDate()- c * 24* 60 * 60 *1000) {

                            } else {
                                List<AppInfo> appInfos = new ArrayList<>();
                                appInfos = mgr.queryToAppInfo(-c);
                                if (appInfos.size() != 0) {
                                    if (appInfos.get(appInfos.size() - 1).getState() == Data.getUnUpload()) {
                                        Log.i(TAG, "appinfo");
                                        uploadai(c);
                                    }
                                    List<AppAttachment> appAttachments = mgr.queryToAppAttachment(appInfos.get(appInfos.size()-1).getAi_id());
                                    if (appAttachments.size() != 0){
                                        for(int i = 0; i < appAttachments.size(); i++){
                                            if(appAttachments.get(i).getState() == Data.getUnUpload()){
                                                Log.i(TAG, "appattachment");
                                                uploadform("appinfo", null, appAttachments.get(i));
                                            }
                                        }
                                    }
                                }
                                /*long i = (Data.getDate() - uploadInfos.get(uploadInfos.size() - 1).getAppInfo()) / (60 * 60 * 1000 * 24);
                                for (int x = (int) i - 1; x >= 0; x--) {
                                    List<AppInfo> appInfos = new ArrayList<>();
                                    appInfos = mgr.queryToAppInfo(x);
                                    if (appInfos != null) {
                                        if (appInfos.get(appInfos.size() - 1).getState() == Data.getUnUpload()) {
                                            uploadai(x);
                                        }
                                    }
                                }*/
                            }
                        }
                        /*List<AppAttachment> appAttachments = mgr.query_AppAttachment();
                        if (appAttachments.size() != 0) {
                            for (int i = 0; i < appAttachments.size(); i++) {
                                if (appAttachments.get(i).getState() == Data.getUnUpload()) {
                                  *//*  Log.i(TAG, "appattachment");*//*
                                    uploadform("appinfo", null, appAttachments.get(i));
                                }
                            }
                        }*/
                       /* List<MedicineAttachment> medicineAttachments = mgr.query_MedicineAttachment();
                        if (medicineAttachments.size() != 0) {
                            for (int i = 0; i < medicineAttachments.size(); i++) {
                                if (medicineAttachments.get(i).getState() == Data.getUnUpload()) {
                                  *//*  Log.i(TAG, "medicineattchment");*//*
                                    uploadform("medicine", medicineAttachments.get(i), null);
                                }
                            }
                        }*/
                        c--;
                    }
                    /*
                   检查上传
                   */
                }catch (Exception e){
                    Log.i(TAG, e.getMessage());
                }
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // 与SecondSevice绑定
        FirstService.this.bindService(new Intent(this,ForegroundService.class),conn, Context.BIND_IMPORTANT);
    }


    //使用aidl实现进程通信

    class MyBinder extends ProcessService.Stub{

        @Override
        public String getServiceName() throws RemoteException {
            return "I am ForegroundService";
        }
    }

    //建立相互绑定时的连接
    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            Log.i("Info","与ForegroundService连接成功");

        }

        //在异常断开的回调方法进行拉起对方服务并绑定
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(FirstService.this,"ForegroundService被杀死",Toast.LENGTH_SHORT).show();
            // 启动FirstService
            FirstService.this.startService(new Intent(FirstService.this,ForegroundService.class));
            //绑定FirstService
            FirstService.this.bindService(new Intent(FirstService.this,ForegroundService.class),conn, Context.BIND_IMPORTANT);
        }
    }

    private void createSerive(){
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("慢阻肺医疗");
        Notification notification = builder.build();
        startForegroundCompat(NOTIFICATION_ID, notification);
    }

    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Log.w("ApiDemos", "Unable to invoke method", e);
        }
    }

    /**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     */
    void startForegroundCompat(int id, Notification notification) {
        if (mReflectFlg) {
            // If we have the new startForeground API, then use it.
            if (mStartForeground != null) {
                mStartForegroundArgs[0] = Integer.valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod(mStartForeground, mStartForegroundArgs);
                return;
            }

            // Fall back on the old API.
            mSetForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 */

            if(Build.VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     */
    void stopForegroundCompat(int id) {
        if (mReflectFlg) {
            // If we have the new stopForeground API, then use it.
            if (mStopForeground != null) {
                mStopForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mStopForeground, mStopForegroundArgs);
                return;
            }

            // Fall back on the old API.  Note to cancel BEFORE changing the
            // foreground state, since we could be killed at that point.
            mNM.cancel(id);
            mSetForegroundArgs[0] = Boolean.FALSE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
        } else {
            /* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法stopForeground停止前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行 */

            if(Build.VERSION.SDK_INT >= 5) {
                stopForeground(true);
            } else {
                // Fall back on the old API.  Note to cancel BEFORE changing the
                // foreground state, since we could be killed at that point.
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean.FALSE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
            }
        }
    }



}