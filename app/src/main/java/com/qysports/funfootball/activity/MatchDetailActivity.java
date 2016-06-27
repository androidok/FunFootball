package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.present.ImageBannerPresent;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.entity.ActOrder;
import com.qysports.funfootball.entity.Match;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.utils.StringUtils;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.ArrayList;

public class MatchDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUSET_CODE_SIGNUP = 110;

    private TextView tv_coach_name;
    private Button btn_coach_detail;
    private TextView tv_title;
    private TextView tv_address;
    private TextView tv_gender;
    private LinearLayout ll_age_range;
    private LinearLayout ll_match_num;
    private TextView tv_equip;
    private TextView tv_desc;
    private Button btn_sign_up;

    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        match = (Match) getIntent().getSerializableExtra("match");
    }

    private void initView() {
        initBackTitle("赛事详情")
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
        tv_gender = (TextView) findViewById(R.id.tv_gender);
        ll_age_range = (LinearLayout) findViewById(R.id.ll_age_range);
        ll_match_num = (LinearLayout) findViewById(R.id.ll_match_num);
        tv_equip = (TextView) findViewById(R.id.tv_equip);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        btn_sign_up = (Button) findViewById(R.id.btn_sign_up);

        btn_coach_detail.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
    }

    private void initData() {
        setBanner();

        tv_coach_name.setText(match.getCoach().getNickname());
        tv_title.setText(match.getTitle());
        tv_address.setText(match.getLocation());
        tv_gender.setText(StringUtils.getGenderStr(match.getGender()));
        new DatePickerPresent(this, "赛事", findViewById(R.id.include_range_date)).showDateInfo(match);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById(R.id.ll_date).getLayoutParams();
        layoutParams.width = 0;
        layoutParams.weight = 1;
        setAgeRange();
        setMatchNum();
        tv_equip.setText(match.getEquip());
        tv_desc.setText(match.getDesc());

        getSignUpUsers();
    }

    private void setAgeRange() {
        ll_age_range.removeAllViews();
        ArrayList<String> ageRanges = match.getAgeRanges();
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

    private void setMatchNum() {
        ll_match_num.removeAllViews();
        ArrayList<String> nums = new ArrayList<>();
        nums.add(match.getMatchNumStr());
        for(String num : nums) {
            TextView tv = (TextView) View.inflate(this, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_match_num.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(this, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(num);
            ll_match_num.addView(tv);
        }
    }

    public void getSignUpUsers() {
//        showProgressDialog();
//        Observable<ListResponse<ActOrder>> observable = HttpRequest.getOrderOfAct(match);
//        ObservableDecorator.decorate(this, observable)
//                .subscribe(new SimpleSubscriber<ListResponse<ActOrder>>(this) {
//                    @Override
//                    public void onNext(ListResponse<ActOrder> response) {
//                        handleActOrder(response);
//                        dismissProgressDialog();
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        showLog("报名人数获取失败，可以提交报名时再次验证");
//                        dismissProgressDialog();
//                    }
//                });
    }

    private void share() {
        if(true) {
            showToast("敬请期待");
            return;
        }

//        String title = "分享标题";
//        String content = "分享内容";
//        UMImage image = new UMImage(this, match.getImageList().get(0).getImageUrl());
//        UmengShareUtils.share(this, title, content, image);
    }

    private void handleActOrder(ListResponse<ActOrder> response) {
        ArrayList<ActOrder> results = response.getResults();

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
                .load(match.getImageList());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_coach_detail:
                intent = new Intent(MatchDetailActivity.this, CoachDetailActivity.class);
                intent.putExtra("coach", match.getCoach());
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                intent = new Intent(MatchDetailActivity.this, SignUpActivity.class);
                intent.putExtra("match", match);
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
