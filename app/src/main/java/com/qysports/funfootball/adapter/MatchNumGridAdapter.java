package com.qysports.funfootball.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qysports.funfootball.R;

import java.util.ArrayList;

public class MatchNumGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> types;
    private AdapterView.OnItemClickListener listener;

    public MatchNumGridAdapter(Context context, ArrayList<String> types, AdapterView.OnItemClickListener listener) {
        this.context = context;
        this.types = types;
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null || holder == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_course_type, null);
            holder.tv_course_type = (TextView) convertView.findViewById(R.id.tv_course_type);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        final String item = getItem(position);
        holder.tv_course_type.setText(item);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(position);
            }
        });

        return convertView;
    }

    private void clickItem(int position) {
        if(listener != null) {
            listener.onItemClick(null, null, position, -1);
        }
    }

    public static class ViewHolder {
        public TextView tv_course_type;
    }

}
