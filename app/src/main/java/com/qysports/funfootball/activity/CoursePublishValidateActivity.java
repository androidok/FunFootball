package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.view.View;

import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;

public class CoursePublishValidateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_publish_validate);

        initView();
    }

    private void initView() {
        initBackTitle("发布课程");

        findViewById(R.id.btn_coach_apply).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent2Activity(CoachApplyActivity.class);
                        finish();
                    }
                });
    }
}
