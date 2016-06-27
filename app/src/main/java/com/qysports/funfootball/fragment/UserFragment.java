package com.qysports.funfootball.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.boredream.bdcodehelper.entity.SettingItem;
import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.boredream.bdcodehelper.utils.TitleBuilder;
import com.boredream.bdcodehelper.view.DividerItemDecoration;
import com.boredream.bdcodehelper.view.EmptyItemDecoration;
import com.qysports.funfootball.R;
import com.qysports.funfootball.activity.CoachDetailActivity;
import com.qysports.funfootball.activity.CourseOrderActivity;
import com.qysports.funfootball.activity.MyCollectActivity;
import com.qysports.funfootball.activity.MyPublishActivity;
import com.qysports.funfootball.activity.SettingActivity;
import com.qysports.funfootball.adapter.MoreRecyclerAdapter;
import com.qysports.funfootball.base.BaseFragment;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private RecyclerView rv_more;
    private MoreRecyclerAdapter adapter;
    private List<SettingItem> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(activity, R.layout.frag_more, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        new TitleBuilder(view).setTitleText(getString(R.string.tab4));

        rv_more = (RecyclerView) view.findViewById(R.id.rv_more);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false);
        rv_more.setLayoutManager(linearLayoutManager);
        // 每个item之间的分割线
        rv_more.addItemDecoration(new DividerItemDecoration(activity));
    }

    private void initData() {
        User currentUser = UserInfoKeeper.getCurrentUser();
        items = new ArrayList<>();

        if (currentUser.getRole() == User.ROLE_COACH) {
            items.add(new SettingItem(
                    R.mipmap.ic_my_coachdata,
                    "教练资料",
                    null,
                    -1
            ));
            items.add(new SettingItem(
                    R.mipmap.ic_my_order,
                    "我的发起",
                    null,
                    -1
            ));
            items.add(new SettingItem(
                    R.mipmap.ic_my_collect,
                    "我的收藏",
                    null,
                    -1
            ));
            items.add(new SettingItem(
                    R.mipmap.ic_my_setup,
                    "设置",
                    null,
                    -1
            ));

            // 每组item之间的分割间隔
            rv_more.addItemDecoration(new EmptyItemDecoration(
                    new Integer[]{0, 3}, DisplayUtils.dp2px(activity, 16)));
        } else {
            items.add(new SettingItem(
                    R.mipmap.ic_my_order,
                    "课程订单",
                    null,
                    -1
            ));
            items.add(new SettingItem(
                    R.mipmap.ic_my_collect,
                    "我的收藏",
                    null,
                    -1
            ));
            items.add(new SettingItem(
                    R.mipmap.ic_my_setup,
                    "设置",
                    null,
                    -1
            ));

            // 每组item之间的分割间隔
            rv_more.addItemDecoration(new EmptyItemDecoration(
                    new Integer[]{0, 2}, DisplayUtils.dp2px(activity, 16)));
        }

        adapter = new MoreRecyclerAdapter(items, UserInfoKeeper.getCurrentUser(), this);
        rv_more.setAdapter(adapter);

        // 如果未登录进入本页面,然后跳转登录页面成功后返回,此时应该再次更新用户信息
        adapter.setUser(UserInfoKeeper.getCurrentUser());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == -1) {
            // 给特殊的用户信息header位置设为了position=-1的item click事件
            // intent2Activity(UserInfoEditActivity.class);
        } else if (position >= 0) {
            String itemName = items.get(position).midText;
            if (itemName.equals("课程订单")) {
                intent2Activity(CourseOrderActivity.class);
            } else if (itemName.equals("教练资料")) {
                intent2Activity(CoachDetailActivity.class);
            } else if (itemName.equals("我的发起")) {
                intent2Activity(MyPublishActivity.class);
            } else if (itemName.equals("我的收藏")) {
                intent2Activity(MyCollectActivity.class);
            } else if (itemName.equals("设置")) {
                intent2Activity(SettingActivity.class);
            }
        }
    }
}
