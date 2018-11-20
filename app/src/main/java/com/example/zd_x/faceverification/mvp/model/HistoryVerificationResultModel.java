package com.example.zd_x.faceverification.mvp.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;

import com.example.zd_x.faceverification.dao.DaoSession;
import com.example.zd_x.faceverification.dao.CompareResultsBeanDao;
import com.example.zd_x.faceverification.dao.HistoryVerificationResultModelDao;

@Entity
public class HistoryVerificationResultModel {
    @Id(autoincrement = true)
    private Long id;
    private String verificationTime; //比对时间
    private String imageId;//人像ID
    private String faceBase64;//人像编码
    private boolean isVerification;//对比通过标记
    private int total;//对比总数
    @ToMany(referencedJoinProperty = "historyId")
    private List<CompareResultsBean> compareResults;//对比人像详细信息
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1613370791)
    private transient HistoryVerificationResultModelDao myDao;
    @Generated(hash = 1814061965)
    public HistoryVerificationResultModel(Long id, String verificationTime,
            String imageId, String faceBase64, boolean isVerification, int total) {
        this.id = id;
        this.verificationTime = verificationTime;
        this.imageId = imageId;
        this.faceBase64 = faceBase64;
        this.isVerification = isVerification;
        this.total = total;
    }
    @Generated(hash = 327808734)
    public HistoryVerificationResultModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getVerificationTime() {
        return this.verificationTime;
    }
    public void setVerificationTime(String verificationTime) {
        this.verificationTime = verificationTime;
    }
    public String getImageId() {
        return this.imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getFaceBase64() {
        return this.faceBase64;
    }
    public void setFaceBase64(String faceBase64) {
        this.faceBase64 = faceBase64;
    }
    public boolean getIsVerification() {
        return this.isVerification;
    }
    public void setIsVerification(boolean isVerification) {
        this.isVerification = isVerification;
    }
    public int getTotal() {
        return this.total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 361980723)
    public List<CompareResultsBean> getCompareResults() {
        if (compareResults == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CompareResultsBeanDao targetDao = daoSession.getCompareResultsBeanDao();
            List<CompareResultsBean> compareResultsNew = targetDao
                    ._queryHistoryVerificationResultModel_CompareResults(id);
            synchronized (this) {
                if (compareResults == null) {
                    compareResults = compareResultsNew;
                }
            }
        }
        return compareResults;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 340587876)
    public synchronized void resetCompareResults() {
        compareResults = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1365178912)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null
                ? daoSession.getHistoryVerificationResultModelDao() : null;
    }
    

}
