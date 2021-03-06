package com.example.zd_x.faceverification.ui.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.compl.DetailsPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IDetailsView;
import com.example.zd_x.faceverification.ui.adapter.DetailsMsgRecyclerViewAdapter;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.Glide.GlideApp;
import com.example.zd_x.faceverification.utils.PictureMsgUtils;

import java.util.List;

import butterknife.BindView;

public class DetailsActivity extends BaseActivity implements IDetailsView {
    @BindView(R.id.vp_showDetailsMsg_details)
    ViewPager vpShowDetailsMsgDetails;
    @BindView(R.id.iv_faceImage_details)
    ImageView ivFaceImageDetails;
    @BindView(R.id.tv_msg1_details)
    TextView tvMsg1Details;
    @BindView(R.id.tv_msg2_details)
    TextView tvMsg2Details;
    @BindView(R.id.tv_msg3_details)
    TextView tvMsg3Details;
    @BindView(R.id.tv_msg4_details)
    TextView tvMsg4Details;
    @BindView(R.id.rv_showDetailsMsg_details)
    RecyclerView rvShowDetailsMsgDetails;


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
        int position = (int) intent.getLongExtra(ConstsUtils.ID_IDENTIFY,0);
        presenterCompl.findDetailsMsg(position);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onNetChanged(int netWorkState) {

    }

    @Override
    public void getDetailsMsg(List<CompareResultsBean> detailsMsgList) {
        if (detailsMsgList.size()==0){
            return;
        }
        DetailsMsgRecyclerViewAdapter adapter = new DetailsMsgRecyclerViewAdapter(this, detailsMsgList);
        rvShowDetailsMsgDetails.setLayoutManager(new LinearLayoutManager(this));
//        rvShowDetailsMsgDetails.addItemDecoration(new SpaceItemDecoration(0, 30));
        //添加自定义分割线
//        rvShowDetailsMsgDetails.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.custom_divider_shape));
        rvShowDetailsMsgDetails.addItemDecoration(divider);
        rvShowDetailsMsgDetails.setAdapter(adapter);
    }

    @Override
    public void getNativedata(HistoryVerificationResultModel nativedata) {
//        Bitmap bitmap = FileUtils.base64ToBitmap(nativedata.getFaceBase64());
//        ivFaceImageDetails.setImageBitmap(bitmap);

        GlideApp.with(this)
                .load(nativedata.getFaceBase64())
                .placeholder(R.drawable.recog_200_middle)
                .centerCrop()
                .into(ivFaceImageDetails);
        tvMsg1Details.setText(getString(R.string.verificationTimeText) + nativedata.getVerificationTime());
        tvMsg2Details.setText(getString(R.string.verificationTotalText) + nativedata.getTotal());
        tvMsg3Details.setText(getString(R.string.verificationResultText) + PictureMsgUtils.getInstance().getVerificationResult(nativedata.getIsVerification()));
    }

}
