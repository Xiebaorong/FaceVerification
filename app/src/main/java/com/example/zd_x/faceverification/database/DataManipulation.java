package com.example.zd_x.faceverification.database;

import com.example.zd_x.faceverification.dao.CompareResultsBeanDao;
import com.example.zd_x.faceverification.dao.DaoSession;
import com.example.zd_x.faceverification.dao.HistoryVerificationResultModelDao;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.DetectionModel;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.model.VerificationModel;
import com.example.zd_x.faceverification.utils.APPUrl;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManipulation {
    private static DataManipulation instance;
    private DaoSession daoSession;


    public static DataManipulation getInstance() {
        if (instance == null) {
            synchronized (DataManipulation.class) {
                if (instance == null) instance = new DataManipulation();
            }
        }
        return instance;
    }

    public void initDao() {
        daoSession = GreenDaoManager.getInstance().getDaoSession();
    }

    public long insertData(DetectionModel detectionModel, VerificationModel verificationModel) {
        long insert = 0;
        HistoryVerificationResultModel historyVerModel = new HistoryVerificationResultModel();
        historyVerModel.setImageId(detectionModel.getImageID());
        historyVerModel.setTotal(verificationModel.getOut().getTotal());
//        historyVerModel.setFaceBase64(detectionModel.getFaceBase64());
        historyVerModel.setFaceBase64(getImageIp(verificationModel.getOut().getIpcMetadata().getZfsPath()));

        Long entryTime = verificationModel.getOut().getIpcMetadata().getEntryTime();
        historyVerModel.setVerificationTime(getTime(entryTime));
        int total = verificationModel.getOut().getTotal();
        if (total > 0) {
            historyVerModel.setIsVerification(true);
        } else {
            historyVerModel.setIsVerification(false);
        }
        insert = daoSession.insert(historyVerModel);
        if (total != 0) {
            for (int i = 0; i < verificationModel.getOut().getCompareResults().size(); i++) {
                CompareResultsBean compareResultsBean = new CompareResultsBean();
                compareResultsBean.setHistoryId(historyVerModel.getId());
                String imageIp = getImageIp(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getZfsPath());
                LogUtil.e(imageIp);
                compareResultsBean.setZfsPath(getImageIp(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getZfsPath()));
                compareResultsBean.setName(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getName());
                compareResultsBean.setSex(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getSex());
                compareResultsBean.setNation(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getNation());
                compareResultsBean.setNote(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getNote());
                compareResultsBean.setSimilarity(formateRate(verificationModel.getOut().getCompareResults().get(i).getSimilarity() + ""));

                daoSession.insert(compareResultsBean);
            }
        }
        return insert;
//        try {
//            historyVerModel.setImageId("11010815426100376200000000000000000000002");
//            historyVerModel.setTotal(3);
//            historyVerModel.setIsVerification(true);
//            historyVerModel.setFaceBase64(detectionModel.getFaceBase64());
//            historyVerModel.setVerificationTime(getTime(System.currentTimeMillis()));
//            daoSession.insert(historyVerModel);
//            for (int i = 0; i < 3; i++) {
//                CompareResultsBean compareResultsBean = new CompareResultsBean();
//                LogUtil.e(historyVerModel.getId() + "");
//                compareResultsBean.setHistoryId(historyVerModel.getId());
//                compareResultsBean.setImageBase64(detectionModel.getFaceBase64());
//                compareResultsBean.setSimilarity(95 + i);
//                compareResultsBean.setName("李" + i);
//                compareResultsBean.setSex("男");
//                compareResultsBean.setNation("汉");
//                daoSession.insert(compareResultsBean);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtil.e(e + "");
//        }
    }

    public List<HistoryVerificationResultModel> findData(int offset) {
        if (daoSession == null) {
            LogUtil.e("dao空");
        }
        List<HistoryVerificationResultModel> list = GreenDaoManager.getInstance().getDaoSession().queryBuilder(HistoryVerificationResultModel.class).offset(offset * 10).limit(10).orderDesc(HistoryVerificationResultModelDao.Properties.Id).list();
        return list == null ? new ArrayList() : list;
    }

    public List<CompareResultsBean> findDetailsMsg(int id) {
        List<CompareResultsBean> list = daoSession.queryBuilder(CompareResultsBean.class).where(CompareResultsBeanDao.Properties.HistoryId.eq(id)).orderDesc(CompareResultsBeanDao.Properties.Similarity).list();
        return list == null ? new ArrayList() : list;
    }

    public HistoryVerificationResultModel findNativedata(int id) {
        return daoSession.queryBuilder(HistoryVerificationResultModel.class).where(HistoryVerificationResultModelDao.Properties.Id.eq(id)).unique();
    }

    private String getTime(long entryTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
        Date date = new Date(entryTime);
        return simpleDateFormat.format(date);
    }

    public String getImageIp(String zfsPath) {
        return "http://" + zfsPath.split("@")[0] + APPUrl.IMAGE_IP + zfsPath.split("@")[1] + ".jpg";
    }

    public String formateRate(String rateStr) {
        if (rateStr.indexOf(".") != -1) {
            //获取小数点的位置
            int num = 0;
            num = rateStr.indexOf(".");

            //获取小数点后面的数字 是否有两位 不足两位补足两位
            String dianAfter = rateStr.substring(0, num + 1);
            String afterData = rateStr.replace(dianAfter, "");
            if (afterData.length() < 2) {
                afterData = afterData + "0";
            } else {
                afterData = afterData;
            }
            return rateStr.substring(0, num) + "." + afterData.substring(0, 2) + "%";
        } else {
            if (rateStr == "1") {
                return "100%";
            } else {
                return rateStr + "%";
            }
        }
    }
}
