package com.example.zd_x.faceverification.mvp.model;

import java.util.List;

public class DetectionModel {

    /**
     * imageID : 1101 08010 1000 0000 0010 22017032110101000009
     * deviceID : 11010801010000000001
     * deviceName : 摄像机4
     * faceBase64 : imageBase64
     * skip : 0
     * limit : 3
     * strategy : SelectedRepos
     * repoIDs : ["1","3"]
     * threshold : 80
     */

    private String imageID;
    private String deviceID;
    private String deviceName;
    private String faceBase64;
    private int skip;
    private int limit;
    private String strategy;
    private int threshold;
    private List<String> repoIDs;

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getFaceBase64() {
        return faceBase64;
    }

    public void setFaceBase64(String faceBase64) {
        this.faceBase64 = faceBase64;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public List<String> getRepoIDs() {
        return repoIDs;
    }

    public void setRepoIDs(List<String> repoIDs) {
        this.repoIDs = repoIDs;
    }
}
