package com.qysports.funfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.ActOfficeAdapter;
import com.qysports.funfootball.base.BaseFragment;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.OfficeActivity;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import java.util.ArrayList;

import rx.Observable;

public class ActOfficeFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<OfficeActivity> datas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_act_office, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        multiPageLoadPresent = new MultiPageLoadPresent(activity, view.findViewById(R.id.srl));
        multiPageLoadPresent.setItemDecoration(null);
    }

    private void initData() {
        loadPrivateAct();
    }

    private void loadPrivateAct() {
        datas.clear();

        ActOfficeAdapter adapter = new ActOfficeAdapter(activity, datas);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest<ListResponse<OfficeActivity>>() {
                    @Override
                    public Observable<ListResponse<OfficeActivity>> request(int page) {
                        return HttpRequest.getOfficeAct(page);
                    }
                },
                new SimpleSubscriber<ListResponse<OfficeActivity>>(activity) {
                    @Override
                    public void onNext(ListResponse<OfficeActivity> response) {
                        //
                    }
                });
    }
}
