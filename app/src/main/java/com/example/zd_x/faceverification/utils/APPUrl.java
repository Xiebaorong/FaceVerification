package com.example.zd_x.faceverification.utils;

public class APPUrl {
    private static final String BASE_URL = "http://122.115.57.130";

    private static final String PORT_CODE = ":8080";
    public static final String SEND = BASE_URL+PORT_CODE + "/IPCBlkService/BlkCompare";
    public static final String IMAGE_IP = "/SmzQueryImg/jaxrs/Zfs/queryImg/";

    private static final String APP_UPDATE_URL ="http://122.115.57.131:8099";
    public static final String GET_UPDATE_PREFIX =APP_UPDATE_URL+ "/police/upgrade?";

}
