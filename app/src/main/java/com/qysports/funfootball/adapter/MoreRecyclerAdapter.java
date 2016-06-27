package com.qysports.funfootball.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.GlideHelper;

import java.util.List;

/**
 * 更多选项列表适配器
 * <p/>
 * 第一个位置为HEADER类型,对应用户信息<br/>
 * 其他位置为LIST类型,对应选项item
 */
public class MoreRecyclerAdapter extends SettingRecyclerAdapter {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;

    private User user;

    public MoreRecyclerAdapter(List<SettingItem> datas, User user,
                               AdapterView.OnItemClickListener listener) {
        super(datas, listener);
        this.user = user;
    }

    @Override
    public int getItemCount() {
        // header + 1
        return datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : super.getItemViewType(position);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class ViewHolderUserHeader extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_name;
        public TextView tv_address;

        public ViewHolderUserHeader(final View itemView) {
            super(itemView);

            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_more_header, parent, false);
            return new ViewHolderUserHeader(v);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            // header
            ViewHolderUserHeader viewHolderHeader = (ViewHolderUserHeader) holder;

            GlideHelper.showCornerImage(holder.itemView.getContext(),
                    user.getAvatar(),
                    viewHolderHeader.iv_avatar,
                    DisplayUtils.dp2px(holder.itemView.getContext(), 6));
            viewHolderHeader.tv_name.setText(user.getUsername());
            viewHolderHeader.tv_address.setText(user.getLocation());

            viewHolderHeader.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        // header使用特殊的position -1
                        mOnItemClickListener.onItemClick(null, view, -1, -1);
                    }
                }
            });
        } else {
            // setting item
            // 第一个位置多了个HEADER,所以position差1
            super.onBindViewHolder(holder, position - 1);
        }
    }

}
