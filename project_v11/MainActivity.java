package com.example.administrator.project_v11;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jpushdemo.ExampleUtil;
import com.jpushdemo.LocalBroadcastManager;
import com.wertwind.database.DbAdapter;
import com.wertwind.pathreplay.mapActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

import static com.example.administrator.project_v11.R.id.listview;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, Runnable{
    private static DBManager mgr;
    private final int Setting = 1;
    private int Weather_bit;
    private final static String TAG = "MainActivity";
    private ListView listView;
    private TextView TextView_Today;
    private TextView TextView_Nextday;
    private TextView TextView_Prevday;
    private TextView dateTextView,dd;
    private TextView Left;
    private TextView Right;
    private Handler messageHandler;
    private ViewFlipper viewFlipper;
    private Main_list_Adapter mainlistAdapter;
    private TextView finish,aqirem;
    private ImageButton settings, mapper;
    private DbAdapter mDataBaseHelper;
    GestureDetector mGestrue = null;
    List<Main_List> main_lists = null;

    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;
    private Calendar calendar;
    private URL url,pm_url;
    private String jsonData,pm_jsonData;
    private InputStreamReader in,pm_in;
    private HttpURLConnection connPEK,pm;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private static final int TimeOut = 0;
    private static final int NoNet = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将Activity加入ActivityManagerUtils中方便管理
        ActivityManagerUtils.getInstance().addActivity(MainActivity.this);
        //检查权限
        checkManifest();

        //初始化DBManger
        mgr = new DBManager(this);
        //初始化相关TextView，
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        settings = (ImageButton) findViewById(R.id.imageButton);
        mapper = (ImageButton) findViewById(R.id.imageButton2);
        dateTextView=(TextView) findViewById(R.id.textView5);
        Left = (TextView) findViewById(R.id.Today);
        Right = (TextView) findViewById(R.id.Now);
        aqirem = (TextView)findViewById(R.id.aqiremark);
        finish = (TextView)findViewById(R.id.Finishment);
        dd = (TextView)findViewById(R.id.date);
        String datestr  = new DateFormat().format("yyyy-MM ", Calendar.getInstance(Locale.CHINA)) + getWeekday();
        dd.setText(datestr);
        //初始化设置
        initialization();
        try{
            if(Data.getP_id()!= null) {
                //开启后台定位服务
                this.startService(new Intent(this, ForegroundService.class));
                //开启后台监听服务
                this.startService(new Intent(this, FirstService.class));
                JPushInterface.init(getApplicationContext());
                registerMessageReceiver();
            }
        } catch (Exception e) {
            Log.d("MainActivity", e.getMessage());
        }
        if (isNetworkAvailable(MainActivity.this)) {
            if (Data.getU() == 1 && Data.getD() == 0) {
                Toast.makeText(getApplicationContext(), "正在检查更新...", Toast.LENGTH_LONG).show();
                upload();
                Data.setU(0);
                Toast.makeText(getApplicationContext(), "完成更新检查，请继续使用", Toast.LENGTH_LONG).show();
            }
            new Thread(this).start();
        }
        else
        {
            long time1;
            time1 = System.currentTimeMillis() + 1000;
            while(true) {
                if(isNetworkAvailable(MainActivity.this) && System.currentTimeMillis() < time1) {
                    new Thread(this).start();
                    Toast.makeText(getApplicationContext(), "当前没有可用网络，部分功能可能无法正常使用，请连接网络！", Toast.LENGTH_LONG).show();
                    break;
                }
                if(System.currentTimeMillis() > time1) {
                    new Thread(this).start();
                    Toast.makeText(getApplicationContext(), "当前没有可用网络，部分功能可能无法正常使用，请连接网络！", Toast.LENGTH_LONG).show();
                    break;
                }
            }
            Toast.makeText(getApplicationContext(), "当前没有可用网络，部分功能可能无法正常使用，请连接网络！", Toast.LENGTH_LONG).show();
        }
        //设置显示时间
        Date beginDate = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + Data.getD());
        //设置按钮响应
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);*/
                showDialog(Setting);
            }
        });
        mapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, mapActivity.class);
                    startActivity(intent);
                    ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
            }
        });
        //加载当前日期分页
        CreateDay();
        //添加Animation实现不同动画效果
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);
        slideLeftIn.setAnimationListener(animationListener);
        slideLeftOut.setAnimationListener(animationListener);
        slideRightIn.setAnimationListener(animationListener);
        slideRightOut.setAnimationListener(animationListener);
        //创建处理手势的实例
        mGestrue = new GestureDetector(this, new GestureListenter());
        //已经被删除的设置
        listView = (ListView) findViewById(listview);
        //ListView的一些操作
        main_lists = Main_List.getAllList();
        mainlistAdapter = new Main_list_Adapter(this, R.layout.listview_1, main_lists);
        listView.setAdapter(mainlistAdapter);
        Looper looper = Looper.myLooper();
        //此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
        messageHandler = new MessageHandler(looper);
        handler.postDelayed(runnable, 1000 * 60 * 60 * 2);
    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            Log.i(TAG, "reflush");
            this.update();
        }
        void update() {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);

        }
    };
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";

    public static final String KEY_EXTRAS = "extras";
    private MessageReceiver myReceiver;
    public void registerMessageReceiver() {
        myReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void setCostomMsg(String msg){
       /* if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(View.VISIBLE);
        }*/
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //这里实现用户操作，或同意或拒绝的逻辑
        //*grantResults会传进android.content.pm.PackageManager.PERMISSION_GRANTED 或 android.content.pm.PackageManager.PERMISSION_DENIED两个常，前者代表用户同意程序获取系统权限，后者代表用户拒绝程序获取系统权限*//*
    }
    private void checkManifest(){
        List<String> manifest = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            manifest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            manifest.add(Manifest.permission.CAMERA);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            manifest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            manifest.add(Manifest.permission.READ_PHONE_STATE);
        }
        switch (manifest.size()){
            case 0:
                break;
            case 1:
                ActivityCompat.requestPermissions(this, new String[]{manifest.get(0)}, 1);
                break;
            case 2:
                ActivityCompat.requestPermissions(this, new String[]{manifest.get(0), manifest.get(1)}, 1);
                break;
            case 3:
                ActivityCompat.requestPermissions(this, new String[]{manifest.get(0), manifest.get(1), manifest.get(2)}, 1);
                break;
            case 4:
                ActivityCompat.requestPermissions(this, new String[]{manifest.get(0), manifest.get(1), manifest.get(2), manifest.get(3)}, 1);
                break;
            default :
                break;
        }
    }
    private void upload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Post请求的url，与get不同的是不需要带参数
                    URL postUrl = new URL(Data.getUrl() + "i3000/");
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

                    //String content ="id=" + Data.getT_id() + "&AI_id=" + appInfo.getAi_id() + "&date=" + Data.longToString(appInfo.getDate(), "yyyy-MM-dd") + "&P_id=" + appInfo.getP_id() +"" + "&type=" + appInfo.getType();
                    // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
                    String content = "";
                    out.writeBytes(content);
                    //流用完记得关
                    out.flush();
                    out.close();
                    //获取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        JSONObject jsonObject = new JSONObject(line);
                        String a = jsonObject.getString("result");
                        PackageInfo pInfo = getPackageManager().getPackageInfo(
                                getPackageName(), 0);//获取当前的版本信息
                        try{
                            if(Double.parseDouble(a) > Double.parseDouble(pInfo.versionName)){
                                downloadPAK();
                                installAPK();
                            }
                        }catch (Exception e){
                            Log.d(TAG,e.getMessage());
                        }
                    }
                    reader.close();
                    //该干的都干完了,记得把连接断了
                    connection.disconnect();
                }catch(Exception e){
                    Log.d(TAG, e.getMessage());
                }
            } }).start();
    }
    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File("/sdcard/Test" + "/appName.apk")),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必须要加上这一句话才能保证安装完成后不会跳出到主界面
        startActivity(intent);
    }
    public boolean downloadPAK() {
        try {
            // 构造URL
            URL url = new URL(Data.getUrl() + "i3001/");
            // 打开连接
            URLConnection con = url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();

            // 输入流
            InputStream is = con.getInputStream();
            BufferedInputStream br = new BufferedInputStream(is);
            byte[] data = new byte[1024];
            int len;
            // 输出的文件流
            File file = new File("/sdcard/Test" + "/appName.apk");
            if (file.exists()) {
                file.delete();
            }
            File path = new File("/sdcard/Test");
            if (!path.exists()) {
                path.mkdirs();
            }
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            // 开始读取
            while ((len = is.read(data)) != -1) {
                os.write(data, 0, len);
            }
            os.flush();
            os.close();
            br.close();
            //通知用户下载成功
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //通知用户下载失败
        return false;
    }
    private String getWeekday(){
        int weekday;
        String Weekday = "";
        Date imaTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(imaTime);
        weekday = cal.get(Calendar.DAY_OF_WEEK);
        switch(weekday){
            case 1 : Weekday = "星期日";
                break;
            case 2 : Weekday = "星期一";
                break;
            case 3 : Weekday = "星期二";
                break;
            case 4 : Weekday = "星期三";
                break;
            case 5 : Weekday = "星期四";
                break;
            case 6 : Weekday = "星期五";
                break;
            case 7 : Weekday = "星期六";
                break;
        }
        return Weekday;
    }

    public void deletetable(){
        mDataBaseHelper = new DbAdapter(this);
        mDataBaseHelper.open();
        mDataBaseHelper.delete();
        mDataBaseHelper.close();
        mgr.delete();
    }

    public boolean returnFinishment(int x){
        List<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
        cat_mrcs = mgr.queryToCat_Mrc(x);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        appInfos = mgr.queryToAppInfo(x);
        List<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
        medicineChanges = mgr.queryToMedicineChange(x);
        if(cat_mrcs.size()>0 && appInfos.size()>0 && medicineChanges.size()>0){
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            jsonData = "";
            pm_jsonData = "";
            url = null;
            pm_url = null;
            in= null;
            url = new URL("http://api.k780.com:88/?app=weather.today&weaid=1&appkey=34727&sign=19a6a06a88cfafb904cccb4cd2e51869&format=json");
            pm_url = new URL("http://api.k780.com:88/?app=weather.pm25&weaid=1&appkey=34727&sign=19a6a06a88cfafb904cccb4cd2e51869&format=json");
            connPEK = (HttpURLConnection) url.openConnection();
            pm = (HttpURLConnection) pm_url.openConnection();
            // 这一步会连接网络得到输入流
            connPEK.setConnectTimeout(2000);
            pm.setConnectTimeout(2000);
            in = new InputStreamReader(connPEK.getInputStream());
            pm_in = new InputStreamReader(pm.getInputStream());
            // 为输入创建BufferedReader
            BufferedReader brPEK = new BufferedReader(in);
            BufferedReader brpm = new BufferedReader(pm_in);
            String inputLine = "";
            while (((inputLine = brPEK.readLine()) != null)) {
                jsonData += inputLine;
            }
            while (((inputLine = brpm.readLine()) != null)) {
                pm_jsonData += inputLine;
            }
            in.close(); // 关闭InputStreamReader
            pm_in.close();
            // 断开网络连接
            connPEK.disconnect();
            pm.disconnect();

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
            JSONObject jsonObject2 = new JSONObject(pm_jsonData);
            JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("result"));
            String left  = "今日天气\n" + jsonObject1.getString("temperature") + "\n" + jsonObject1.getString("weather") + "\n" + "湿度：" + jsonObject1.getString("humidity");
            String right = "目前状况\n" + jsonObject1.getString("temperature_curr") + "\n" + jsonObject1.getString("weather_curr") + "\n" + "PM2.5：" + jsonObject3.getString("aqi");
            //String up = "空气质量：" + jsonObject3.getString("aqi_levnm");
            String aqirem = jsonObject3.getString("aqi_remark");
            String lunar_ = Lunar.returnlunar();
            Message message = Message.obtain();
            message.obj = left;
            messageHandler.sendMessage(message);
            Message message1 = Message.obtain();
            message1.obj = right;
            messageHandler.sendMessage(message1);
            Message message2 = Message.obtain();
            message2.obj = aqirem;
            messageHandler.sendMessage(message2);
            Message message3 = Message.obtain();
            message3.obj = lunar_;
            messageHandler.sendMessage(message3);
        } catch (Throwable e) {
            Log.v(TAG, e.toString());
            Message msg = mHandler1.obtainMessage();
            msg.what = NoNet;
            mHandler1.sendMessage(msg);
        }
    }
    Handler mHandler1 = new Handler() {
        public void dispatchMessage(android.os.Message msg) {
            switch (msg.what) {
                //开始定位
                case TimeOut:
                    Toast.makeText(getApplicationContext(), "LIANJIE", Toast.LENGTH_LONG).show();
                    break;
                case NoNet:
                    Left.setText("监测到手机未连网，请连接4G或打开Wifi功能");
                    Right.setText("成功连网后请重新打开该程序");
                    break;
            }
        }
    };

    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }  
        @Override
        public void handleMessage(Message msg) {
            if(Weather_bit == 0) {
                Left.setText((String) msg.obj);
                Weather_bit = 1;
            }
            else if(Weather_bit == 1){
                Right.setText((String) msg.obj);
                Weather_bit = 2;
            }
            else if (Weather_bit == 2){
                aqirem.setText((String) msg.obj);
                Weather_bit = 3;
            }
            else{
                dateTextView.setText((String)msg.obj);
                Weather_bit = 0;
            }
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestrue.onTouchEvent(motionEvent);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        switch (id){
            case Setting:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("此选项需医护人员设置，是否继续？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* Intent i = new Intent(MainActivity.this, GetMessage.class);
                        startActivity(i);
                        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);*/
                    }
                });
                dialog = builder.create();
                break;
            default:
                break;
        }
        return dialog;
    }



    public void initialization(){
        //用户是否登录
        List<User> users = new ArrayList<User>();
        users = mgr.query_User();
        if(users.size() > 0){
            Data.setP_id(users.get(users.size() - 1).getP_id());
            Data.setMaxd(((Data.getDate() - users.get(users.size() - 1).getDate()) / (24 * 60 * 60 * 1000)) + 30);
            if((Data.getMaxd()+1) % 60 == 0){
                deletetable();
                users.get(users.size()-1).setDate(Data.getDate());
                mgr.addUser(users);
            }
        }
        else{
            Data.setP_id(null);
            Data.setMaxd(0);
            Toast.makeText(getApplicationContext(), "请点击左上角设置按钮进行登陆", Toast.LENGTH_LONG).show();
        }
        //今日是否填CAT表
        List<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
        cat_mrcs = mgr.queryToCat_Mrc(Data.getD());
        if(cat_mrcs.size() > 0){
            Data.setI(1);
            Data.setId(cat_mrcs.get(cat_mrcs.size()-1).getId());
        }
        else{
            Data.setI(0);
            Data.setId(null);
        }

        //用户是否填Appinfo表
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        appInfos = mgr.queryToAppInfo(Data.getD());
        if(appInfos.size() > 0){
            Data.setM(1);
            Data.setT_id(appInfos.get(appInfos.size()-1).getId());
        }
        else{
            Data.setM(0);
            Data.setT_id(null);
        }
        //用户是否填MedicineChange表
        List<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
        medicineChanges = mgr.queryToMedicineChange(Data.getD());
        if(medicineChanges.size() > 0){
            Data.setN(1);
            Data.setC_id(medicineChanges.get(medicineChanges.size()-1).getId());
        }
        else{
            Data.setN(0);
            Data.setC_id(null);
        }
        //用户是否填MedicineRegular表(因为MedicineRegular与MedicineChange两个表同时填写，所以只需判断一个即可)
        List<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
        medicineRegulars = mgr.queryToMedicineRegular(Data.getD());
        if(medicineRegulars.size() > 0){
            Data.setR_id(medicineRegulars.get(medicineRegulars.size()-1).getId());
        }
        else {
            Data.setR_id(null);
        }
        int i = (int) Data.getMaxd()- 30;
        Data.setV(0);
        while(i > 0){
            if(!returnFinishment(-i)){
                Data.setV(i);
                break;
            }
            i--;
        }
        List<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
        uploadInfos = mgr.queryToUploadInfo(Data.getD());
        if(uploadInfos.size() == 0 && Data.getP_id() != null){
            long a = -1;
            long starttime = Data.getDate()+ Data.getD()*24*60*60*1000;
            UploadInfo uploadInfo = new UploadInfo(Data.getP_id() + starttime, Data.getP_id(), a, a, a, a, a, a);
            uploadInfos.clear();
            uploadInfos.add(uploadInfo);
            mgr.addUploadInfo(uploadInfos);
            Data.setU(1);
        }
        if(returnFinishment(Data.getD()))
            finish.setText("该日问卷均已填写完成");
        else
            finish.setText("该日问卷尚未填写完成");
    }
    //响应返回键操作
    long lastPressTime = 0;
    public void onBackPressed(){
        if (new Date().getTime() - lastPressTime < 1500) {
            finish();//结束程序
        } else {
            lastPressTime = new Date().getTime();//重置lastPressTime
            Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
        }
    }
    /*public void onBackPressed() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        ActivityManagerUtils.getInstance().finishActivityclass(MainActivity.class);
        startActivity(home);
        super.onBackPressed();
    }*/

    protected void onDestroy() {
        super.onDestroy();
        //应用的最后一个Activity关闭时应释放DB
        mgr.closeDB();
        handler.removeCallbacks(runnable); //停止刷新
    }

    /**
     * 创建一个动画监听事件
     */
    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        //动画开始调用
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画完成调用
        @Override
        public void onAnimationEnd(Animation animation) {
            //动画完成，创建新的日期显示
            CreateDay();
        }

        //动画重复调用
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 内部类，继承SimpleOnGestureListener实现手势控制
     */
    class GestureListenter extends GestureDetector.SimpleOnGestureListener {
        @Override
        //e1为向量的起点，e2为向量的终点。velocityX为X轴的移动速度，velocityY为Y轴的移动速度。
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try{
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                {
                    return false;
                }
                //从右向左滑动，下一天
                if (e1.getX() -e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                {
                    viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                    viewFlipper.showNext();
                    //日期修改改为下一天
                    nextDay();
                    initialization();
                    main_lists.clear();
                    for(int i = 0; i < Main_List.getAllList().size(); i++)
                        main_lists .add(Main_List.getAllList().get(i));
                    mainlistAdapter.notifyDataSetChanged();
                    listView.setAdapter(mainlistAdapter);
                    return true;
                }else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    //从左向右滑动，前一天
                    viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                    viewFlipper.showPrevious();
                    //日期修改为上一天
                    previousDay();
                    initialization();
                    main_lists.clear();
                    for(int i = 0; i < Main_List.getAllList().size(); i++)
                        main_lists .add(Main_List.getAllList().get(i));
                    mainlistAdapter.notifyDataSetChanged();
                    listView.setAdapter(mainlistAdapter);
                    return true;
                }
            }catch(Exception e) {
            }
            return false;
        }
    }


    /**
     * 当前日期增加一天
     */
    private void nextDay() {
        if(Data.getD() < 0) {
            Data.setD(Data.getD()+1);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        else
            return;
    }

    /**
     * 当前日期减去一天
     */
    private void previousDay() {
        if(-Data.getMaxd() < Data.getD()) {
            Data.setD(Data.getD() - 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        else
            return;
    }

    public String getStringDateShort(Date currentTime) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     *  创建日期显示
     */
    public void CreateDay() {
        //创建三个日历对象
        Calendar tempCalendar1 = Calendar.getInstance();
        Calendar tempCalendar2 = Calendar.getInstance();
        Calendar tempCalendar3 = Calendar.getInstance();
        // 将三个日历对象分别设置成当前TextView中显示的日期时间。
        // calendar对象会根据滑动TextView控件而增加一天或减去一天。
        tempCalendar1.setTime(calendar.getTime());
        tempCalendar2.setTime(calendar.getTime());
        tempCalendar3.setTime(calendar.getTime());

        //创建当天的TextView对象，设置属性及内容
        TextView_Today = new TextView(this);
        TextView_Today.setGravity(Gravity.CENTER);
        TextView_Today.setTextSize(100);
        TextView_Today.setClickable(true);
        TextView_Today.setText(getStringDateShort(tempCalendar1.getTime()));

        //创建下一天的TextView对象，设置属性及内容
        TextView_Nextday = new TextView(this);
        TextView_Nextday.setGravity(Gravity.CENTER);
        TextView_Nextday.setTextSize(100);

        if(Data.getD() < 0) {
            TextView_Nextday.setClickable(true);
            tempCalendar2.add(Calendar.DAY_OF_MONTH, 1);
            TextView_Nextday.setText(getStringDateShort(tempCalendar2.getTime()));
        }

        //创建前一天的TextView对象，设置属性及内容
        TextView_Prevday = new TextView(this);
        TextView_Prevday.setGravity(Gravity.CENTER);
        TextView_Prevday.setTextSize(100);
        if(-Data.getMaxd() < Data.getD()) {
            TextView_Prevday.setClickable(true);
            tempCalendar3.add(Calendar.DAY_OF_MONTH, -1);
            TextView_Prevday.setText(getStringDateShort(tempCalendar3.getTime()));
        }
        //设置TextView的监听事件
        TextView_Today.setOnTouchListener(this);
        TextView_Nextday.setOnTouchListener(this);
        TextView_Prevday.setOnTouchListener(this);

        //如果viewFlipper里有内容则全部删除
        if (viewFlipper.getChildCount() != 0)
        {
            viewFlipper.removeAllViews();
        }
        //将TextView对象添加到ViewFlipper对象中
        viewFlipper.addView(TextView_Today);
        viewFlipper.addView(TextView_Nextday);
        viewFlipper.addView(TextView_Prevday);
    }
    public boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}



