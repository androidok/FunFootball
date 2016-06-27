package com.qysports.funfootball.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qysports.funfootball.R;
import com.qysports.funfootball.entity.PayType;

import java.util.List;

public class PayTypeAdapter extends BaseAdapter {

    private Context context;
    private List<PayType> datas;

    public PayTypeAdapter(Context context, List<PayType> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PayType getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_pay_type, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PayType item = getItem(position);
        holder.tv_name.setText(item.name);

        return convertView;
    }


    public static class ViewHolder {
        public TextView tv_name;
    }

}
