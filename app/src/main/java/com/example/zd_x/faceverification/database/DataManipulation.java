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

//        int total = verificationModel.getOut().getTotal();
//        if (total > 0) {
//            historyVerModel.setIsVerification(true);
//        } else {
//            historyVerModel.setIsVerification(false);
//        }
//        historyVerModel.setCompareResults(verificationModel.getOut().getCompareResults());
//        historyVerModel.setImageId(detectionModel.getImageID());
//        historyVerModel.setTotal(verificationModel.getOut().getTotal());
//        historyVerModel.setFaceBase64(detectionModel.getFaceBase64());
//        long entryTime = verificationModel.getOut().getIpcMetadata().getEntryTime();
//        historyVerModel.setVerificationTime(getTime(entryTime));
//        historyVerModel.setCompareResults(verificationModel.getOut().getCompareResults());
        try {
            historyVerModel.setImageId("11010815426100376200000000000000000000002");
            historyVerModel.setTotal(0);
            historyVerModel.setIsVerification(false);
            historyVerModel.setFaceBase64("/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDABsSFBcUERsXFhceHBsgKEIrKCUlKFE6PTBCYFVlZF9VXVtqeJmBanGQc1tdhbWGkJ6jq62rZ4C8ybqmx5moq6T/2wBDARweHigjKE4rK06kbl1upKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKT/wAARCABkAGQDASIAAhEBAxEB/8QAFwABAQEBAAAAAAAAAAAAAAAAAAEDBf/EABUQAQEAAAAAAAAAAAAAAAAAAAAB/8QAFQEBAQAAAAAAAAAAAAAAAAAAAAH/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwDtgqoAALABQEBQABQAAZgKCgAqKACgAqAAAKAyVFUAUAFABUAFBFAAUBkAooKAAAoAKACgAKIMVIKKACgACgCooCgAADJUUBQAUUEUAAUAFAABioAqgAoAKAAAKAAAg//Z");
            historyVerModel.setVerificationTime(getTime(1517387585987L));
            daoSession.insert(historyVerModel);
            for (int i = 0; i < 3; i++) {
                CompareResultsBean compareResultsBean = new CompareResultsBean();
                compareResultsBean.setHistoryId(historyVerModel.getId());
                compareResultsBean.setName("李" + i);
                compareResultsBean.setSex("女" + i);
                daoSession.insert(compareResultsBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e+"");
        }
    }

    public List<HistoryVerificationResultModel> findData() {
        if (daoSession == null) {
            LogUtil.e("dao空");
        }
        List<HistoryVerificationResultModel> list = GreenDaoManager.getInstance().getDaoSession().queryBuilder(HistoryVerificationResultModel.class).list();
        return list == null ? new ArrayList() : list;
    }

    public List<CompareResultsBean> findDetailsMsg(int id) {
        List<CompareResultsBean> list = daoSession.queryBuilder(CompareResultsBean.class).where(CompareResultsBeanDao.Properties.HistoryId.eq(id)).orderAsc(CompareResultsBeanDao.Properties.HistoryId).list();
        return list == null ? new ArrayList() : list;
    }

    private String getTime(long entryTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss  ");
        Date date = new Date(entryTime);
        return simpleDateFormat.format(date);
    }

}
