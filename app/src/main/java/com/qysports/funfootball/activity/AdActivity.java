package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;

public class AdActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_count;
    private Button btn_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        initView();
        initData();
    }

    private void initView() {
        tv_count = (TextView) findViewById(R.id.tv_count);
        btn_skip = (Button)findViewById(R.id.btn_skip);

        btn_skip.setOnClickListener(this);
    }

    private void initData() {
        // 倒计时开始,共3秒,每次减少1秒
        CountDownTimer timer = new CountDownTimer(3999, 1000) {
            @Override
            public void onTick(long l) {
                showLog("onTick " + l);
                String restTime = (int) (l / 1000) + "s";
                tv_count.setText(restTime);
            }

            @Override
            public void onFinish() {
                // 倒计时结束
                intent2Activity(MainActivity.class);
            }
        };
        timer.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_skip:
                intent2Activity(MainActivity.class);
                break;
        }
    }
}
