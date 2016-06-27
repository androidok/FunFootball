package com.qysports.funfootball.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qysports.funfootball.R;

import java.util.ArrayList;

public class TypeGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> types;
    private ArrayList<String> selectedTypes = new ArrayList<>();

    public ArrayList<String> getSelectedTypes() {
        return selectedTypes;
    }

    public TypeGridAdapter(Context context, ArrayList<String> types) {
        this.context = context;
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public String getItem(int position) {
        return types.get(position);
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
            convertView = View.inflate(context, R.layout.item_grid_course_type, null);
            holder.tv_course_type = (TextView) convertView.findViewById(R.id.tv_course_type);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        final String age = getItem(position);
        holder.tv_course_type.setText(age);
        holder.tv_course_type.setBackgroundResource(selectedTypes.contains(age)
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
        if(!selectedTypes.contains(age)) {
            selectedTypes.add(age);
        } else {
            selectedTypes.remove(age);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView tv_course_type;
    }

}
