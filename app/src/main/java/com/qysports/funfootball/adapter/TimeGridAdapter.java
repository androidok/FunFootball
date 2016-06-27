package com.qysports.funfootball.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.DateUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.qysports.funfootball.R;

import java.util.ArrayList;
import java.util.Date;

public class TimeGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Date> times;
    private Date startDate;
    private Date noonDate;
    private Date afterDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public TimeGridAdapter(Context context, ArrayList<Date> times, Date noonDate, Date afterDate) {
        this.context = context;
        this.times = times;
        this.noonDate = noonDate;
        this.afterDate = afterDate;
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Date getItem(int position) {
        return times.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null || holder == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_time, null);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_date);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        final Date time = getItem(position);
        final String timeStr = DateUtils.date2str(time, "HH:mm");
        holder.tv_time.setText(timeStr);

        if(startDate != null && time.equals(startDate)) {
            holder.tv_time.setBackgroundResource(R.color.txt_light_gray);
        } else if(endDate != null && time.equals(endDate)) {
            holder.tv_time.setBackgroundResource(R.color.txt_light_gray);
        } else {
            int timeRange = getTimeRange(time);
            if(timeRange == 1) {
                holder.tv_time.setBackgroundResource(R.color.colorPrimaryLight);
            } else if(timeRange == 2) {
                holder.tv_time.setBackgroundResource(R.color.colorPrimary);
            } else {
                holder.tv_time.setBackgroundResource(R.color.colorPrimaryDark);
            }
        }

        holder.tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickTime(time);
            }
        });

        return convertView;
    }

    private void clickTime(Date time) {
        if(startDate == null && endDate == null) {
            startDate = time;
        } else if(startDate != null && endDate == null) {
            if(time.after(startDate)) {
                if(getTimeRange(time) != getTimeRange(startDate)) {
                    ToastUtils.showToast(context, "只能选择同一时间段内的开始和结束时间");
                    return;
                }
                endDate = time;
            } else {
                startDate = time;
            }
        } else if(startDate != null && endDate != null) {
            startDate = time;
            endDate = null;
        }

        notifyDataSetChanged();
    }

    /**
     * 获取时间段
     * @param time
     * @return 1-上午 2-下午 3-晚上
     */
    private int getTimeRange(Date time) {
        if(time.before(noonDate)) {
            return 1;
        } else if(time.after(afterDate)) {
            return 3;
        } else {
            return 2;
        }
    }

    public static class ViewHolder {
        public TextView tv_time;
    }

}
