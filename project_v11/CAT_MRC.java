package com.example.administrator.project_v11;

import java.util.Date;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class CAT_MRC {

    private String cm_id;
    private String p_id;
    private Long date;
    private String cat1;
    private String cat2;
    private String cat3;
    private String cat4;
    private String cat5;
    private String cat6;
    private String cat7;
    private String cat8;
    private String catsum;
    private String mrc;
    private String acuteExac;
    private String id;
    private int state;

    public CAT_MRC(String cm_id, String p_id, Long date, String cat1, String cat2, String cat3, String cat4, String cat5, String cat6,
                   String cat7, String cat8, String catsum, String mrc, String acuteExac, String id, int state) {
        this.cm_id = cm_id;
        this.p_id = p_id;
        this.date = date;
        this.cat1 = cat1;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.cat4 = cat4;
        this.cat5 = cat5;
        this.cat6 = cat6;
        this.cat7 = cat7;
        this.cat8 = cat8;
        this.catsum = catsum;
        this.mrc = mrc;
        this.acuteExac = acuteExac;
        this.id = id;
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCm_id() {
        return cm_id;
    }

    public void setCm_id(String cm_id) {
        this.cm_id = cm_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getCat1() {
        return cat1;
    }

    public void setCat1(String cat1) {
        this.cat1 = cat1;
    }

    public String getCat2() {
        return cat2;
    }

    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public String getCat4() {
        return cat4;
    }

    public void setCat4(String cat4) {
        this.cat4 = cat4;
    }

    public String getCat5() {
        return cat5;
    }

    public void setCat5(String cat5) {
        this.cat5 = cat5;
    }

    public String getCat6() {
        return cat6;
    }

    public void setCat6(String cat6) {
        this.cat6 = cat6;
    }

    public String getCat7() {
        return cat7;
    }

    public void setCat7(String cat7) {
        this.cat7 = cat7;
    }

    public String getCat8() {
        return cat8;
    }

    public void setCat8(String cat8) {
        this.cat8 = cat8;
    }

    public String getCatsum() {
        return catsum;
    }

    public void setCatsum(String catsum) {
        this.catsum = catsum;
    }

    public String getMrc() {
        return mrc;
    }

    public void setMrc(String mrc) {
        this.mrc = mrc;
    }

    public String getAcuteExac() {
        return acuteExac;
    }

    public void setAcuteExac(String acuteExac) {
        this.acuteExac = acuteExac;
    }
}
