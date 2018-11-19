package com.example.zd_x.faceverification.mvp.view;

import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;

import java.util.List;

public interface IHomeView {
    void getHistoryMsg(List<HistoryVerificationResultModel> list);
}
