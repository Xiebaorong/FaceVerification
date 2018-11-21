package com.example.zd_x.faceverification.mvp.view;

import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;

import java.util.List;

public interface IDetailsView {
    void getDetailsMsg(List<CompareResultsBean> detailsMsg);

    void getNativedata(HistoryVerificationResultModel nativedata);
}
