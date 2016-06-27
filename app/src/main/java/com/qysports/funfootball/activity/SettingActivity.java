package com.qysports.funfootball.activity;

import android.content.BroadcastReceiver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.boredream.bdcodehelper.adapter.SettingRecyclerAdapter;
import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.utils.AppUtils;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.utils.UpdateUtils;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.ArrayList;
import java.util.List;


public class SettingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private RecyclerView rv_setting;
    private Button btn_logout;

    private SettingRecyclerAdapter adapter;

    private BroadcastReceiver onCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
                        AppUtils.promptInstall(SettingActivity.this, loadedFileUri);
                    }
                });
    }

    @Override
    protected void onStop() {
        unregisterReceiver(onCompleteReceiver);
        super.onStop();
    }

    private void initView() {
        initBackTitle("设置");

        rv_setting = (RecyclerView) findViewById(R.id.rv_setting);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(this);
    }

    private void initData() {
        // 使用列表显示多个选项条
        List<SettingItem> items = new ArrayList<>();

        items.add(new SettingItem(
                R.mipmap.ic_launcher,
                "检查更新",
                AppUtils.getAppVersionName(this),
                -1
        ));
        items.add(new SettingItem(
                R.mipmap.ic_launcher,
                "意见反馈",
                null,
                -1
        ));

        adapter = new SettingRecyclerAdapter(items, this);
        rv_setting.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        rv_setting.setLayoutManager(linearLayoutManager);
        // 每个item之间的分割线
        rv_setting.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                showToast("检查更新");

                UpdateUtils.checkUpdate(SettingActivity.this, true);
                break;
            case 1:
//                intent2Activity(FeedBackActivity.class);
                showToast("意见反馈");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                // 登出,清理用户数据同时跳转到登录页
                UserInfoKeeper.logout();
                clearIntent2Login();
                break;
        }
    }
}
