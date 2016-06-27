package com.qysports.funfootball.activity;

import android.os.Bundle;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.view.PinnedSectionListView;
import com.boredream.bdcodehelper.view.PositionBar;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.CityLetterAdapter;
import com.qysports.funfootball.base.BaseActivity;

import java.util.ArrayList;

public class CityActivity extends BaseActivity {

    private PinnedSectionListView lv_city;
    private CityLetterAdapter adapter;
    private PositionBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        initView();
    }

    private void initView() {
        initBackTitle("选择城市");

        lv_city = (PinnedSectionListView) findViewById(R.id.lv_city);
        pb = (PositionBar) findViewById(R.id.pb);

        if(AddressData.allCities != null) {
            lv_city.setShadowVisible(false);
            adapter = new CityLetterAdapter(this, AddressData.allCities);
            adapter.setSingleChoose(getIntent().getBooleanExtra("setSingleChoose", false));
            lv_city.setAdapter(adapter);

            final ArrayList<String> letterList = adapter.getLetterList();
            pb.setItems(letterList.toArray(new String[letterList.size()]));

            pb.setOnPositionChangedListener(new PositionBar.OnPositionChangedListener() {
                @Override
                public void onPositionSelected(String key) {
                    int index = letterList.indexOf(key);
                    if(index > -1) {
                        lv_city.setSelection(index * 2);
                    }
                }
            });
        }
    }
}
