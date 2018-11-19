package com.example.zd_x.faceverification.mvp.model;

import java.util.List;

public class VerificationModel {

    /**
     * status : 0
     * message : 成功
     * out : {"ipcMetadata":{"imageID":"11010801010000000001022017032110101000009","deviceID":"11010801010000000001","entryTime":1517533260308,"createTime":1517533260308,"zfsPath":"192.168.104.3:8080@2018-02-02-0000_9664","deviceName":"摄像机4","isBlk":1,"zfsPersonPath":null,"snapshotID":"63866561-07b4-11e8-9a0a-b8ca3a958ee3"},"compareResults":[{"ipcBlacklist":{"compareID":"bd70d54f-0657-11e8-886f-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6117","imageBase64":null,"name":"test3","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"3"},"similarity":100},{"ipcBlacklist":{"compareID":"d1253c80-0657-11e8-875b-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6136","imageBase64":null,"name":"test2","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"3"},"similarity":98.26102},{"ipcBlacklist":{"compareID":"795d28f0-0657-11e8-ad89-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6041","imageBase64":null,"name":"test1","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"1"},"similarity":98.26102}],"total":3}
     */

    private int status;
    private String message;
    private OutBean out;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OutBean getOut() {
        return out;
    }

    public void setOut(OutBean out) {
        this.out = out;
    }

    public static class OutBean {
        /**
         * ipcMetadata : {"imageID":"11010801010000000001022017032110101000009","deviceID":"11010801010000000001","entryTime":1517533260308,"createTime":1517533260308,"zfsPath":"192.168.104.3:8080@2018-02-02-0000_9664","deviceName":"摄像机4","isBlk":1,"zfsPersonPath":null,"snapshotID":"63866561-07b4-11e8-9a0a-b8ca3a958ee3"}
         * compareResults : [{"ipcBlacklist":{"compareID":"bd70d54f-0657-11e8-886f-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6117","imageBase64":null,"name":"test3","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"3"},"similarity":100},{"ipcBlacklist":{"compareID":"d1253c80-0657-11e8-875b-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6136","imageBase64":null,"name":"test2","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"3"},"similarity":98.26102},{"ipcBlacklist":{"compareID":"795d28f0-0657-11e8-ad89-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6041","imageBase64":null,"name":"test1","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"1"},"similarity":98.26102}]
         * total : 3
         */

        private IpcMetadataBean ipcMetadata;
        private int total;
        private List<CompareResultsBean> compareResults;

        public IpcMetadataBean getIpcMetadata() {
            return ipcMetadata;
        }

        public void setIpcMetadata(IpcMetadataBean ipcMetadata) {
            this.ipcMetadata = ipcMetadata;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<CompareResultsBean> getCompareResults() {
            return compareResults;
        }

        public void setCompareResults(List<CompareResultsBean> compareResults) {
            this.compareResults = compareResults;
        }

        public static class IpcMetadataBean {
            /**
             * imageID : 11010801010000000001022017032110101000009
             * deviceID : 11010801010000000001
             * entryTime : 1517533260308
             * createTime : 1517533260308
             * zfsPath : 192.168.104.3:8080@2018-02-02-0000_9664
             * deviceName : 摄像机4
             * isBlk : 1
             * zfsPersonPath : null
             * snapshotID : 63866561-07b4-11e8-9a0a-b8ca3a958ee3
             */

            private String imageID;
            private String deviceID;
            private long entryTime;
            private long createTime;
            private String zfsPath;
            private String deviceName;
            private int isBlk;
            private Object zfsPersonPath;
            private String snapshotID;

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

            public long getEntryTime() {
                return entryTime;
            }

            public void setEntryTime(long entryTime) {
                this.entryTime = entryTime;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getZfsPath() {
                return zfsPath;
            }

            public void setZfsPath(String zfsPath) {
                this.zfsPath = zfsPath;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public int getIsBlk() {
                return isBlk;
            }

            public void setIsBlk(int isBlk) {
                this.isBlk = isBlk;
            }

            public Object getZfsPersonPath() {
                return zfsPersonPath;
            }

            public void setZfsPersonPath(Object zfsPersonPath) {
                this.zfsPersonPath = zfsPersonPath;
            }

            public String getSnapshotID() {
                return snapshotID;
            }

            public void setSnapshotID(String snapshotID) {
                this.snapshotID = snapshotID;
            }
        }


    }
}
