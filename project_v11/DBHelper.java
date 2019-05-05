package com.example.administrator.project_v11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "test.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User" +
                "(P_id CHAR(10) PRIMARY KEY, date INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS CAT_MRC" +
                "(CM_id CHAR(32) PRIMARY KEY, P_id CHAR(10), date INTEGER, cat1 CHAR(2) NOT NULL, " +
                "cat2 CHAR(2) NOT NULL, cat3 CHAR(2) NOT NULL, cat4 CHAR(2) NOT NULL, " +
                "cat5 CHAR(2) NOT NULL, cat6 CHAR(2) NOT NULL, cat7 CHAR(2) NOT NULL, " +
                "cat8 CHAR(2) NOT NULL, catSum CHAR(3) NOT NULL, mrc CHAR(2) NOT NULL, " +
                "acuteExac CHAR(1) NOT NULL, id CHAR(5), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS PmExposure" +
                "(PE_id CHAR(32) PRIMARY KEY, P_id CHAR(10), date INTEGER NOT NULL, exposure VARCHAR(10) NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS AppInfo" +
                "(AI_id CHAR(32) PRIMARY KEY, date INTEGER, P_id CHAR(10),type INTEGER, id CHAR(5), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS AppAttachment" +
                "(AA_id CHAR(32) PRIMARY KEY, name CHAR(10), P_id CHAR(10), AI_id CHAR(32), dir VARCHAR(50), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MedicineRegular" +
                "(MR_id CHAR(32) PRIMARY KEY, regular CHAR(2), P_id CHAR(10), date INTEGER, id CHAR(5), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MedicineChange" +
                "(MC_id CHAR(32) PRIMARY KEY, change CHAR(2), P_id CHAR(10), date INTEGER, id CHAR(5), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MedicineRecord" +
                "(MR_id CHAR(32) PRIMARY KEY, MC_id CHAR(32), medicine VARCHAR(20), state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MedicineAttachment" +
                "(MA_id CHAR(32) PRIMARY KEY, name CHAR(10), P_id CHAR(10), MC_id CHAR(32), dir VARCHAR(50), state INTEGER, sign INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TrackInfo" +
                "(TI_id CHAR(32) PRIMARY KEY, P_id CHAR(10), date INTEGER NOT NULL, name CHAR(32) NOT NULL, dir VARCHAR(50) NOT NULL, state INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS UploadInfo" +
                "(UI_id CHAR(32) PRIMARY KEY, P_id CHAR(10), catMrc INTEGER, pm INTEGER, appInfo INTEGER," +
                "medicineReg INTEGER, medicineCha INTEGER, trackInfo INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Message" +
                "(Ms_id INTEGER PRIMARY KEY, message CHAR(32), state INTEGER)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DBOpenHelper", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS CAT_MRC");
        db.execSQL("DROP TABLE IF EXISTS PmExposure");
        db.execSQL("DROP TABLE IF EXISTS AppInfo");
        db.execSQL("DROP TABLE IF EXISTS AppAttachment");
        db.execSQL("DROP TABLE IF EXISTS MedicineRegular");
        db.execSQL("DROP TABLE IF EXISTS MedicineChange");
        db.execSQL("DROP TABLE IF EXISTS MedicineRecord");
        db.execSQL("DROP TABLE IF EXISTS MedicineAttachment");
        db.execSQL("DROP TABLE IF EXISTS TrackInfo");
        db.execSQL("DROP TABLE IF EXISTS UploadInfo");
        db.execSQL("DROP TABLE IF EXISTS Message");
        onCreate(db);
    }
}