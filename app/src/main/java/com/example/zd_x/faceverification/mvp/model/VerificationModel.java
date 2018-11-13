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

        public static class CompareResultsBean {
            /**
             * ipcBlacklist : {"compareID":"bd70d54f-0657-11e8-886f-b8ca3a958ee3","blacklistID":"652122198610253236","zfsPath":"192.168.104.3:8080@2018-01-31-0002_6117","imageBase64":null,"name":"test3","sex":"男","birthday":530553600000,"nation":"汉","dubious":8,"note":"111111111","repoID":"3"}
             * similarity : 100
             */

            private IpcBlacklistBean ipcBlacklist;
            private int similarity;

            public IpcBlacklistBean getIpcBlacklist() {
                return ipcBlacklist;
            }

            public void setIpcBlacklist(IpcBlacklistBean ipcBlacklist) {
                this.ipcBlacklist = ipcBlacklist;
            }

            public int getSimilarity() {
                return similarity;
            }

            public void setSimilarity(int similarity) {
                this.similarity = similarity;
            }

            public static class IpcBlacklistBean {
                /**
                 * compareID : bd70d54f-0657-11e8-886f-b8ca3a958ee3
                 * blacklistID : 652122198610253236
                 * zfsPath : 192.168.104.3:8080@2018-01-31-0002_6117
                 * imageBase64 : null
                 * name : test3
                 * sex : 男
                 * birthday : 530553600000
                 * nation : 汉
                 * dubious : 8
                 * note : 111111111
                 * repoID : 3
                 */

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

                public String getCompareID() {
                    return compareID;
                }

                public void setCompareID(String compareID) {
                    this.compareID = compareID;
                }

                public String getBlacklistID() {
                    return blacklistID;
                }

                public void setBlacklistID(String blacklistID) {
                    this.blacklistID = blacklistID;
                }

                public String getZfsPath() {
                    return zfsPath;
                }

                public void setZfsPath(String zfsPath) {
                    this.zfsPath = zfsPath;
                }

                public Object getImageBase64() {
                    return imageBase64;
                }

                public void setImageBase64(Object imageBase64) {
                    this.imageBase64 = imageBase64;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public long getBirthday() {
                    return birthday;
                }

                public void setBirthday(long birthday) {
                    this.birthday = birthday;
                }

                public String getNation() {
                    return nation;
                }

                public void setNation(String nation) {
                    this.nation = nation;
                }

                public int getDubious() {
                    return dubious;
                }

                public void setDubious(int dubious) {
                    this.dubious = dubious;
                }

                public String getNote() {
                    return note;
                }

                public void setNote(String note) {
                    this.note = note;
                }

                public String getRepoID() {
                    return repoID;
                }

                public void setRepoID(String repoID) {
                    this.repoID = repoID;
                }
            }
        }
    }
}
