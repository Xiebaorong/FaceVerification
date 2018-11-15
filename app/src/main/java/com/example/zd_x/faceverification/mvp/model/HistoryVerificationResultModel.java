package com.example.zd_x.faceverification.mvp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

@Entity
public class HistoryVerificationResultModel {
    @Id(autoincrement = true)
    private Long id;
    private String verificationTime;
    private String imageId;
    private String faceBase64;
    private boolean isVerification;
    private List<CompareResultsBean> compareResults;

    public static class CompareResultsBean {
        private IpcBlacklistBean ipcBlacklist;
        private int similarity;
    }

    public static class IpcBlacklistBean {
        private String compareID;
        private String blacklistID;
        private String zfsPath;
        private Object imageBase64;
        private String name;
        private String sex;
        private long birthday;
        private String nation;
        private int dubious;
        private String note;
        private String repoID;
    }

}
