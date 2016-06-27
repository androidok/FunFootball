package com.qysports.funfootball.activity;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.fragment.FragmentController;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.fragment.ActivityFragment;
import com.qysports.funfootball.fragment.CourseFragment;
import com.qysports.funfootball.fragment.MatchFragment;
import com.qysports.funfootball.fragment.UserFragment;
import com.qysports.funfootball.utils.UpdateUtils;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.qysports.funfootball.views.AddDialog;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;


public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_bottom_tab;
    private RadioButton rb_course;
    private ImageView btn_add;
    private AddDialog addDialog;
    private FragmentController controller;

    private BroadcastReceiver onCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onCompleteReceiver = UpdateUtils.registCompleteListener(this,
                new UpdateUtils.OnDownloadCompleteListener() {
                    @Override
                    public void onDownloadComplete(Uri loadedFileUri) {
                        AppUtils.promptInstall(MainActivity.this, loadedFileUri);
                    }
                });
    }

    @Override
    protected void onStop() {
        unregisterReceiver(onCompleteReceiver);
        super.onStop();
    }

    private void initView() {
        rg_bottom_tab = (RadioGroup) findViewById(R.id.rg_bottom_tab);
        rb_course = (RadioButton) findViewById(R.id.rb_course);
        btn_add = (ImageView) findViewById(R.id.btn_add);

        rg_bottom_tab.setOnCheckedChangeListener(this);
        btn_add.setOnClickListener(this);
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new CourseFragment());
        fragments.add(new MatchFragment());
        fragments.add(new ActivityFragment());
        fragments.add(new UserFragment());
        controller = new FragmentController(this, R.id.fl_content, fragments);
        // 默认选择fragment
        rb_course.setChecked(true);
        controller.showFragment(0);

        getCollectCourse(null);

        UpdateUtils.checkUpdate(this, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_course:
                controller.showFragment(0);
                break;
            case R.id.rb_match:
                controller.showFragment(1);
                break;
            case R.id.rb_activity:
                controller.showFragment(2);
                break;
            case R.id.rb_user:
                controller.showFragment(3);
                break;
            case R.id.btn_add:
                showAddDialog();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                showAddDialog();
                break;
        }
    }

    private void showAddDialog() {
        final User currentUser = UserInfoKeeper.getCurrentUser();
        addDialog = new AddDialog(this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_publish1:
                        addDialog.dismiss();
                        if (currentUser.getRole() == User.ROLE_COACH_APPLYING) {
                            showToast("您已经提交了教练申请，请耐心等待审核");
                        } else if (currentUser.getRole() == User.ROLE_COACH) {
                            showToast("您当前已经是教练身份");
                        } else {
                            intent2Activity(CoachApplyActivity.class);
                        }
                        break;
                    case R.id.ll_publish2:
                        addDialog.dismiss();
                        if (currentUser.getRole() != User.ROLE_COACH) {
                            showToast("只有教练才能发布课程");
                        } else {
                            intent2Activity(CoursePublishActivity.class);
                        }
                        break;
                    case R.id.ll_publish3:
                        addDialog.dismiss();
                        if (currentUser.getRole() != User.ROLE_COACH) {
                            showToast("只有教练才能发布活动");
                        } else {
                            intent2Activity(ActivityPublishActivity.class);
                        }
                        break;
                    case R.id.ll_publish4:
                        addDialog.dismiss();
                        if (currentUser.getRole() != User.ROLE_COACH) {
                            showToast("只有教练才能发起自主邀约");
                        } else {
                            intent2Activity(MatchPublishActivity.class);
                        }
                        break;
                }
            }
        });
        addDialog.show();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        // 双击返回键关闭程序
        // 如果两秒重置时间内再次点击返回,则退出程序
        if (doubleBackToExitPressedOnce) {
            exit();
            return;
        }

        doubleBackToExitPressedOnce = true;
        showToast("再按一次返回键关闭程序");
        Observable.just(null)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        // 延迟两秒后重置标志位为false
                        doubleBackToExitPressedOnce = false;
                    }
                });
    }

}
