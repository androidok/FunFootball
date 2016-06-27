package com.qysports.funfootball.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boredream.bdcodehelper.adapter.ListDropDownAdapter;
import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.ImageBannerPresent;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.boredream.bdcodehelper.view.DropDownMenu;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.CourseAdapter;
import com.qysports.funfootball.base.BaseFragment;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Ad;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class CourseFragment extends BaseFragment {

    private View view;
    private MultiPageLoadPresent multiPageLoadPresent;
    private ImageBannerPresent imageBannerPresent;
    private ArrayList<Course> datas = new ArrayList<>();
    private CityModel curCity = AddressData.currentCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_course, null);
        initView();
        initData();
        return view;
    }

    private void initView() {
        initCityTitle(view, getString(R.string.tab1), curCity.name);

        imageBannerPresent = new ImageBannerPresent(
                activity, view.findViewById(R.id.include_banner_with_indicator));

        initDropDownMenu();
    }

    private void initData() {
        loadCourse();

        Observable<ListResponse<Ad>> observable = HttpRequest.getAdByType(1);
        ObservableDecorator.decorate(activity, observable)
                .subscribe(new SimpleSubscriber<ListResponse<Ad>>(activity) {
                    @Override
                    public void onNext(ListResponse<Ad> response) {
                        imageBannerPresent.load(response.getResults());
                    }
                });

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
            initCityTitle(view, getString(R.string.tab1), curCity.name);
            loadCourse();
        }
    }

    private DropDownMenu ddm;

    private String headers[] = {"所有类型", "所有年龄"};
    private List<String> types = new ArrayList<>();
    private List<String> ages = new ArrayList<>();
    private String currentType = headers[0];
    private String currentAge = headers[1];
    private List<View> popupViews = new ArrayList<>();

    private ListDropDownAdapter checkinStatusAdapter;
    private ListDropDownAdapter ageAdapter;

    private void initDropDownMenu() {
        ddm = (DropDownMenu) view.findViewById(R.id.ddm);

        //init check in status menu
        final ListView checkinStatusView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        types.add(headers[0]);
        types.addAll(CommonConstants.COURSE_TYPES);
        checkinStatusAdapter = new ListDropDownAdapter(activity, types);
        checkinStatusView.setAdapter(checkinStatusAdapter);

        //init age menu
        final ListView ageView = (ListView) View.inflate(
                activity, R.layout.listview_dropdown_menu, null);
        ages.add(headers[1]);
        ages.addAll(CommonConstants.AGE_RANGES);
        ageAdapter = new ListDropDownAdapter(activity, ages);
        ageView.setAdapter(ageAdapter);

        //init popupViews
        popupViews.add(checkinStatusView);
        popupViews.add(ageView);

        //add item click event
        checkinStatusView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkinStatusAdapter.setCheckItem(position);
                currentType = types.get(position);
                ddm.setTabText(currentType);
                ddm.closeMenu();

                loadCourse();
            }
        });

        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ageAdapter.setCheckItem(position);
                currentAge = ages.get(position);
                ddm.setTabText(currentAge);
                ddm.closeMenu();

                loadCourse();
            }
        });

        View include_refresh_list = View.inflate(activity, R.layout.include_refresh_list, null);
        multiPageLoadPresent = new MultiPageLoadPresent(activity, include_refresh_list.findViewById(R.id.srl));

        //init context view
        include_refresh_list.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        //init dropdownview
        ddm.setDropDownMenu(Arrays.asList(headers), popupViews, include_refresh_list);
    }

    private void loadCourse() {
        datas.clear();

        CourseAdapter adapter = new CourseAdapter(activity, datas);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest<ListResponse<Course>>() {
                    @Override
                    public Observable<ListResponse<Course>> request(int page) {
                        String type = currentType.equals(headers[0]) ? null : currentType;
                        String age = currentAge.equals(headers[1]) ? null : currentAge;
                        return HttpRequest.getCourseByTypeAndAge(curCity.name, page, type, age);
                    }
                },
                new SimpleSubscriber<ListResponse<Course>>(activity) {
                    @Override
                    public void onNext(ListResponse<Course> response) {
                        //
                    }
                });
    }
}
