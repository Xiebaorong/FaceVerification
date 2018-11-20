package com.example.zd_x.faceverification.mvp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class CompareResultsBean {
    //ID主键自增
    @Id
    private Long id;
    private Long  historyId;
    private String compareID;
    private String blacklistID;
    private String zfsPath;
    private String imageBase64;
    private String name;
    private String sex;
    private long birthday;
    private String nation;
    private int dubious;
    private String note;
    private int similarity;//相似度
    @Generated(hash = 1385249942)
    public CompareResultsBean(Long id, Long historyId, String compareID,
            String blacklistID, String zfsPath, String imageBase64, String name,
            String sex, long birthday, String nation, int dubious, String note,
            int similarity) {
        this.id = id;
        this.historyId = historyId;
        this.compareID = compareID;
        this.blacklistID = blacklistID;
        this.zfsPath = zfsPath;
        this.imageBase64 = imageBase64;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.nation = nation;
        this.dubious = dubious;
        this.note = note;
        this.similarity = similarity;
    }
    @Generated(hash = 1971530714)
    public CompareResultsBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getHistoryId() {
        return this.historyId;
    }
    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }
    public String getCompareID() {
        return this.compareID;
    }
    public void setCompareID(String compareID) {
        this.compareID = compareID;
    }
    public String getBlacklistID() {
        return this.blacklistID;
    }
    public void setBlacklistID(String blacklistID) {
        this.blacklistID = blacklistID;
    }
    public String getZfsPath() {
        return this.zfsPath;
    }
    public void setZfsPath(String zfsPath) {
        this.zfsPath = zfsPath;
    }
    public String getImageBase64() {
        return this.imageBase64;
    }
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public long getBirthday() {
        return this.birthday;
    }
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
    public String getNation() {
        return this.nation;
    }
    public void setNation(String nation) {
        this.nation = nation;
    }
    public int getDubious() {
        return this.dubious;
    }
    public void setDubious(int dubious) {
        this.dubious = dubious;
    }
    public String getNote() {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public int getSimilarity() {
        return this.similarity;
    }
    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }


}



