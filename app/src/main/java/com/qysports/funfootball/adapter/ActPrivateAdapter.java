package com.qysports.funfootball.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.present.ImageBannerPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.activity.ActDetailActivity;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.Ad;
import com.qysports.funfootball.net.GlideHelper;
import com.qysports.funfootball.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ActPrivateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_LIST = 1;

    private Context context;
    private List<Act> datas;
    private ArrayList<Ad> ads;
    private ImageBannerPresent imageBannerPresent;

    public void setAds(ArrayList<Ad> ads) {
        this.ads = ads;
    }

    public ActPrivateAdapter(Context context, List<Act> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getItemCount() {
        // header + 1
        return datas == null ? 1 : datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_LIST;
    }

    public void stopBannerAutoScroll() {
        if(imageBannerPresent != null) {
            imageBannerPresent.stopAutoScroll();
        }
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public ViewHolderHeader(final View itemView) {
            super(itemView);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_address;
        public TextView tv_time;

        public ViewHolder(final View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_banner_with_indicator, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_act_private, parent, false);
            return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            // header
            ViewHolderHeader holderHeader = (ViewHolderHeader) holder;
            if(ads != null) {
                imageBannerPresent = new ImageBannerPresent(context, holderHeader.itemView);
                imageBannerPresent.load(ads);
            }
        } else {
            // item
            ViewHolder holderList = (ViewHolder) holder;
            final Act data = datas.get(position - 1);
            GlideHelper.showCornerImage(context,
                    data.getImageList().get(0).getImageUrl(),
                    holderList.iv_image, DisplayUtils.dp2px(context, 4));
            holderList.tv_title.setText(data.getTitle());
            holderList.tv_date.setText(StringUtils.getSplitDate(data.getDate()));
            holderList.tv_address.setText(data.getLocation());
            holderList.tv_time.setText(StringUtils.getSplitTime(data.getDate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ActDetailActivity.class);
                    intent.putExtra("act", data);
                    context.startActivity(intent);
                }
            });
        }
    }
}
