package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.DateUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.entity.CoachComment;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.Date;

import rx.Observable;

public class WriteCoachCommentActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_comment;
    private Button btn_submit;

    private User coach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_coach_comment);

        initExtras();
        initView();
    }

    private void initExtras() {
        coach = (User) getIntent().getSerializableExtra("coach");
    }

    private void initView() {
        initBackTitle("教练评价");

        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
    }

    /**
     * TODO 输入验证,可根据需要自行修改补充
     */
    private void submit() {
        // 开始验证输入内容
        String comment = et_comment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            Toast.makeText(this, "comment不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        User currentUser = UserInfoKeeper.getCurrentUser();
        currentUser.set__type(Pointer.TYPE);
        currentUser.setClassName("_User");

        CoachComment coachComment = new CoachComment();
        coachComment.setCoachUserId(coach.getObjectId());
        coachComment.setUser(currentUser);
        coachComment.setDate(DateUtils.getDate(DateUtils.date2str(new Date())));
        coachComment.setContent(comment);
        Observable<BaseEntity> observable = HttpRequest.getApiService().addCoachComment(coachComment);

        ObservableDecorator.decorate(this, observable).subscribe(
                new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        showToast("评价成功");
                        finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }

}
