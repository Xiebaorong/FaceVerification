package com.example.zd_x.faceverification.mvp.view;

import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;

import java.util.List;

public interface IHomeView {
    List<HistoryVerificationResultModel> getHistoryMsg();

    void notifyDataSetChanged();

    void partItemnotifyDataSetChanged();

    void appUpdate();
}
