package com.example.zd_x.faceverification.database;

import com.example.zd_x.faceverification.dao.CompareResultsBeanDao;
import com.example.zd_x.faceverification.dao.DaoSession;
import com.example.zd_x.faceverification.dao.HistoryVerificationResultModelDao;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.DetectionModel;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.model.VerificationModel;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

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

    public void insertData(DetectionModel detectionModel, VerificationModel verificationModel) {
        HistoryVerificationResultModel historyVerModel = new HistoryVerificationResultModel();

        int total = verificationModel.getOut().getTotal();
        if (total > 0) {
            historyVerModel.setIsVerification(true);
        } else {
            historyVerModel.setIsVerification(false);
        }
        historyVerModel.setImageId(detectionModel.getImageID());
        historyVerModel.setTotal(verificationModel.getOut().getTotal());
        historyVerModel.setFaceBase64(detectionModel.getFaceBase64());
        Long entryTime = verificationModel.getOut().getIpcMetadata().getEntryTime();
        historyVerModel.setVerificationTime(getTime(entryTime));
        daoSession.insert(historyVerModel);
        for (int i = 0; i < verificationModel.getOut().getCompareResults().size(); i++) {
            CompareResultsBean compareResultsBean = new CompareResultsBean();
            compareResultsBean.setHistoryId(historyVerModel.getId());
            compareResultsBean.setImageBase64(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getImageBase64());
            compareResultsBean.setName(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getName());
            compareResultsBean.setSex(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getSex());
            compareResultsBean.setNation(verificationModel.getOut().getCompareResults().get(i).getIpcBlacklist().getNation());
            compareResultsBean.setSimilarity(verificationModel.getOut().getCompareResults().get(i).getSimilarity());
            daoSession.insert(compareResultsBean);
        }
//        historyVerModel.setCompareResults(verificationModel.getOut().getCompareResults());
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

    public List<HistoryVerificationResultModel> findData() {
        if (daoSession == null) {
            LogUtil.e("dao空");
        }
        List<HistoryVerificationResultModel> list = GreenDaoManager.getInstance().getDaoSession().queryBuilder(HistoryVerificationResultModel.class).orderDesc(HistoryVerificationResultModelDao.Properties.Id).list();
        return list == null ? new ArrayList() : list;
    }

    public List<CompareResultsBean> findDetailsMsg(int id) {
        List<CompareResultsBean> list = daoSession.queryBuilder(CompareResultsBean.class).where(CompareResultsBeanDao.Properties.HistoryId.eq(id)).orderDesc(CompareResultsBeanDao.Properties.Similarity).list();
        return list == null ? new ArrayList() : list;
    }

    public HistoryVerificationResultModel findNativedata(int id) {
        LogUtil.e(id + "");
        return daoSession.queryBuilder(HistoryVerificationResultModel.class).where(HistoryVerificationResultModelDao.Properties.Id.eq(id)).unique();
    }

    private String getTime(long entryTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss  ");
        Date date = new Date(entryTime);
        return simpleDateFormat.format(date);
    }

}
