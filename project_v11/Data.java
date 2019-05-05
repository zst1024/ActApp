package com.example.administrator.project_v11;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class Data extends Application {
    private static volatile String P_id = null;
    private static volatile String cat1 = null, cat2 = null, cat3 = null, cat4 = null, cat5 = null, cat6 = null,
            cat7 = null, cat8 = null, catSum = null, mrc = null, acuteExac = null, id  = null;
    private static int count = 0;
    private static volatile int i = 0;
    private static volatile int n = 0;
    private static volatile int m = 0;
    private static volatile int s =0;
    private static volatile String type = null, t_id  = null;
    private static volatile String change = null, c_id = null;
    private static volatile String regular = null, r_id = null;
    private static volatile int d = 0;
    private static volatile long maxd = 0;
    private static volatile int UnUpload = 0;
    private static volatile int Upload = 1;
    private static volatile int u = 0;
    private static volatile int v = 0;
    //private static String url = "http://192.168.1.210:8080/";
    private static String url = "http://www.ibreathcare.cn/";
    private static String msg = null;

    public static void setUrl(String url) {
        Data.url = url;
    }

    public static String getMsg() {
        return msg;
    }

    public static void setMsg(String msg) {
        Data.msg = msg;
    }

    public static int getV() {
        return v;
    }

    public static void setV(int v) {
        Data.v = v;
    }

    public static int getU() {
        return u;
    }

    public static void setU(int u) {
        Data.u = u;
    }

    public static String getUrl() {
        return url;
    }

    public static int getS() {
        return s;
    }

    public static void setS(int s) {
        Data.s = s;
    }

    public static int getUnUpload() {
        return UnUpload;
    }

    public static int getUpload() {
        return Upload;
    }

    public static String getC_id() {
        return c_id;
    }

    public static void setC_id(String c_id) {
        Data.c_id = c_id;
    }

    public static String getR_id() {
        return r_id;
    }

    public static void setR_id(String r_id) {
        Data.r_id = r_id;
    }

    public static String getT_id() {
        return t_id;
    }

    public static void setT_id(String t_id) {
        Data.t_id = t_id;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Data.id = id;
    }

    public static long getMaxd() {
        return maxd;
    }

    public static void setMaxd(long maxd) {
        Data.maxd = maxd;
    }

    public static String getP_id() {
        return P_id;
    }

    public static void setP_id(String p_id) {
        P_id = p_id;
    }

    public static String getCat1() {
        return cat1;
    }

    public static void setCat1(String cat1) {
        Data.cat1 = cat1;
    }

    public static String getCat2() {
        return cat2;
    }

    public static void setCat2(String cat2) {
        Data.cat2 = cat2;
    }

    public static String getCat3() {
        return cat3;
    }

    public static void setCat3(String cat3) {
        Data.cat3 = cat3;
    }

    public static String getCat4() {
        return cat4;
    }

    public static void setCat4(String cat4) {
        Data.cat4 = cat4;
    }

    public static String getCat5() {
        return cat5;
    }

    public static void setCat5(String cat5) {
        Data.cat5 = cat5;
    }

    public static String getCat6() {
        return cat6;
    }

    public static void setCat6(String cat6) {
        Data.cat6 = cat6;
    }

    public static String getCat7() {
        return cat7;
    }

    public static void setCat7(String cat7) {
        Data.cat7 = cat7;
    }

    public static String getCat8() {
        return cat8;
    }

    public static void setCat8(String cat8) {
        Data.cat8 = cat8;
    }

    public static String getCatSum() {
        return catSum;
    }

    public static void setCatSum(String catSum) {
        Data.catSum = catSum;
    }

    public static String getMrc() {
        return mrc;
    }

    public static void setMrc(String mrc) {
        Data.mrc = mrc;
    }

    public static String getAcuteExac() {
        return acuteExac;
    }

    public static void setAcuteExac(String acuteExac) {
        Data.acuteExac = acuteExac;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Data.count = count;
    }

    public static int getI() {
        return i;
    }

    public static void setI(int i) {
        Data.i = i;
    }

    public static int getM() {
        return m;
    }

    public static void setM(int m) {
        Data.m = m;
    }

    public static String getType() {
        return type;
    }

    public static void setType(String type) {
        Data.type = type;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        Data.n = n;
    }

    public static String getChange() {
        return change;
    }

    public static void setChange(String change) {
        Data.change = change;
    }

    public static String getRegular() {
        return regular;
    }

    public static void setRegular(String regular) {
        Data.regular = regular;
    }

    public static int getD() {
        return d;
    }

    public static void setD(int d) {
        Data.d = d;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = formatter.parse(strTime);
        return date;
    }
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }
    public static long getDate(){
        Date now = new Date();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getTime(){
        Date now = new Date();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(now);
        return cal.getTimeInMillis();
    }

    public static boolean isNetworkAvailable(Context context)
    {
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
