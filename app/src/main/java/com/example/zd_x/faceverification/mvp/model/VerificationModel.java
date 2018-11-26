package com.example.zd_x.faceverification.mvp.model;

import java.util.List;

public class VerificationModel {


    /**
     * status : 0
     * message : 成功
     * out : {"ipcMetadata":{"imageID":"11010801800007000001022018112310470019067","deviceID":"11010801800007000001","entryTime":1542941220323,"createTime":1542941220323,"zfsPath":"122.115.57.130:8080@2018-11-23-0000_104","deviceName":"1","isBlk":1,"zfsPersonPath":null,"snapshotID":"8f5338ed-085b-4f5b-9d68-1b4bc5dadfce","snapshotbodyID":null,"direction":null},"compareResults":[{"ipcBlacklist":{"compareID":"15dfff6d-ca26-4988-a6a7-2553ae3b5eb9","blacklistID":"6578","zfsPath":"122.115.57.130:8080@2018-11-23-0000_101","imageBase64":null,"name":"谢宝荣","sex":null,"birthday":null,"nation":"汉族","dubious":12,"note":null,"repoID":"75dd62c4-8eaf-4ab8-8d1f-7066e712b8ec"},"similarity":92.907974}],"total":1}
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
         * ipcMetadata : {"imageID":"11010801800007000001022018112310470019067","deviceID":"11010801800007000001","entryTime":1542941220323,"createTime":1542941220323,"zfsPath":"122.115.57.130:8080@2018-11-23-0000_104","deviceName":"1","isBlk":1,"zfsPersonPath":null,"snapshotID":"8f5338ed-085b-4f5b-9d68-1b4bc5dadfce","snapshotbodyID":null,"direction":null}
         * compareResults : [{"ipcBlacklist":{"compareID":"15dfff6d-ca26-4988-a6a7-2553ae3b5eb9","blacklistID":"6578","zfsPath":"122.115.57.130:8080@2018-11-23-0000_101","imageBase64":null,"name":"谢宝荣","sex":null,"birthday":null,"nation":"汉族","dubious":12,"note":null,"repoID":"75dd62c4-8eaf-4ab8-8d1f-7066e712b8ec"},"similarity":92.907974}]
         * total : 1
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
             * imageID : 11010801800007000001022018112310470019067
             * deviceID : 11010801800007000001
             * entryTime : 1542941220323
             * createTime : 1542941220323
             * zfsPath : 122.115.57.130:8080@2018-11-23-0000_104
             * deviceName : 1
             * isBlk : 1
             * zfsPersonPath : null
             * snapshotID : 8f5338ed-085b-4f5b-9d68-1b4bc5dadfce
             * snapshotbodyID : null
             * direction : null
             */

            private String imageID;
            private String deviceID;
            private long entryTime;
            private long createTime;
            private String zfsPath;
            private String deviceName;
            private int isBlk;
            private String zfsPersonPath;
            private String snapshotID;
            private String snapshotbodyID;
            private String direction;

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

            public String getZfsPersonPath() {
                return zfsPersonPath;
            }

            public void setZfsPersonPath(String zfsPersonPath) {
                this.zfsPersonPath = zfsPersonPath;
            }

            public String getSnapshotID() {
                return snapshotID;
            }

            public void setSnapshotID(String snapshotID) {
                this.snapshotID = snapshotID;
            }

            public String getSnapshotbodyID() {
                return snapshotbodyID;
            }

            public void setSnapshotbodyID(String snapshotbodyID) {
                this.snapshotbodyID = snapshotbodyID;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }
        }

        public static class CompareResultsBean {
            /**
             * ipcBlacklist : {"compareID":"15dfff6d-ca26-4988-a6a7-2553ae3b5eb9","blacklistID":"6578","zfsPath":"122.115.57.130:8080@2018-11-23-0000_101","imageBase64":null,"name":"谢宝荣","sex":null,"birthday":null,"nation":"汉族","dubious":12,"note":null,"repoID":"75dd62c4-8eaf-4ab8-8d1f-7066e712b8ec"}
             * similarity : 92.907974
             */

            private IpcBlacklistBean ipcBlacklist;
            private double similarity;

            public IpcBlacklistBean getIpcBlacklist() {
                return ipcBlacklist;
            }

            public void setIpcBlacklist(IpcBlacklistBean ipcBlacklist) {
                this.ipcBlacklist = ipcBlacklist;
            }

            public double getSimilarity() {
                return similarity;
            }

            public void setSimilarity(double similarity) {
                this.similarity = similarity;
            }

            public static class IpcBlacklistBean {
                /**
                 * compareID : 15dfff6d-ca26-4988-a6a7-2553ae3b5eb9
                 * blacklistID : 6578
                 * zfsPath : 122.115.57.130:8080@2018-11-23-0000_101
                 * imageBase64 : null
                 * name : 谢宝荣
                 * sex : null
                 * birthday : null
                 * nation : 汉族
                 * dubious : 12
                 * note : null
                 * repoID : 75dd62c4-8eaf-4ab8-8d1f-7066e712b8ec
                 */

                private String compareID;
                private String blacklistID;
                private String zfsPath;
                private String imageBase64;
                private String name;
                private String sex;
                private String birthday;
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

                public String getImageBase64() {
                    return imageBase64;
                }

                public void setImageBase64(String imageBase64) {
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

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
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
