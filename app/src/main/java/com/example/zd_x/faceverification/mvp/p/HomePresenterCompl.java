package com.example.zd_x.faceverification.mvp.p;

import com.example.zd_x.faceverification.ui.activity.HomeActivity;

public class HomePresenterCompl implements IHomePresenter {
    private HomeActivity IHomeView;

    public HomePresenterCompl(HomeActivity view) {
        IHomeView = view;
    }
}
