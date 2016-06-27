package com.qysports.funfootball.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.activity.WebViewActivity;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.OfficeActivity;
import com.qysports.funfootball.net.GlideHelper;

import java.util.List;

public class ActOfficeAdapter extends RecyclerView.Adapter<ActOfficeAdapter.ViewHolder> {

    private Context context;
    private List<OfficeActivity> datas;

    public ActOfficeAdapter(Context context, List<OfficeActivity> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        public TextView tv_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    @Override
    public ActOfficeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_act_office, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final OfficeActivity data = datas.get(position);
        GlideHelper.showCornerImage(context,
                data.getImgUrl(),
                holder.iv_image,
                DisplayUtils.dp2px(context, 4));
        holder.tv_title.setText(data.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("title", data.getTitle());
                intent.putExtra("url", data.getLink());
                context.startActivity(intent);
            }
        });
    }


}
