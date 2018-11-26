package com.example.zd_x.faceverification.mvp.p.compl;

import android.util.Log;

import com.example.zd_x.faceverification.database.DataManipulation;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.IHomePresenter;
import com.example.zd_x.faceverification.ui.activity.HomeActivity;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.util.List;

public class HomePresenterCompl implements IHomePresenter {
    private HomeActivity IHomeView;

    public HomePresenterCompl(HomeActivity view) {
        IHomeView = view;
    }

    @Override
    public void findHistoryResult(int offset) {
        LogUtil.e("-----");
        try {
            List<HistoryVerificationResultModel> historyList = DataManipulation.getInstance().findData(offset);
            if (offset==0){
                IHomeView.getHistoryMsg().clear();
                IHomeView.getHistoryMsg().addAll(historyList);
                IHomeView.notifyDataSetChanged();
            }else {
                IHomeView.getHistoryMsg().addAll(historyList);
                IHomeView.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e+"");
        }
    }
}
