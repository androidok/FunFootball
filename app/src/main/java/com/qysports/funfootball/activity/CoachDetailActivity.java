package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.PageIndex;
import com.boredream.bdcodehelper.net.MultiPageRequest;
import com.boredream.bdcodehelper.present.MultiPageLoadPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.CoachDetailAdapter;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.CoachComment;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import java.util.ArrayList;
import java.util.HashMap;

import rx.Observable;

public class CoachDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private User coach;
    private MultiPageLoadPresent multiPageLoadPresent;
    private CoachDetailAdapter adapter;

    private ArrayList<CoachComment> allDatas = new ArrayList<>();
    private ArrayList<CoachComment> datas1 = new ArrayList<>();
    private ArrayList<CoachComment> datas2 = new ArrayList<>();
    private ArrayList<CoachComment> datas3 = new ArrayList<>();
    private HashMap<Integer, Boolean> dataLoadedMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_detail);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        coach = (User) getIntent().getSerializableExtra("coach");
        multiPageLoadPresent = new MultiPageLoadPresent(this, findViewById(R.id.srl));
    }

    private void initView() {
        initBackTitle("教练信息").setRightText("测试用评价")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CoachDetailActivity.this, WriteCoachCommentActivity.class);
                        intent.putExtra("coach", coach);
                        startActivity(intent);
                    }
                });
    }

    private void initData() {
        dataLoadedMap.put(0, false);
        dataLoadedMap.put(1, false);
        dataLoadedMap.put(2, false);
        dataLoadedMap.put(3, false);

        adapter = new CoachDetailAdapter(coach, allDatas, this);
        loadData(0, allDatas);
    }

    private void loadData(final int commentType, ArrayList<CoachComment> datas) {
        adapter.setCommentType(commentType);
        adapter.setDatas(datas);

        PageIndex pageIndex = new PageIndex(1, CommonConstants.COUNT_OF_PAGE);

        if(dataLoadedMap.get(commentType)) {
            multiPageLoadPresent.setDatas(datas);
        } else {
            multiPageLoadPresent.load(adapter, datas, pageIndex,
                    new MultiPageRequest<ListResponse<CoachComment>>() {
                        @Override
                        public Observable<ListResponse<CoachComment>> request(int page) {
                            return HttpRequest.getCoachComments(coach.getObjectId(), commentType, page);
                        }
                    },
                    new SimpleSubscriber<ListResponse<CoachComment>>(this) {
                        @Override
                        public void onNext(ListResponse<CoachComment> response) {
                            dataLoadedMap.put(commentType, true);
                        }
                    });
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_coach_comment_all:
                loadData(0, allDatas);
                break;
            case R.id.rb_coach_comment1:
                loadData(1, datas1);
                break;
            case R.id.rb_coach_comment2:
                loadData(2, datas2);
                break;
            case R.id.rb_coach_comment3:
                loadData(3, datas3);
                break;
        }
    }
}
