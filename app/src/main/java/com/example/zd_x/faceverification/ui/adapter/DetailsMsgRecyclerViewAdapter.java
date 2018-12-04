package com.example.zd_x.faceverification.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.mvp.model.CompareResultsBean;
import com.example.zd_x.faceverification.ui.activity.DetailsActivity;
import com.example.zd_x.faceverification.utils.Glide.GlideApp;
import com.example.zd_x.faceverification.utils.LogUtil;

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
        LogUtil.e("onBindViewHolder: " + mList.get(position).getZfsPath());
        if (mList.get(position).getZfsPath() == null) {
            //当ViewHolder复用的时候，如果当前返回的图片url为null，为了防止上一个复用的viewHolder图片
            //遗留，要clear并且将图片设置为空。
            Glide.with(mContext).clear(holder.ivNetworkImageDetailsRecycler);
            holder.ivNetworkImageDetailsRecycler.setImageDrawable(null);
            holder.ivNetworkImageDetailsRecycler.setTag(R.id.iv_networkImage_details_recycler, position);
            return;
        }

        Object tag = holder.ivNetworkImageDetailsRecycler.getTag(R.id.iv_networkImage_details_recycler);
        if (tag != null && (int) tag != position) {
            //如果tag不是Null,并且同时tag不等于当前的position。
            //说明当前的viewHolder是复用来的
            Glide.with(mContext).clear(holder.ivNetworkImageDetailsRecycler);
        }
        GlideApp.with(mContext)
                .load(mList.get(position).getZfsPath())
                .placeholder(R.drawable.recog_200_middle)
                .centerCrop()
                .into(holder.ivNetworkImageDetailsRecycler);
        String defaultValue = "无";
//        Gli
        //给ImageView设置唯一标记。
//        holder.ivNetworkImageDetailsRecycler.setTag(R.id.iv_networkImage_details_recycler, position);
        holder.tvMsg1DetailsRecycler.setText(mContext.getString(R.string.verificationSimilarityText) + String.valueOf(mList.get(position).getSimilarity()));
        holder.tvMsg2DetailsRecycler.setText(mList.get(position).getName() == null ? mContext.getString(R.string.verificationNameText) + defaultValue : mContext.getString(R.string.verificationNameText) + mList.get(position).getName());
        holder.tvMsg3DetailsRecycler.setText(mList.get(position).getNote() == null ? mContext.getString(R.string.verificationNoteText) + defaultValue : mContext.getString(R.string.verificationNoteText) + mList.get(position).getNote());
//        holder.tvMsg4DetailsRecycler.setText(mContext.getString(R.string.verificationSexText) + mList.get(position).getNation());
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
