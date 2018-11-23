package com.example.zd_x.faceverification.mvp.p.compl;

import android.util.Log;

import com.example.zd_x.faceverification.database.DataManipulation;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.IDetailsPresenter;
import com.example.zd_x.faceverification.mvp.view.IDetailsView;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailsPresenterCompl implements IDetailsPresenter {
    private IDetailsView iDetailsView;

    public DetailsPresenterCompl(IDetailsView view) {
        iDetailsView = view;
    }

    @Override
    public void findDetailsMsg(int position) {
        HistoryVerificationResultModel nativeData = DataManipulation.getInstance().findNativedata(position);
        iDetailsView.getNativedata(nativeData);
        List<CompareResultsBean> detailsMsg;
        if (nativeData.getTotal() != 0) {
            detailsMsg = DataManipulation.getInstance().findDetailsMsg(position);
        } else {
            detailsMsg = new ArrayList<>();
        }
        iDetailsView.getDetailsMsg(detailsMsg);
    }
}
