package com.example.administrator.project_v11;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    private final static String TAG = "DBManager";

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }
    public void delete(){
        helper.onUpgrade(db, 1, 2);
    }

    public void addMessage(Messagems msg){
        try{
            db.execSQL("INSERT INTO Message VALUES(?, ?, ?)", new Object[]{msg.getId(), msg.getMessage(), msg.getState()});
        }catch (Exception e){

        }
    }
    public void addUser(List<User> users){
        try {
            for (User user : users) {
                db.execSQL("INSERT INTO User VALUES(?, ?)", new Object[]{user.getP_id(), user.getDate()});
            }
        } catch(Exception e) {
        }
    }

    public void addAppinfo(List<AppInfo> appInfos){
        try {
            for (AppInfo appInfo : appInfos) {
                db.execSQL("INSERT INTO AppInfo VALUES(?, ?, ?, ?, ?, ?)", new Object[]{appInfo.getAi_id(), appInfo.getDate(),
                        appInfo.getP_id(), appInfo.getType(), appInfo.getId(), appInfo.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addCat_Mrc(List<CAT_MRC> cat_mrcs) {
        try {
            for (CAT_MRC cat_mrc : cat_mrcs) {
                db.execSQL("INSERT INTO CAT_MRC VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{cat_mrc.getCm_id(), cat_mrc.getP_id(), cat_mrc.getDate(),
                                cat_mrc.getCat1(), cat_mrc.getCat2(), cat_mrc.getCat3(), cat_mrc.getCat4(),
                                cat_mrc.getCat5(), cat_mrc.getCat6(), cat_mrc.getCat7(), cat_mrc.getCat8(),
                                cat_mrc.getCatsum(), cat_mrc.getMrc(), cat_mrc.getAcuteExac(), cat_mrc.getId(), cat_mrc.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addMedicineRegular(List<MedicineRegular> medicineRegulars){
        try {
            for (MedicineRegular medicineRegular : medicineRegulars) {
                db.execSQL("INSERT INTO MedicineRegular VALUES(?, ?, ?, ?, ?, ?)", new Object[]{medicineRegular.getMr_id(), medicineRegular.getRegular(),
                medicineRegular.getP_id(), medicineRegular.getDate(), medicineRegular.getId(), medicineRegular.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addMedicineChange(List<MedicineChange> medicineChanges){
        try {
            for (MedicineChange medicineChange : medicineChanges) {
                db.execSQL("INSERT INTO MedicineChange VALUES(?, ?, ?, ?, ?, ?)", new Object[]{medicineChange.getMc_id(), medicineChange.getChange(),
                        medicineChange.getP_id(), medicineChange.getDate(), medicineChange.getId(), medicineChange.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addPmExposure(List<PmExposure> pmexposures) {
        try {
            for (PmExposure pmexposure : pmexposures) {
                db.execSQL("INSERT INTO PmExposure VALUES(?, ?, ?, ?)",
                        new Object[]{pmexposure.getPe_id(), pmexposure.getP_id(), pmexposure.getDate(), pmexposure.getExposure()});
            }
        } catch(Exception e) {
        }
    }

    public void addAppAttachment(List<AppAttachment> appAttachments) {
        try {
            for (AppAttachment appAttachment : appAttachments) {
                db.execSQL("INSERT INTO AppAttachment VALUES(?, ?, ?, ?, ?, ?)",
                        new Object[]{appAttachment.getAa_id(), appAttachment.getName(), appAttachment.getP_id(), appAttachment.getAi_id(),
                                appAttachment.getDir(), appAttachment.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addMedicineAttachment(List<MedicineAttachment> medicineAttachments) {
        try {
            for (MedicineAttachment medicineAttachment : medicineAttachments) {
                db.execSQL("INSERT INTO MedicineAttachment VALUES(?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{medicineAttachment.getMa_id(), medicineAttachment.getName(), medicineAttachment.getP_id(), medicineAttachment.getMc_id(),
                                medicineAttachment.getDir(), medicineAttachment.getState(), medicineAttachment.getSign()});
            }
        } catch(Exception e) {
        }
    }

    public void addMedicineRecord(List<MedicineRecord> medicineRecords) {
        try {
            for (MedicineRecord medicineRecord : medicineRecords) {
                db.execSQL("INSERT INTO MedicineRecord VALUES(?, ?, ?, ?)",
                        new Object[]{medicineRecord.getMr_id(), medicineRecord.getMc_id(), medicineRecord.getMedicine(), medicineRecord.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addTrackInfo(List<TrackInfo> trackInfos) {
        try {
            for (TrackInfo trackInfo : trackInfos) {
                db.execSQL("INSERT INTO TrackInfo VALUES(?, ?, ?, ?, ?, ?)",
                        new Object[]{trackInfo.getTi_id(), trackInfo.getP_id(), trackInfo.getDate(), trackInfo.getName(), trackInfo.getDir(), trackInfo.getState()});
            }
        } catch(Exception e) {
        }
    }

    public void addUploadInfo(List<UploadInfo> uploadInfos) {
        try {
            for (UploadInfo uploadInfo : uploadInfos) {
                db.execSQL("INSERT INTO UploadInfo VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        new Object[]{uploadInfo.getUi_id(), uploadInfo.getP_id(), uploadInfo.getCatMrc(), uploadInfo.getPm(), uploadInfo.getAppInfo(),
                        uploadInfo.getMedicineReg(), uploadInfo.getMedicineCha(), uploadInfo.getTrackInfo()});
            }
        } catch(Exception e) {
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<CAT_MRC> queryToCat_Mrc(int i) {
        ArrayList<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
        Cursor c = queryTodayCAT_MRC(i);
        while (c.moveToNext()){
            CAT_MRC cat_mrc = new CAT_MRC(c.getString(c.getColumnIndex("CM_id")), c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")),
                    c.getString(c.getColumnIndex("cat1")),  c.getString(c.getColumnIndex("cat2")),  c.getString(c.getColumnIndex("cat3")),
                    c.getString(c.getColumnIndex("cat4")),  c.getString(c.getColumnIndex("cat5")),  c.getString(c.getColumnIndex("cat6")),
                    c.getString(c.getColumnIndex("cat7")),  c.getString(c.getColumnIndex("cat8")),  c.getString(c.getColumnIndex("catSum")),
                    c.getString(c.getColumnIndex("mrc")),  c.getString(c.getColumnIndex("acuteExac")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            cat_mrcs.add(cat_mrc);
        }
        c.close();
        return cat_mrcs;
    }

    public List<AppInfo> queryToAppInfo(int i) {
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        Cursor c = queryTodayAppinfo(i);
        while (c.moveToNext()){
            AppInfo appInfo = new AppInfo(c.getString(c.getColumnIndex("AI_id")), c.getLong(c.getColumnIndex("date")),
                    c.getString(c.getColumnIndex("P_id")), c.getInt(c.getColumnIndex("type")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            appInfos.add(appInfo);
        }
        c.close();
        return appInfos;
    }

    public List<AppAttachment> queryToAppAttachment(String i) {
        ArrayList<AppAttachment> appAttachments = new ArrayList<AppAttachment>();
        Cursor c = queryTodayAppAttachment(i);
        while (c.moveToNext()){
            AppAttachment appAttachment = new AppAttachment(c.getString(c.getColumnIndex("AA_id")), c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("P_id")), c.getString(c.getColumnIndex("AI_id")), c.getString(c.getColumnIndex("dir")), c.getInt(c.getColumnIndex("state")));
            appAttachments.add(appAttachment);
        }
        c.close();
        return appAttachments;
    }

    public List<MedicineAttachment> queryToMedicineAttachment(int i) {
        ArrayList<MedicineAttachment> medicineAttachments = new ArrayList<MedicineAttachment>();
        Cursor c = queryTodayMedicineAttachment(i);
        while (c.moveToNext()){
            MedicineAttachment medicineAttachment = new MedicineAttachment(c.getString(c.getColumnIndex("MA_id")), c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("P_id")),  c.getString(c.getColumnIndex("MC_id")),  c.getString(c.getColumnIndex("dir")),
                    c.getInt(c.getColumnIndex("state")), c.getInt(c.getColumnIndex("sign")));
            medicineAttachments.add(medicineAttachment);
        }
        c.close();
        return medicineAttachments;
    }

    public List<MedicineRegular> queryToMedicineRegular(int i) {
        ArrayList<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
        Cursor c = queryTodayMedicineRegular(i);
        while (c.moveToNext()){
            MedicineRegular medicineRegular = new MedicineRegular(c.getString(c.getColumnIndex("MR_id")), c.getString(c.getColumnIndex("regular")),
                    c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            medicineRegulars.add(medicineRegular);
        }
        c.close();
        return medicineRegulars;
    }

    public List<MedicineChange> queryToMedicineChange(int i) {
        ArrayList<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
        Cursor c = queryTodayMedicineChanger(i);
        while (c.moveToNext()) {
            MedicineChange medicineChange = new MedicineChange(c.getString(c.getColumnIndex("MC_id")), c.getString(c.getColumnIndex("change")),
                    c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            medicineChanges.add(medicineChange);
        }
        c.close();
        return medicineChanges;
    }

    public List<MedicineRecord> queryToMedicineRecord(int i) {
        ArrayList<MedicineRecord> medicineRecords = new ArrayList<MedicineRecord>();
        Cursor c = queryTodayMedicineRecord(i);
        while (c.moveToNext()){
            MedicineRecord medicineRecord = new MedicineRecord(c.getString(c.getColumnIndex("MR_id")), c.getString(c.getColumnIndex("MC_id")),
                    c.getString(c.getColumnIndex("medicine")), c.getInt(c.getColumnIndex("state")));
            medicineRecords.add(medicineRecord);
        }
        c.close();
        return medicineRecords;
    }

    public List<UploadInfo> queryToUploadInfo(int i) {
        ArrayList<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
        Cursor c = queryTodayUploadInfo(i);
        while (c.moveToNext()){
            UploadInfo uploadInfo = new UploadInfo(c.getString(c.getColumnIndex("UI_id")), c.getString(c.getColumnIndex("P_id")),
                    c.getLong(c.getColumnIndex("catMrc")), c.getLong(c.getColumnIndex("pm")), c.getLong(c.getColumnIndex("appInfo")),
                    c.getLong(c.getColumnIndex("medicineReg")), c.getLong(c.getColumnIndex("medicineCha")), c.getLong(c.getColumnIndex("trackInfo")));
            uploadInfos.add(uploadInfo);
        }
        c.close();
        return uploadInfos;
    }

    public List<TrackInfo> queryToTrackInfo(int i) {
        ArrayList<TrackInfo> trackInfos = new ArrayList<TrackInfo>();
        Cursor c = queryTodayTrackInfo(i);
        while (c.moveToNext()){
            TrackInfo trackInfo = new TrackInfo(c.getString(c.getColumnIndex("TI_id")), c.getString(c.getColumnIndex("P_id")),
                    c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("dir")), c.getInt(c.getColumnIndex("state")));
            trackInfos.add(trackInfo);
        }
        c.close();
        return trackInfos;
    }
    public List<Messagems> queryToMessage(){
        ArrayList<Messagems> messages = new ArrayList<Messagems>();
        Cursor c = queryTodayMessage();
        while (c.moveToNext()) {
            Messagems message = new Messagems(c.getLong(c.getColumnIndex("Ms_id")), c.getString(c.getColumnIndex("message")),
                    c.getInt(c.getColumnIndex("state")));
            messages.add(message);
        }
        c.close();
        return messages;
    }
/////////////////////////////////////////////////////////////
    public void deleteTodayCAT_MRC(int i){
        List<CAT_MRC> cat_mrcs = queryToCat_Mrc(i);
        try{
            for(CAT_MRC cat_mrc : cat_mrcs){
                db.delete("CAT_MRC", "CM_id = ?", new String[]{cat_mrc.getCm_id()});
            }
        }catch (Exception e){

        }
    }
    public void deleteTodayMessage(){
        List<Messagems> messages = queryToMessage();
        try{
            for(Messagems message : messages){
                db.delete("Message", "state = ?", new String[]{String.valueOf(message.getState())});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayAppinfo(int i, int type){
        List<AppInfo> appInfos = queryToAppInfo(i);
        try{
            for(AppInfo appInfo : appInfos){
                if(appInfo.getType() == type)
                    db.delete("AppInfo", "AI_id = ?", new String[]{appInfo.getAi_id()});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayAppAttachment(String i){
        List<AppAttachment> appAttachments = queryToAppAttachment(i);
        try{
            for(AppAttachment appAttachment : appAttachments){
                db.delete("AppAttachment", "AA_id = ?", new String[]{appAttachment.getAa_id()});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayMedicineAttachment(int i){
        List<MedicineAttachment> medicineAttachments = queryToMedicineAttachment(i);
        try{
            for(MedicineAttachment medicineAttachment : medicineAttachments){
                db.delete("MedicineAttachment", "MA_id = ?", new String[]{medicineAttachment.getMa_id()});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayMedicineRegular(int i){
        List<MedicineRegular> medicineRegulars = queryToMedicineRegular(i);
        try{
            for(MedicineRegular medicineRegular : medicineRegulars){
                db.delete("MedicineRegular", "MR_id = ?", new String[]{medicineRegular.getMr_id()});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayMedicineChange(int i){
        List<MedicineChange> medicineChanges = queryToMedicineChange(i);
        try{
            for(MedicineChange medicineChange : medicineChanges){
                db.delete("MedicineChange", "MC_id = ?", new String[]{medicineChange.getMc_id()});
            }
        }catch (Exception e){

        }
    }

    public void deleteTodayMedicineRecord(int i){
        List<MedicineRecord> medicineRecords = queryToMedicineRecord(i);
        try{
            for(MedicineRecord medicineRecord : medicineRecords){
                db.delete("MedicineRecord", "MR_id = ?", new String[]{medicineRecord.getMr_id()});
            }
        }catch (Exception e){

        }
    }
    public void deleteTodayUploadInfo(int i){
        List<UploadInfo> uploadInfos = queryToUploadInfo(i);
        try {
            for(UploadInfo uploadInfo : uploadInfos){
                db.delete("UploadInfo", "UI_id = ?", new String[]{uploadInfo.getUi_id()});
            }
        }catch (Exception e){

        }
    }
    public void deleteTodayTrackInfo(int i){
        List<TrackInfo> trackInfos = queryToTrackInfo(i);
        try {
            for(TrackInfo trackInfo : trackInfos){
                db.delete("TrackInfo", "TI_id = ?", new String[]{trackInfo.getTi_id()});
            }
        }catch (Exception e){

        }
    }


///////////////////////////////////////////////////////////////
    public List<User> query_User(){
        ArrayList<User> users = new ArrayList<User>();
        Cursor c = queryTheUser();
        while (c.moveToNext()) {
            User user = new User(c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")));
            users.add(user);
        }
        c.close();
        return users;
    }
    public List<Messagems> query_Message(){
        ArrayList<Messagems> messages = new ArrayList<Messagems>();
        Cursor c = queryTheMessage();
        while (c.moveToNext()) {
            Messagems message = new Messagems(c.getLong(c.getColumnIndex("Ms_id")), c.getString(c.getColumnIndex("message")),
                    c.getInt(c.getColumnIndex("state")));
            messages.add(message);
        }
        c.close();
        return messages;
    }

    public List<AppInfo> query_Appinfo(){
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();
        Cursor c = queryTheAppinfo();
        while (c.moveToNext()) {
                AppInfo appInfo = new AppInfo(c.getString(c.getColumnIndex("AI_id")), c.getLong(c.getColumnIndex("date")),
                        c.getString(c.getColumnIndex("P_id")), c.getInt(c.getColumnIndex("type")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            appInfos.add(appInfo);
        }
        c.close();
        return appInfos;
    }

    public List<MedicineRegular> query_MedicineRegular(){
        ArrayList<MedicineRegular> medicineRegulars = new ArrayList<MedicineRegular>();
        Cursor c = queryTheMedicineRegular();
        while (c.moveToNext()) {
            MedicineRegular medicineRegular = new MedicineRegular(c.getString(c.getColumnIndex("MR_id")), c.getString(c.getColumnIndex("regular")),
                    c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            medicineRegulars.add(medicineRegular);
        }
        c.close();
        return medicineRegulars;
    }

    public List<MedicineChange> query_MedicineChange(){
        ArrayList<MedicineChange> medicineChanges = new ArrayList<MedicineChange>();
        Cursor c = queryTheMedicineChange();
        while (c.moveToNext()) {
            MedicineChange medicineChange = new MedicineChange(c.getString(c.getColumnIndex("MC_id")), c.getString(c.getColumnIndex("change")),
                    c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            medicineChanges.add(medicineChange);
        }
        c.close();
        return medicineChanges;
    }

    public List<PmExposure> query_PmExposure() {
        ArrayList<PmExposure> pmExposures = new ArrayList<PmExposure>();
        Cursor c = queryThePmExposure();
        while (c.moveToNext()) {
            PmExposure pmExposure = new PmExposure(c.getString(c.getColumnIndex("PE_id")), c.getString(c.getColumnIndex("P_id")),
                    c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("exposure")));
            pmExposures.add(pmExposure);
        }
        c.close();
        return pmExposures;
    }

    public List<AppAttachment> query_AppAttachment() {
        ArrayList<AppAttachment> appAttachments = new ArrayList<AppAttachment>();
        Cursor c = queryTheAppAttachment();
        while (c.moveToNext()) {
            AppAttachment appAttachment = new AppAttachment(c.getString(c.getColumnIndex("AA_id")), c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("P_id")), c.getString(c.getColumnIndex("AI_id")), c.getString(c.getColumnIndex("dir")), c.getInt(c.getColumnIndex("state")));
            appAttachments.add(appAttachment);
        }
        c.close();
        return appAttachments;
    }

    public List<MedicineAttachment> query_MedicineAttachment() {
        ArrayList<MedicineAttachment> medicineAttachments = new ArrayList<MedicineAttachment>();
        Cursor c = queryTheMedicineAttachment();
        while (c.moveToNext()) {
            MedicineAttachment medicineAttachment = new MedicineAttachment(c.getString(c.getColumnIndex("MA_id")), c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("P_id")), c.getString(c.getColumnIndex("MC_id")), c.getString(c.getColumnIndex("dir")),
                    c.getInt(c.getColumnIndex("state")), c.getInt(c.getColumnIndex("sign")));
            medicineAttachments.add(medicineAttachment);
        }
        c.close();
        return medicineAttachments;
    }

    public List<MedicineRecord> query_MedicineRecord() {
        ArrayList<MedicineRecord> medicineRecords = new ArrayList<MedicineRecord>();
        Cursor c = queryTheMedicineRecord();
        while (c.moveToNext()) {
            MedicineRecord medicineRecord = new MedicineRecord(c.getString(c.getColumnIndex("MR_id")), c.getString(c.getColumnIndex("MC_id")),
                    c.getString(c.getColumnIndex("medicine")), c.getInt(c.getColumnIndex("state")));
            medicineRecords.add(medicineRecord);
        }
        c.close();
        return medicineRecords;
    }

    public List<TrackInfo> query_TrackInfo() {
        ArrayList<TrackInfo> trackInfos = new ArrayList<TrackInfo>();
        Cursor c = queryTheTrackInfo();
        while (c.moveToNext()) {
            TrackInfo trackInfo = new TrackInfo(c.getString(c.getColumnIndex("TI_id")), c.getString(c.getColumnIndex("P_id")),
                    c.getLong(c.getColumnIndex("date")), c.getString(c.getColumnIndex("name")), c.getString(c.getColumnIndex("dir")), c.getInt(c.getColumnIndex("state")));
            trackInfos.add(trackInfo);
        }
        c.close();
        return trackInfos;
    }

    public List<UploadInfo> query_UploadInfo() {
        ArrayList<UploadInfo> uploadInfos = new ArrayList<UploadInfo>();
        Cursor c = queryTheUploadInfo();
        while (c.moveToNext()) {
            UploadInfo uploadInfo = new UploadInfo(c.getString(c.getColumnIndex("UI_id")), c.getString(c.getColumnIndex("P_id")),
                    c.getLong(c.getColumnIndex("catMrc")), c.getLong(c.getColumnIndex("pm")), c.getLong(c.getColumnIndex("appInfo")),
                    c.getLong(c.getColumnIndex("medicineReg")), c.getLong(c.getColumnIndex("medicineCha")), c.getLong(c.getColumnIndex("trackInfo")));
            uploadInfos.add(uploadInfo);
        }
        c.close();
        return uploadInfos;
    }

    public List<CAT_MRC> query_Cat_Mrc() {
        ArrayList<CAT_MRC> cat_mrcs = new ArrayList<CAT_MRC>();
        Cursor c = queryTheCAT_MRC();
        while (c.moveToNext()) {
            CAT_MRC cat_mrc = new CAT_MRC(c.getString(c.getColumnIndex("CM_id")), c.getString(c.getColumnIndex("P_id")), c.getLong(c.getColumnIndex("date")),
                    c.getString(c.getColumnIndex("cat1")),  c.getString(c.getColumnIndex("cat2")),  c.getString(c.getColumnIndex("cat3")),
                    c.getString(c.getColumnIndex("cat4")),  c.getString(c.getColumnIndex("cat5")),  c.getString(c.getColumnIndex("cat6")),
                    c.getString(c.getColumnIndex("cat7")),  c.getString(c.getColumnIndex("cat8")),  c.getString(c.getColumnIndex("catSum")),
                    c.getString(c.getColumnIndex("mrc")),  c.getString(c.getColumnIndex("acuteExac")), c.getString(c.getColumnIndex("id")), c.getInt(c.getColumnIndex("state")));
            cat_mrcs.add(cat_mrc);
        }
        c.close();
        return cat_mrcs;
    }


///////////////////////////////////////////////////////////////
    public Cursor queryTheUser() {
        Cursor c = db.rawQuery("SELECT * FROM User ", null);
        return c;
    }
    public Cursor queryTheMessage() {
        Cursor c = db.rawQuery("SELECT * FROM Message ", null);
        return c;
    }
    public Cursor queryTheMedicineRegular() {
        Cursor c = db.rawQuery("SELECT * FROM MedicineRegular ", null);
        return c;
    }
    public Cursor queryTheMedicineChange() {
        Cursor c = db.rawQuery("SELECT * FROM MedicineChange ", null);
        return c;
    }
    public Cursor queryTheCAT_MRC() {
        Cursor c = db.rawQuery("SELECT * FROM CAT_MRC ", null);
        return c;
    }
    public Cursor queryThePmExposure() {
        Cursor c = db.rawQuery("SELECT * FROM PmExposure ", null);
        return c;
    }
    public Cursor queryTheAppinfo() {
        Cursor c = db.rawQuery("SELECT * FROM AppInfo ", null);
        return c;
    }
    public Cursor queryTheAppAttachment() {
        Cursor c = db.rawQuery("SELECT * FROM AppAttachment ", null);
        return c;
    }
    public Cursor queryTheMedicineAttachment() {
        Cursor c = db.rawQuery("SELECT * FROM MedicineAttachment ", null);
        return c;
    }
    public Cursor queryTheMedicineRecord() {
        Cursor c = db.rawQuery("SELECT * FROM MedicineRecord ", null);
        return c;
    }
    public Cursor queryTheTrackInfo() {
        Cursor c = db.rawQuery("SELECT * FROM TrackInfo ", null);
        return c;
    }
    public Cursor queryTheUploadInfo() {
        Cursor c = db.rawQuery("SELECT * FROM UploadInfo ", null);
        return c;
    }
///////////////////////////////////////////////////////////////////////////////////
    public Cursor queryTodayCAT_MRC(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM CAT_MRC WHERE " +
                "date =?",
                new String[] {String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayAppinfo(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM AppInfo WHERE " +
                        "date =?",
                new String[] {String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayAppAttachment(String i) {
        /*long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;*/
        Cursor c = db.rawQuery("SELECT * FROM AppAttachment WHERE " +
                        "AI_id =?",
                new String[] {i});
        return c;
    }
    public Cursor queryTodayMedicineAttachment(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM MedicineAttachment WHERE " +
                        "MC_id =?",
                new String[] {Data.getP_id()+String.valueOf(startTime)});
        return c;
    }

    public Cursor queryTodayMedicineRegular(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM MedicineRegular WHERE " +
                        "date =?",
                new String[] {String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayMedicineChanger(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM MedicineChange WHERE " +
                        "date =?",
                new String[] {String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayMedicineRecord(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM MedicineRecord WHERE " +
                        "MC_id =?",
                new String[] {Data.getP_id()+String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayUploadInfo(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM UploadInfo WHERE " +
                        "UI_id =?",
                new String[] {Data.getP_id()+String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayTrackInfo(int i) {
        long startTime = Data.getDate() + i * 24 * 60 * 60 * 1000;
        Cursor c = db.rawQuery("SELECT * FROM TrackInfo WHERE " +
                        "TI_id =?",
                new String[] {Data.getP_id()+String.valueOf(startTime)});
        return c;
    }
    public Cursor queryTodayMessage() {
        Cursor c = db.rawQuery("SELECT * FROM Message WHERE " +
                        "state =?",
                new String[] {String.valueOf(0)});
        return c;
    }


    public void closeDB() {
        helper.close();
        db.close();
    }

}
