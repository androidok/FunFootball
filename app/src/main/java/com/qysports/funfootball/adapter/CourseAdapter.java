package com.qysports.funfootball.adapter;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qysports.funfootball.R;
import com.qysports.funfootball.activity.CourseDetailActivity;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.net.GlideHelper;
import com.qysports.funfootball.utils.StringUtils;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Activity context;
    private List<Course> datas;

    public CourseAdapter(Activity context, List<Course> datas) {
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
        public TextView tv_address;
        public TextView tv_type;
        public TextView tv_time;

        public ViewHolder(final View itemView) {
            super(itemView);

            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Course data = datas.get(position);

        GlideHelper.showImage(context, data.getImageList().get(0).getImageUrl(), holder.iv_image);

        holder.tv_title.setText(data.getTitle());
        holder.tv_address.setText(data.getLocation());
        holder.tv_type.setText(data.getType());
        holder.tv_time.setText(StringUtils.getSplitDate(data.getDate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("course", data);
                context.startActivity(intent);
            }
        });
    }
}
