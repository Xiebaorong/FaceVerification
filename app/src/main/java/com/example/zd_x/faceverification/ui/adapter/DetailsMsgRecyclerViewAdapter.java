package com.example.zd_x.faceverification.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.ui.activity.DetailsActivity;
import com.example.zd_x.faceverification.utils.FileUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsMsgRecyclerViewAdapter extends RecyclerView.Adapter<DetailsMsgRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<CompareResultsBean> mList;

    public DetailsMsgRecyclerViewAdapter(DetailsActivity context, List<CompareResultsBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_details_msg_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String faceBase64 = mList.get(position).getImageBase64();
//        //TODO 需要两个参数,用于判断bitmap是否缩放显示 ,传入参数确定中
        Bitmap bitmap = FileUtils.base64ToBitmap(faceBase64);
        holder.ivNetworkImageDetailsRecycler.setImageBitmap(bitmap);
        holder.tvMsg1DetailsRecycler.setText(mContext.getString(R.string.verificationSimilarityText) + String.valueOf(mList.get(position).getSimilarity()));
        holder.tvMsg2DetailsRecycler.setText(mContext.getString(R.string.verificationNameText) + mList.get(position).getName());
        holder.tvMsg3DetailsRecycler.setText(mContext.getString(R.string.verificationSexText) + mList.get(position).getSex());
        holder.tvMsg4DetailsRecycler.setText(mContext.getString(R.string.verificationSexText) + mList.get(position).getNation());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_networkImage_details_recycler)
        ImageView ivNetworkImageDetailsRecycler;
        @BindView(R.id.tv_msg1_details_recycler)
        TextView tvMsg1DetailsRecycler;
        @BindView(R.id.tv_msg2_details_recycler)
        TextView tvMsg2DetailsRecycler;
        @BindView(R.id.tv_msg3_details_recycler)
        TextView tvMsg3DetailsRecycler;
        @BindView(R.id.tv_msg4_details_recycler)
        TextView tvMsg4DetailsRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
