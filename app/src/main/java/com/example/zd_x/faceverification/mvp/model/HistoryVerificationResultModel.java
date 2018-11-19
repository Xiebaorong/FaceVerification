package com.example.zd_x.faceverification.mvp.model;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryVerificationResultModel {
    @Id(autoincrement = true)
    private Long id;
    private String verificationTime; //比对时间
    private String imageId;//人像ID
    private String faceBase64;//人像编码
    private boolean isVerification;//对比通过标记
    private int total;//对比总数
    @Convert(converter = CompareResultsBeanConverter.class, columnType = String.class)
    private List<CompareResultsBean> compareResults;//对比人像详细信息


    @Generated(hash = 1184740486)
    public HistoryVerificationResultModel(Long id, String verificationTime, String imageId, String faceBase64, boolean isVerification,
            int total, List<CompareResultsBean> compareResults) {
        this.id = id;
        this.verificationTime = verificationTime;
        this.imageId = imageId;
        this.faceBase64 = faceBase64;
        this.isVerification = isVerification;
        this.total = total;
        this.compareResults = compareResults;
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


    public List<CompareResultsBean> getCompareResults() {
        return this.compareResults;
    }


    public void setCompareResults(List<CompareResultsBean> compareResults) {
        this.compareResults = compareResults;
    }


    public static class CompareResultsBeanConverter implements PropertyConverter<List<CompareResultsBean>, String> {
        @Override
        public List<CompareResultsBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            List<String> list_str = Arrays.asList(databaseValue.split(","));
            List<CompareResultsBean> list_transport = new ArrayList<>();
            for (String s : list_str) {
                list_transport.add(new Gson().fromJson(s, CompareResultsBean.class));
            }
            return list_transport;
        }

        @Override
        public String convertToDatabaseValue(List<CompareResultsBean> arrays) {
            if (arrays == null) {
                return null;
            } else {
                StringBuilder sb = new StringBuilder();
                for (CompareResultsBean array : arrays) {
                    String str = new Gson().toJson(array);
                    sb.append(str);
                    sb.append(",");
                }
                return sb.toString();
            }
        }
    }
}
