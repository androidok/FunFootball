package com.qysports.funfootball.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseFragment;
import com.qysports.funfootball.present.VpWithIndicatorPresent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends BaseFragment {

    private View view;
    private VpWithIndicatorPresent vpWithIndicatorPresent;
    private CityModel curCity = AddressData.currentCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_act, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(final View view) {
        initCityTitle(view, getString(R.string.tab3), curCity.name);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new ActPrivateFragment());
        fragments.add(new ActOfficeFragment());
        vpWithIndicatorPresent = new VpWithIndicatorPresent(activity,
                (RadioGroup) view.findViewById(R.id.rg_act_top_tab),
                (ViewPager) view.findViewById(R.id.vp_act_container));
        vpWithIndicatorPresent.load(new String[]{"校内", "官方"}, fragments);
        vpWithIndicatorPresent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initCityTitle(view, getString(R.string.tab3),
                        position == 0 ? curCity.name : null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCity(CityModel city) {
        if(!city.equals(curCity)) {
            showLog("update dif city = " + city.name);
            curCity = city;
            initCityTitle(view, getString(R.string.tab3), curCity.name);
        }
    }

}
