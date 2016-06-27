package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.ImageBannerPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.ActOrder;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.ArrayList;

import rx.Observable;

public class ActDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUSET_CODE_SIGNUP = 110;

    private TextView tv_coach_name;
    private Button btn_coach_detail;
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_person_num;
    private LinearLayout ll_age_range;
    private LinearLayout ll_type;
    private TextView tv_equip;
    private TextView tv_desc;
    private Button btn_sign_up;

    private Act act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detail);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        act = (Act) getIntent().getSerializableExtra("act");
    }

    private void initView() {
        initBackTitle("活动详情")
                .setRightText("分享")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        share();
                    }
                });
        tv_coach_name = (TextView) findViewById(R.id.tv_coach_name);
        btn_coach_detail = (Button) findViewById(R.id.btn_coach_detail);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_person_num = (TextView) findViewById(R.id.tv_person_num);
        ll_age_range = (LinearLayout) findViewById(R.id.ll_age_range);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        tv_equip = (TextView) findViewById(R.id.tv_equip);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        btn_coach_detail.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    private void initData() {
        setBanner();

        tv_coach_name.setText(act.getCoach().getNickname());
        tv_title.setText(act.getTitle());
        tv_address.setText(act.getLocation());
        new DatePickerPresent(this, "课程", findViewById(R.id.include_range_date)).showDateInfo(act);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.ll_date).getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        tv_person_num.setText("0 / " + act.getPersonNum());
        setAgeRange();
        setTypes();
        tv_equip.setText(act.getEquip());
        tv_desc.setText(act.getDesc());

        getSignUpUsers();
    }

    private void setAgeRange() {
        ll_age_range.removeAllViews();
        ArrayList<String> ageRanges = act.getAgeRanges();
        for(String age : ageRanges) {
            TextView tv = (TextView) View.inflate(this, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_age_range.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(this, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(age);
            ll_age_range.addView(tv);
        }
    }

    private void setTypes() {
        ll_type.removeAllViews();
        ArrayList<String> types = act.getTypes();
        for(String type : types) {
            TextView tv = (TextView) View.inflate(this, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_type.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(this, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(type);
            ll_type.addView(tv);
        }
    }

    public void getSignUpUsers() {
        showProgressDialog();
        Observable<ListResponse<ActOrder>> observable = HttpRequest.getOrderOfAct(act);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<ListResponse<ActOrder>>(this) {
                    @Override
                    public void onNext(ListResponse<ActOrder> response) {
                        handleActOrder(response);
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
        if(true) {
            showToast("敬请期待");
            return;
        }

//        String title = "分享标题";
//        String content = "分享内容";
//        UMImage image = new UMImage(this, act.getImageList().get(0).getImageUrl());
//        UmengShareUtils.share(this, title, content, image);
    }

    private void handleActOrder(ListResponse<ActOrder> response) {
        ArrayList<ActOrder> results = response.getResults();
        tv_person_num.setText(results.size() + " / " + act.getPersonNum());

        if (results.size() == act.getPersonNum()) {
            btn_sign_up.setEnabled(false);
            btn_sign_up.setText("报名人数已满");
        } else if (isSignUped(results)) {
            btn_sign_up.setEnabled(false);
            btn_sign_up.setText("已报名");
        } else {
            btn_sign_up.setEnabled(true);
        }
    }

    private boolean isSignUped(ArrayList<ActOrder> results) {
        boolean isSignUped = false;
        for (ActOrder order : results) {
            if (order.getUser().getObjectId().equals(UserInfoKeeper.getCurrentUser().getObjectId())) {
                isSignUped = true;
                break;
            }
        }
        return isSignUped;
    }

    private void setBanner() {
        new ImageBannerPresent(this, findViewById(R.id.include_banner_with_indicator))
                .load(act.getImageList());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_coach_detail:
                intent = new Intent(ActDetailActivity.this, CoachDetailActivity.class);
                intent.putExtra("coach", act.getCoach());
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(ActDetailActivity.this, SignUpActivity.class);
                intent.putExtra("act", act);
                startActivityForResult(intent, REQUSET_CODE_SIGNUP);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUSET_CODE_SIGNUP:
                getSignUpUsers();
                break;
        }
    }

}
