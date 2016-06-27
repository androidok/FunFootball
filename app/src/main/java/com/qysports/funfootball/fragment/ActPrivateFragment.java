package com.qysports.funfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.ActPrivateAdapter;
import com.qysports.funfootball.base.BaseFragment;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.Ad;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func2;

public class ActPrivateFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<Act> datas = new ArrayList<>();
    private ActPrivateAdapter adapter;
    private CityModel curCity = AddressData.currentCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_act_private, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        multiPageLoadPresent = new MultiPageLoadPresent(activity, view.findViewById(R.id.srl));
    }

    private void initData() {
        loadPrivateAct();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCity(CityModel city) {
        if (!city.equals(curCity)) {
            showLog("update dif city = " + city.name);
            curCity = city;
            loadPrivateAct();
        }
    }

    private void loadPrivateAct() {
        datas.clear();

        adapter = new ActPrivateAdapter(activity, datas);
        final PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest<ListResponse<Act>>() {
                    @Override
                    public Observable<ListResponse<Act>> request(int page) {
                        Observable<ListResponse<Act>> actObservable = HttpRequest.getPrivateAct(
                                AddressData.currentCity.name, page);

                        // 如果加载的不是起始页，则只加载活动列表
                        if(page != pageIndex.getStartPage()) {
                            return actObservable;
                        }

                        // 如果是加载起始页，则同时加载广告和活动列表
                        Observable<ListResponse<Ad>> adObservable = HttpRequest.getAdByType(3);
                        return Observable.zip(actObservable, adObservable,
                                new Func2<ListResponse<Act>, ListResponse<Ad>, ListResponse<Act>>() {
                                    @Override
                                    public ListResponse<Act> call(ListResponse<Act> actListResponse,
                                                                  ListResponse<Ad> response) {
                                        adapter.stopBannerAutoScroll();
                                        adapter.setAds(response.getResults());
                                        return actListResponse;
                                    }
                                });
                    }
                },
                new SimpleSubscriber<ListResponse<Act>>(activity) {
                    @Override
                    public void onNext(ListResponse<Act> response) {
                        //
                    }
                });
    }
}
