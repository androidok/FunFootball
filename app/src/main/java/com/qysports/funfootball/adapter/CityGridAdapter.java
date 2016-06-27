package com.qysports.funfootball.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.qysports.funfootball.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CityGridAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<CityModel> cities;
    private boolean isSingleChoose;

    public void setSingleChoose(boolean singleChoose) {
        isSingleChoose = singleChoose;
    }

    public CityGridAdapter(Activity context, ArrayList<CityModel> cities) {
        this.context = context;
        this.cities = cities;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public CityModel getItem(int position) {
        return cities.get(position);
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
            convertView = View.inflate(context, R.layout.item_grid_city, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        final CityModel city = getItem(position);
        holder.tv_name.setText(city.name);

        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSingleChoose) {
                    // 单次选择，不变换全部城市，只result返回
                    Intent intent = new Intent();
                    intent.putExtra("city", city);
                    context.setResult(Activity.RESULT_OK, intent);
                    context.finish();
                } else {
                    AddressData.currentCity = city;
                    EventBus.getDefault().post(city);
                    context.finish();
                }
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public TextView tv_name;
    }

}
