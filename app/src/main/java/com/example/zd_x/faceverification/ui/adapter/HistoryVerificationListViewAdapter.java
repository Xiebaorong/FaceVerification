package com.example.zd_x.faceverification.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.ui.activity.DetailsActivity;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.Glide.GlideApp;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.example.zd_x.faceverification.utils.PictureMsgUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryVerificationListViewAdapter extends RecyclerView.Adapter<HistoryVerificationListViewAdapter.ViewHolder> {
    private Context mContext;
    private List<HistoryVerificationResultModel> mList;

    public HistoryVerificationListViewAdapter(Context context, List<HistoryVerificationResultModel> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_history_verification_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//        String faceBase64 = mList.get(position).getFaceBase64();
//        //TODO 需要两个参数,用于判断bitmap是否缩放显示 ,传入参数确定中
//        Bitmap bitmap = FileUtils.base64ToBitmap(faceBase64);
//        holder.ivHFaceImageListView.setImageBitmap(bitmap);

        if (mList.get(position).getFaceBase64() == null) { //当ViewHolder复用的时候，如果当前返回的图片url为null，为了防止上一个复用的viewHolder图片
            // 遗留，要clear并且将图片设置为空。
            GlideApp.with(mContext).clear(holder.ivHFaceImageListView);
            holder.ivHFaceImageListView.setImageDrawable(null);
            holder.ivHFaceImageListView.setTag(R.id.iv_hFaceImage_listView, position);
            return;
        }
        Object tag=holder.ivHFaceImageListView.getTag(R.id.iv_hFaceImage_listView);
        if (tag!=null&&(int) tag!= position) {
            GlideApp.with(mContext).clear(holder.ivToDetailsListView);
        }
        GlideApp.with(mContext)
                .load(mList.get(position).getFaceBase64())
                .placeholder(R.drawable.recog_200_middle)
                .centerCrop()
                .into(holder.ivHFaceImageListView);

        holder.tvHVerificationTotalListView.setText(mContext.getString(R.string.verificationTotalText) + mList.get(position).getTotal());
        holder.tvHVerificationResultListView.setText(mContext.getString(R.string.verificationResultText) + PictureMsgUtils.getInstance().getVerificationResult(mList.get(position).getIsVerification()));
        holder.tvHVerificationTimeListView.setText(mContext.getString(R.string.verificationTimeText) + mList.get(position).getVerificationTime());
        holder.ivToDetailsListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                LogUtil.e(mList.get(position).getId() + "");
                intent.putExtra(ConstsUtils.ID_IDENTIFY, mList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void partItemChanged(List<HistoryVerificationResultModel> historylist) {
        mList.addAll(historylist);
        notifyItemChanged(mList.size(), 1);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hFaceImage_listView)
        ImageView ivHFaceImageListView;
        @BindView(R.id.tv_hVerificationTime_listView)
        TextView tvHVerificationTimeListView;
        @BindView(R.id.tv_hVerificationResult_listView)
        TextView tvHVerificationResultListView;
        @BindView(R.id.tv_hVerificationTotal_listView)
        TextView tvHVerificationTotalListView;
        @BindView(R.id.iv_toDetails_listView)
        ImageView ivToDetailsListView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
