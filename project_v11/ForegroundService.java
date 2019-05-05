package com.example.administrator.project_v11;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.cxmscb.cxm.processproject.aidl.ProcessService;
import com.wertwind.recordutil.Util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
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
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import static java.lang.Math.abs;


public class ForegroundService extends Service implements
        View.OnClickListener, AMapLocationListener {
    private static final String TAG = "ForegroundService";

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Intent alarmIntent = null;
    private PendingIntent alarmPi = null;
    private AlarmManager alarm = null;
    private DBManager mgr;

    private AMapLocation firstpoint = null, secondpoint = null;
    private static long Hour = 60 * 60 * 1000;
    private static int Step1 = 60;
    private static int Step2 = 30;
    private static int Step3 = 15;
    private static int Step4 = 10;
    private static int Step5 = 5;
    private int Time;
    private int temp;
    private int s;

    private com.wertwind.database.DbAdapter DbHepler;
    private com.wertwind.record.PathRecord record;
    private long mStartTime;
    private long mEndTime;

    private MyBinder binder;
    private MyConn conn;

    /*private boolean mReflectFlg = false;
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
    private Object[] mStopForegroundArgs = new Object[1];*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        ForegroundService.this.bindService(new Intent(this, FirstService.class), conn, Context.BIND_IMPORTANT);
    }

    class MyBinder extends ProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "I am ForegroundService";
        }
    }

    class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info", "与FirtService连接成功" + ForegroundService.this.getApplicationInfo().processName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(ForegroundService.this, "FirstService被杀死", Toast.LENGTH_SHORT).show();
            // 启动FirstService
            ForegroundService.this.startService(new Intent(ForegroundService.this, FirstService.class));
            //绑定FirstService
            ForegroundService.this.bindService(new Intent(ForegroundService.this, FirstService.class), conn, Context.BIND_IMPORTANT);
        }
    }

    private void loadtemp(){
        try {
            String filePath = "/sdcard/Test/" + Data.longToString(Data.getDate(), "yyyy-MM-dd") + "-temp.txt";
            File file = new File(filePath);
            if (file.exists()) {
                readTxtFile(filePath);
            }
        }catch (Exception e){

        }
    }

    private void readTxtFile(String filePath){
        try {
            int i= 0;
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuffer bf = new StringBuffer();
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    if(i == 0){
                        reset(lineTxt);
                    }
                    else{
                        bf.append(lineTxt);
                    }
                    i++;
                }
                record.setPathline(Util.parseLocations(bf.toString()));
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
    }
    private boolean readtxt(){
        try {
            String filePath = "/sdcard/Test/" + Data.longToString(Data.getDate(), "yyyy-MM-dd") + ".txt";
            File file = new File(filePath);
            if(file.isFile() && file.exists() && file.length() != 0) { //判断文件是否存在且不为空
                s = 0;
                return true;
            }
            else{
                s = 1;
                return false;
            }
        }catch (Exception e){
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new MyBinder();
        if (conn == null)
            conn = new MyConn();
        Log.d(TAG, "onCreate");
        s = 1;
        loadtemp();
        mgr = new DBManager(this);

        /* mNM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
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
        }*/

        alarmIntent = new Intent();
        alarmIntent.setAction("LOCATION");
        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"START"的intent
        alarmPi = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("LOCATION");
        registerReceiver(alarmReceiver, filter);

        // 启动定位
        //locationClient.startLocation();
        // AlarmManager对象,注意这里并不是new一个对象，Alarmmanager为系统级服务
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarm != null) {
                alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime(), Hour / 12, alarmPi);
        }
        //createSerive();
    }

    /*private void createSerive(){
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.icon);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("Foreground Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();
        startForegroundCompat(NOTIFICATION_ID, notification);
    }*/
    private void setupLocation() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(5 * 1000);
        Time = 1;
        temp = 0;
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(this);
    }

    private void resetLocation() {
        firstpoint = null;
        secondpoint = null;
        locationClient = null;
        locationOption = null;
    }

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

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        stopForegroundCompat(NOTIFICATION_ID);
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

    *//**
     * This is a wrapper around the new startForeground method, using the older
     * APIs if it is not available.
     *//*
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
            *//* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法startForeground设置前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground设置前台运行 *//*

            if(VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                // Fall back on the old API.
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    */
    /**
     * This is a wrapper around the new stopForeground method, using the older
     * APIs if it is not available.
     *//*
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
            *//* 还可以使用以下方法，当sdk大于等于5时，调用sdk现有的方法stopForeground停止前台运行，
             * 否则调用反射取得的sdk level 5（对应Android 2.0）以下才有的旧方法setForeground停止前台运行 *//*

            if(VERSION.SDK_INT >= 5) {
                stopForeground(true);
            } else {
                // Fall back on the old API.  Note to cancel BEFORE changing the
                // foreground state, since we could be killed at that point.
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean.FALSE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
            }
        }
    }*/

    public void uploadfile(int i){
        if(!Data.isNetworkAvailable(getApplicationContext()))
            return;
        final int x = i;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String content ="P_id=" + Data.getP_id() + "&date=" + Data.longToString(Data.getDate(), "yyyy-MM-dd");
                    URL postUrl = new URL(Data.getUrl() + "i54/?" + content);
                    //URL postUrl = new URL("http://www.ibreathcare.cn/i54/"+content);
                    String newName =  Data.longToString(Data.getDate()- 60*60*24*1000*x,"yyyy-MM-dd") + ".txt";
                    String uploadFile = "/sdcard/Test/" + Data.longToString(Data.getDate()- 60*60*24*1000*x,"yyyy-MM-dd") + ".txt";
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
                        JSONObject jsonObject = new JSONObject(line);
                        String a = jsonObject.getString("result");
                        if(a.equals("0")){
                            mgr.deleteTodayTrackInfo(-x);
                            Long starttime = Data.getDate()- 60*60*24*1000*x;
                            List<TrackInfo> trackInfos = new ArrayList<>();
                            TrackInfo trackInfo = new TrackInfo(Data.getP_id()+starttime, Data.getP_id(), starttime,
                                    Data.longToString(starttime,"yyyy-MM-dd") + ".txt",
                                    "/sdcard/Test/" + Data.longToString(starttime,"yyyy-MM-dd") + ".txt", Data.getUpload());
                            trackInfos.add(trackInfo);
                            mgr.addTrackInfo(trackInfos);
                            List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
                            uploadInfos = mgr.queryToUploadInfo(-x);
                            UploadInfo uploadInfo = uploadInfos.get(uploadInfos.size()-1);
                            uploadInfos.clear();
                            mgr.deleteTodayUploadInfo(-x);
                            uploadInfo.setTrackInfo(Data.getDate());
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
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("LOCATION")) {
                //因为setWindow只执行一次，所以要重新定义闹钟实现循环。
                if (System.currentTimeMillis() > getStarttime() && System.currentTimeMillis() < getStoptime()) {
                    if (null == locationClient) {
                        if (record != null) {
                            record = null;
                        }
                        record = new com.wertwind.record.PathRecord();
                        mStartTime = System.currentTimeMillis();
                        record.setDate(getcueDate(mStartTime));
                        setupLocation();
                        mHandler.sendEmptyMessage(Utils.MSG_LOCATION_START);
                        locationClient.startLocation();
                    }
                } else {
                    if (null != locationClient) {
                        if(Data.getP_id() == null){
                            List<User> users = new ArrayList<User>();
                            users = mgr.query_User();
                            if(users.size() > 0){
                                Data.setP_id(users.get(users.size() - 1).getP_id());
                            }
                        }
                        mHandler.sendEmptyMessage(Utils.MSG_LOCATION_STOP);
                        locationClient.stopLocation();
                        locationClient.onDestroy();
                        resetLocation();
                        mEndTime = System.currentTimeMillis();
                        saveRecord(record.getPathline(), record.getDate());
                        try {
                            String filePath = "/sdcard/Test/";
                            String fileName = Data.longToString(Data.getDate(), "yyyy-MM-dd") + "-temp.txt";
                            DeleteFolder(filePath + fileName);
                        }catch (Exception e){

                        }
                        List<TrackInfo> trackInfos = new ArrayList<>();
                        TrackInfo trackInfo = new TrackInfo(Data.getP_id()+Data.getDate(), Data.getP_id(), Data.getDate(),
                                new DateFormat().format("yyyy-MM-dd", Calendar.getInstance(Locale.CHINA)) + ".txt",
                                "/sdcard/Test/" + new DateFormat().format("yyyy-MM-dd", Calendar.getInstance(Locale.CHINA)) + ".txt", Data.getUnUpload());
                        trackInfos.add(trackInfo);
                        mgr.addTrackInfo(trackInfos);
                        uploadfile(0);
                    }
                }
            }
        }
    };

    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            Message msg = mHandler.obtainMessage();
            msg.obj = loc;
            msg.what = Utils.MSG_LOCATION_FINISH;
            mHandler.sendMessage(msg);
        }
    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case Utils.MSG_LOCATION_START:
                    if(!readtxt()) {
                        initData("[");
                        Log.i(TAG, "start");
                    }
                    break;
                // 定位完成
                case Utils.MSG_LOCATION_FINISH:
                    AMapLocation loc = (AMapLocation) msg.obj;
                    if (firstpoint == null)
                        firstpoint = loc;
                    else {
                        secondpoint = firstpoint;
                        firstpoint = loc;
                        changeTime(firstpoint, secondpoint);
                    }
                    if(loc.getLatitude() == 0.0 && loc.getLongitude() == 0.0 && loc.getTime() < 100 * 60 * 60 * 1000)
                        break;
                    record.addpoint(loc);
                    saveTemp(record.getPathline(), record.getDate());
                    String result = Utils.getLocationStr(loc);
                    if(s == 1)
                        initData(result);
                    else
                        initData(","+ result);
                    s = 0;
                    Log.i(TAG, "Location");
                    break;
                //停止定位
                case Utils.MSG_LOCATION_STOP:
                    initData("]");
                    Log.i(TAG, "stop");
                    break;
                default:
                    break;
            }
        }
    };

    private void changeTime(AMapLocation first, AMapLocation second) {
        if(second.getSpeed() == 0){
            temp++;
            if(temp == 5){
                Time += 5;
                temp = 0;
            }
            if(Time > 10)
                Time = 10;
            locationOption.setInterval(Time * 60 * 1000);
        }
        else{
            temp = 0;
            Time = 1;
            if(second.getSpeed() <= 0.1){
                locationOption.setInterval(Step1 * 1000);
            }
            else if(second.getSpeed() > 0.1 && second.getSpeed() <= 0.5){
                locationOption.setInterval(Step2 * 1000);
            }
            else if(second.getSpeed() > 0.5 && second.getSpeed() <= 1){
                locationOption.setInterval(Step3 * 1000);
            }
            else{
                if(abs(first.getSpeed()-second.getSpeed()) / second.getSpeed() <= 0.1){
                    locationOption.setInterval(Step4 * 1000);
                }
                else{
                    locationOption.setInterval(Step5 * 1000);
                }
            }
        }
        locationClient.setLocationOption(locationOption);
    }

    private long getStarttime() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 5);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private long getStoptime() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    @Override
    public void onClick(View v) {
    }

    private void initData(String content) {
        String filePath = "/sdcard/Test/";
        String fileName = new DateFormat().format("yyyy-MM-dd", Calendar.getInstance(Locale.CHINA)) + ".txt";
        writeTxtToFile(content, filePath, fileName);
    }

    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return file.delete();
            }
        }
        return false;
    }
    private void saveTempData(String content){
        try {
            String filePath = "sdcard/Test/";
            String fileName = Data.longToString(Data.getDate(), "yyyy-MM-dd") + "-temp.txt";
            DeleteFolder(filePath + fileName);
            writeTxtToFile(content, filePath, fileName);
        }catch (Exception e){
            e.getMessage();
        }

    }

    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        String strContent = strcontent;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }
    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    private void saveTemp(List<AMapLocation> list, String time){
        String pathlineSring = getPathLine(list);
        saveTempData(time + "\n" + pathlineSring);
    }

    private void reset(String time){
        record = new com.wertwind.record.PathRecord();
        record.setDate(time);
        setupLocation();
        locationClient.startLocation();
    }

    protected void saveRecord(List<AMapLocation> list, String time) {
        if (list != null && list.size() > 0) {
            DbHepler = new com.wertwind.database.DbAdapter(this);
            DbHepler.open();
            String duration = getDuration();
            float distance = getDistance(list);
            String average = getAverage(distance);
            String pathlineSring = getPathLineString(list);
            AMapLocation firstLocaiton = list.get(0);
            AMapLocation lastLocaiton = list.get(list.size() - 1);
            String stratpoint = amapLocationToString(firstLocaiton);
            String endpoint = amapLocationToString(lastLocaiton);
            DbHepler.createrecord(String.valueOf(distance), duration, average,
                    pathlineSring, stratpoint, endpoint, time);
            DbHepler.close();
        }
    }
    private String getDuration() {
        return String.valueOf((mEndTime - mStartTime) / 1000f);
    }

    private String getAverage(float distance) {
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }

    private float getDistance(List<AMapLocation> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            AMapLocation firstpoint = list.get(i);
            AMapLocation secondpoint = list.get(i + 1);
            LatLng firstLatLng = new LatLng(firstpoint.getLatitude(),
                    firstpoint.getLongitude());
            LatLng secondLatLng = new LatLng(secondpoint.getLatitude(),
                    secondpoint.getLongitude());
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    private String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
             return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;
     }
     private String getPathLine(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        return pathline.toString();
    }
    private String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }
}
