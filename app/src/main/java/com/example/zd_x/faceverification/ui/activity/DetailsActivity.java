package com.example.zd_x.faceverification.ui.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.compl.DetailsPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IDetailsView;
import com.example.zd_x.faceverification.ui.adapter.DetailsViewPagerAdapter;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends BaseActivity implements IDetailsView {
    @BindView(R.id.vp_showDetailsMsg_details)
    ViewPager vpShowDetailsMsgDetails;
    private DetailsPresenterCompl presenterCompl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_details;
    }

    @Override
    protected void OnActCreate(Bundle savedInstanceState) {
        presenterCompl = new DetailsPresenterCompl(this);
        processExtraData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    private void processExtraData() {
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        presenterCompl.findDetailsMsg(position);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void getDetailsMsg(List<CompareResultsBean> detailsMsgList) {
        int size = detailsMsgList.size();
        LogUtil.e("getDetailsMsg: " + size);
        addPage(detailsMsgList);
//        LogUtil.e("getDetailsMsg: ---");
//        if (detailsMsg.size() == 0) {
//            return;
//        }
//        tvHVerificationImageIDDetails.setText(getString(R.string.verificationImageIdText) + detailsMsg());
//        String result;
//        if (historyModel.getIsVerification()) {
//            result = getString(R.string.verificationTrue);
//        } else {
//            result = getString(R.string.verificationFalse);
//        }
//        tvHVerificationResultDetails.setText(getString(R.string.verificationResultText) + result);
//        tvHVerificationTimeDetails.setText(getString(R.string.verificationTimeText) + historyModel.getVerificationTime());
//        List<CompareResultsBean> compareResultsList = historyModel.getCompareResults();
//        addPage(compareResultsList);
    }

    private List<View> viewList = new ArrayList<>();

    private void addPage(List<CompareResultsBean> detailsMsgList) {

        for (int i = 0; i < detailsMsgList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.viewpager_show_detailsmsg_layout, null);
            TextView tvUsernameViewpager = view.findViewById(R.id.tv_username_viewpager);
            TextView tvSexViewpager = view.findViewById(R.id.tv_sex_viewpager);
            tvUsernameViewpager.setText(detailsMsgList.get(i).getName());
            tvSexViewpager.setText(detailsMsgList.get(i).getSex());
            viewList.add(view);
        }
        LogUtil.e(viewList.size()+"");
        try {
            DetailsViewPagerAdapter pagerAdapter = new DetailsViewPagerAdapter(viewList);
            vpShowDetailsMsgDetails.setAdapter(pagerAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e+"");
        }
    }
}
