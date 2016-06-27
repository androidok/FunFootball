package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.ImageBannerPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.umeng.UmengShareUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.CourseOrder;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;

import rx.Observable;

public class CourseDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUSET_CODE_SIGNUP = 110;

    private TitleBuilder titleBuilder;
    private ImageView iv_share;
    private TextView tv_coach_name;
    private Button btn_coach_detail;
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_person_num;
    private TextView tv_cost;
    private LinearLayout ll_age_range;
    private LinearLayout ll_type;
    private TextView tv_equip;
    private TextView tv_desc;
    private Button btn_sign_up;

    private User currentUser;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        course = (Course) getIntent().getSerializableExtra("course");
    }

    private void initView() {
        titleBuilder = initBackTitle("课程详情")
                .setRightImage(R.mipmap.icon_collect)
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toggleCollect();
                    }
                });
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_coach_name = (TextView) findViewById(R.id.tv_coach_name);
        btn_coach_detail = (Button) findViewById(R.id.btn_coach_detail);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_person_num = (TextView) findViewById(R.id.tv_person_num);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        ll_age_range = (LinearLayout) findViewById(R.id.ll_age_range);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        tv_equip = (TextView) findViewById(R.id.tv_equip);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        iv_share.setOnClickListener(this);
        btn_coach_detail.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    private void initData() {
        currentUser = UserInfoKeeper.getCurrentUser();

        setBanner();

        tv_coach_name.setText(course.getCoach().getNickname());
        tv_title.setText(course.getTitle());
        tv_address.setText(course.getLocation());
        new DatePickerPresent(this, "课程", findViewById(R.id.include_range_date)).showDateInfo(course);
        tv_person_num.setText("0 / " + course.getPersonNum());
        tv_cost.setText(StringUtils.getMoney(course.getCost()));
        setAgeRange();
        setTypes();
        tv_equip.setText(course.getEquip());
        tv_desc.setText(course.getDesc());

        getSignUpUsers();
        if (currentUser.getCollectCourseList() == null) {
            getCollectCourse(new SimpleSubscriber<ListResponse<Course>>(this) {
                @Override
                public void onNext(ListResponse<Course> courseListResponse) {
                    setCollectStatus();
                }

                @Override
                public void onError(Throwable throwable) {
                    //
                }
            });
        } else {
            setCollectStatus();
        }
    }

    private boolean isCollected() {
        ArrayList<Course> collectCourseList = currentUser.getCollectCourseList();
        if (collectCourseList == null) {
            return false;
        }
        return collectCourseList.contains(course);
    }

    private void setCollectStatus() {
        titleBuilder.setRightImage(isCollected() ?
                R.mipmap.icon_collect_pre : R.mipmap.icon_collect);
    }

    private void toggleCollect() {
        showProgressDialog();

        // 如果当前已经是已收藏,则取消收藏；如果未收藏则收藏之
        Observable<BaseEntity> observable = HttpRequest.collectCourse(course, !isCollected());
        ObservableDecorator.decorate(this, observable).subscribe(
                new SimpleSubscriber<BaseEntity>(this) {

                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();

                        String msg;

                        ArrayList<Course> collectCourseList = currentUser.getCollectCourseList();
                        if(collectCourseList == null) {
                            collectCourseList = new ArrayList<>();
                        }

                        if(isCollected()) {
                            // 如果之前已经收藏，接口调用后是成功取消收藏
                            collectCourseList.remove(course);
                            msg = "取消收藏成功";
                        } else {
                            // 如果之前未收藏，接口调用后是成功收藏
                            collectCourseList.add(course);
                            msg = "收藏成功";
                        }
                        currentUser.setCollectCourseList(collectCourseList);
                        setCollectStatus();
                        showToast(msg);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    private void setAgeRange() {
        ll_age_range.removeAllViews();
        ArrayList<String> ageRanges = course.getAgeRanges();
        for (String age : ageRanges) {
            TextView tv = (TextView) View.inflate(this, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (ll_age_range.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(this, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(age);
            ll_age_range.addView(tv);
        }
    }

    private void setTypes() {
        ll_type.removeAllViews();
        ArrayList<String> types = course.getTypes();
        for (String type : types) {
            TextView tv = (TextView) View.inflate(this, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (ll_type.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(this, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(type);
            ll_type.addView(tv);
        }
    }

    public void getSignUpUsers() {
        showProgressDialog();
        Observable<ListResponse<CourseOrder>> observable = HttpRequest.getOrderOfCourse(course);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<ListResponse<CourseOrder>>(this) {
                    @Override
                    public void onNext(ListResponse<CourseOrder> response) {
                        handleCourseOrder(response);
                        dismissProgressDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        showLog("报名人数获取失败，可以提交报名时再次验证");
                        dismissProgressDialog();
                    }
                });
    }

    private void share() {
        String title = "分享标题";
        String content = "分享内容";
        UMImage image = new UMImage(this, course.getImageList().get(0).getImageUrl());
        UmengShareUtils.share(this, title, content, image);
    }

    private void handleCourseOrder(ListResponse<CourseOrder> response) {
        ArrayList<CourseOrder> results = response.getResults();
        tv_person_num.setText(results.size() + " / " + course.getPersonNum());

        if (results.size() == course.getPersonNum()) {
            btn_sign_up.setEnabled(false);
            btn_sign_up.setText("报名人数已满");
        } else if (isSignUped(results)) {
            btn_sign_up.setEnabled(false);
            btn_sign_up.setText("已报名");
        } else {
            btn_sign_up.setEnabled(true);
        }
    }

    private boolean isSignUped(ArrayList<CourseOrder> results) {
        boolean isSignUped = false;
        for (CourseOrder order : results) {
            if (order.getUser().getObjectId().equals(UserInfoKeeper.getCurrentUser().getObjectId())) {
                isSignUped = true;
                break;
            }
        }
        return isSignUped;
    }

    private void setBanner() {
        new ImageBannerPresent(this, findViewById(R.id.include_banner_with_indicator))
                .load(course.getImageList());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_share:
                share();
                break;
            case R.id.btn_coach_detail:
                intent = new Intent(CourseDetailActivity.this, CoachDetailActivity.class);
                intent.putExtra("coach", course.getCoach());
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(CourseDetailActivity.this, SignUpActivity.class);
                intent.putExtra("course", course);
                startActivityForResult(intent, REQUSET_CODE_SIGNUP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUSET_CODE_SIGNUP:
                getSignUpUsers();
                break;
        }
    }

}
