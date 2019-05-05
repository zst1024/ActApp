package com.example.administrator.project_v11;


/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class UploadInfo {
    private String ui_id;
    private String p_id;
    private Long catMrc;
    private Long pm;
    private Long appInfo;
    private Long medicineReg;
    private Long medicineCha;
    private Long trackInfo;

    public UploadInfo(String ui_id, String p_id, Long catMrc, Long pm, Long appInfo, Long medicineReg, Long medicineCha, Long trackInfo) {
        this.ui_id = ui_id;
        this.p_id = p_id;
        this.catMrc = catMrc;
        this.pm = pm;
        this.appInfo = appInfo;
        this.medicineReg = medicineReg;
        this.medicineCha = medicineCha;
        this.trackInfo = trackInfo;
    }

    public String getUi_id() {
        return ui_id;
    }

    public void setUi_id(String ui_id) {
        this.ui_id = ui_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public Long getCatMrc() {
        return catMrc;
    }

    public void setCatMrc(Long catMrc) {
        this.catMrc = catMrc;
    }

    public Long getPm() {
        return pm;
    }

    public void setPm(Long pm) {
        this.pm = pm;
    }

    public Long getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(Long appInfo) {
        this.appInfo = appInfo;
    }

    public Long getMedicineReg() {
        return medicineReg;
    }

    public void setMedicineReg(Long medicineReg) {
        this.medicineReg = medicineReg;
    }

    public Long getMedicineCha() {
        return medicineCha;
    }

    public void setMedicineCha(Long medicineCha) {
        this.medicineCha = medicineCha;
    }

    public Long getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(Long trackInfo) {
        this.trackInfo = trackInfo;
    }
}
