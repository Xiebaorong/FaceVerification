package com.example.zd_x.faceverification.mvp.model;

public class CompareResultsBean {
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
