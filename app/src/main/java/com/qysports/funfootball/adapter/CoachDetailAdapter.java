package com.qysports.funfootball.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.CoachComment;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.GlideHelper;

import java.util.ArrayList;
import java.util.List;

public class CoachDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_LIST = 1;

    private User coach;
    private List<CoachComment> comments = new ArrayList<>();
    private int commentType;
    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public void setDatas(ArrayList<CoachComment> datas) {
        this.comments = datas;
    }

    public CoachDetailAdapter(User coach, List<CoachComment> comments,
                              RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        this.coach = coach;
        this.comments = comments;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public int getItemCount() {
        // header + 1
        return comments == null ? 1 : comments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_LIST;
    }

    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public ImageView iv_bg_avatar;
        public ImageView iv_avatar;
        public TextView tv_name;
        public TextView tv_age;
        public TextView tv_training_years;
        public TextView tv_cert_level;
        public TextView tv_career;
        public RadioGroup rg_coach_comment;
        public RadioButton rb_coach_comment_all;
        public RadioButton rb_coach_comment1;
        public RadioButton rb_coach_comment2;
        public RadioButton rb_coach_comment3;

        public ViewHolderHeader(final View itemView) {
            super(itemView);

            iv_bg_avatar = (ImageView) itemView.findViewById(R.id.iv_bg_avatar);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_age = (TextView) itemView.findViewById(R.id.tv_age);
            tv_training_years = (TextView) itemView.findViewById(R.id.tv_training_years);
            tv_cert_level = (TextView) itemView.findViewById(R.id.tv_cert_level);
            tv_career = (TextView) itemView.findViewById(R.id.tv_career);
            rg_coach_comment = (RadioGroup) itemView.findViewById(R.id.rg_coach_comment);
            rb_coach_comment_all = (RadioButton) itemView.findViewById(R.id.rb_coach_comment_all);
            rb_coach_comment1 = (RadioButton) itemView.findViewById(R.id.rb_coach_comment1);
            rb_coach_comment2 = (RadioButton) itemView.findViewById(R.id.rb_coach_comment2);
            rb_coach_comment3 = (RadioButton) itemView.findViewById(R.id.rb_coach_comment3);
        }
    }

    public static class ViewHolderList extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_date;
        public TextView tv_content;

        public ViewHolderList(final View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_coach_detail, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coach_comment, parent, false);
            return new ViewHolderList(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_VIEW_TYPE_HEADER) {
            // header
            ViewHolderHeader holderHeader = (ViewHolderHeader) holder;
            GlideHelper.showAvatar(holder.itemView.getContext(), coach.getAvatar(), holderHeader.iv_avatar);
            holderHeader.tv_name.setText(coach.getCoachName());
            holderHeader.tv_age.setText(coach.getCoachAge() + "");
            holderHeader.tv_training_years.setText(coach.getTrainingYears() + "");
            holderHeader.tv_cert_level.setText(coach.getCertLevel());
            holderHeader.tv_career.setText(coach.getCareer());
            holderHeader.rg_coach_comment.setOnCheckedChangeListener(onCheckedChangeListener);
            checkItem(holderHeader);
        } else {
            // item
            CoachComment comment = comments.get(position - 1);

            ViewHolderList holderList = (ViewHolderList) holder;
            holderList.tv_name.setText(comment.getUser().getNickname());
            holderList.tv_date.setText(comment.getDate());
            holderList.tv_content.setText(comment.getContent());
        }
    }

    private void checkItem(ViewHolderHeader holderHeader) {
        switch (commentType) {
            case 1:
                holderHeader.rb_coach_comment1.setChecked(true);
                break;
            case 2:
                holderHeader.rb_coach_comment2.setChecked(true);
                break;
            case 3:
                holderHeader.rb_coach_comment3.setChecked(true);
                break;
            default:
                holderHeader.rb_coach_comment_all.setChecked(true);
                break;
        }
    }
}