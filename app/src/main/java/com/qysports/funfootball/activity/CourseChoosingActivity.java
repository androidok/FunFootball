package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.CourseAdapter;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import java.util.ArrayList;

import rx.Observable;

public class CourseChoosingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_area;
    private TextView tv_age;
    private TextView tv_apply;

    private MultiPageLoadPresent multiPageLoadPresent;
    private ArrayList<Course> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_choosing);

        initView();
        initData();
    }

    private void initView() {
        initBackTitle("选课");

        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_apply = (TextView) findViewById(R.id.tv_apply);
        tv_area.setOnClickListener(this);
        tv_age.setOnClickListener(this);
        tv_apply.setOnClickListener(this);

        multiPageLoadPresent = new MultiPageLoadPresent(this, findViewById(R.id.srl));
    }

    private void initData() {
        CourseAdapter adapter = new CourseAdapter(this, datas);
        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);
        multiPageLoadPresent.load(adapter, datas, pageIndex,
                new MultiPageRequest() {
                    @Override
                    public Observable request(int page) {
                        return HttpRequest.getCourse(AddressData.currentCity.name, page);
                    }
                }, new SimpleSubscriber(this) {
                    //
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_area:

                break;
            case R.id.tv_age:

                break;
            case R.id.tv_apply:

                break;
            default:

                break;
        }
    }
}
