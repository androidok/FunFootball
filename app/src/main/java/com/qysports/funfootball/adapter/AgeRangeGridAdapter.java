package com.qysports.funfootball.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qysports.funfootball.R;

import java.util.ArrayList;

public class AgeRangeGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> ages;
    private ArrayList<String> selectedAges = new ArrayList<>();

    public ArrayList<String> getSelectedAges() {
        return selectedAges;
    }

    public AgeRangeGridAdapter(Context context, ArrayList<String> ages) {
        this.context = context;
        this.ages = ages;
    }

    @Override
    public int getCount() {
        return ages.size();
    }

    @Override
    public String getItem(int position) {
        return ages.get(position);
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
            convertView = View.inflate(context, R.layout.item_grid_age_range, null);
            holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        final String age = getItem(position);
        holder.tv_age.setText(age);
        holder.tv_age.setBackgroundResource(selectedAges.contains(age)
            ? R.color.colorPrimary : R.color.txt_light_gray);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAge(age);
            }
        });

        return convertView;
    }

    private void clickAge(String age) {
        if(!selectedAges.contains(age)) {
            selectedAges.add(age);
        } else {
            selectedAges.remove(age);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView tv_age;
    }

}
