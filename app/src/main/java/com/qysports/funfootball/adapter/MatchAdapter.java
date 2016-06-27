package com.qysports.funfootball.adapter;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.Act;

import java.util.List;


/**
 * 动态图详情适配器
 * <p>
 * 第一个位置为HEADER类型,对应动态图图片<br/>
 * 其他位置为LIST类型,对应评论item
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private Activity context;
    private List<Act> datas;

    public MatchAdapter(Activity context, List<Act> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_username;
        public TextView tv_time;
        public TextView tv_content;

        public ViewHolder(final View itemView) {
            super(itemView);

            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_time = (TextView) itemView.findViewById(R.id.tv_date);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_coach_comment, parent, false);
            return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Act data = datas.get(position);

        holder.tv_username.setText(data.getTitle());
    }
}
