package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.utils.StringUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.ActOrder;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.CourseOrder;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.utils.UserInfoKeeper;

import rx.Observable;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_parent_name;
    private EditText et_student_name;
    private EditText et_age;
    private EditText et_phone;
    private RelativeLayout rl_cost;
    private TextView tv_cost;
    private Button btn_next;

    private Course course;
    private Act act;
    private String parentName;
    private String studentName;
    private int ageNum;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initExtras();
        initView();
        initData();
    }

    private void initExtras() {
        Bundle extras = getIntent().getExtras();
        if(extras.get("course") != null) {
            course = (Course) getIntent().getSerializableExtra("course");
        } else {
            act = (Act) getIntent().getSerializableExtra("act");
        }
    }

    private void initView() {
        initBackTitle("我要报名");

        et_parent_name = (EditText) findViewById(R.id.et_parent_name);
        et_student_name = (EditText) findViewById(R.id.et_student_name);
        et_age = (EditText) findViewById(R.id.et_age);
        et_phone = (EditText) findViewById(R.id.et_phone);
        rl_cost = (RelativeLayout) findViewById(R.id.rl_cost);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);
    }

    private void initData() {
        if(course != null) {
            tv_cost.setText(StringUtils.getMoney(course.getCost()));
        } else {
            rl_cost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                submit();
                break;
        }
    }

    private void submit() {
        // 开始验证输入内容
        parentName = et_parent_name.getText().toString().trim();
        if (TextUtils.isEmpty(parentName)) {
            Toast.makeText(this, "家长姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        studentName = et_student_name.getText().toString().trim();
        if (TextUtils.isEmpty(studentName)) {
            Toast.makeText(this, "学员姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String age = et_age.getText().toString().trim();
        if (TextUtils.isEmpty(age)) {
            Toast.makeText(this, "学员年龄不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ageNum = Integer.parseInt(age);
        if (ageNum <= 0) {
            showToast("请输入正确的年龄");
            return;
        }

        phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(course != null) {
            signUpCourse();
        } else {
            signUpAct();
        }
    }

    private void signUpCourse() {
        course.set__type(Pointer.TYPE);
        course.setClassName("Course");

        User currentUser = UserInfoKeeper.getCurrentUser();
        currentUser.set__type(Pointer.TYPE);
        currentUser.setClassName("_User");

        CourseOrder order = new CourseOrder();
        order.setUser(currentUser);
        order.setCourse(course);
        order.setParentName(parentName);
        order.setStudentName(studentName);
        order.setAge(ageNum);
        order.setPhone(phone);

        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.signUpCourse(order);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();
                        showToast("报名成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    private void signUpAct() {
        act.set__type(Pointer.TYPE);
        act.setClassName("Activity");

        User currentUser = UserInfoKeeper.getCurrentUser();
        currentUser.set__type(Pointer.TYPE);
        currentUser.setClassName("_User");

        ActOrder order = new ActOrder();
        order.setUser(currentUser);
        order.setAct(act);
        order.setParentName(parentName);
        order.setStudentName(studentName);
        order.setAge(ageNum);
        order.setPhone(phone);

        showProgressDialog();
        Observable<BaseEntity> observable = HttpRequest.signUpAct(order);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();
                        showToast("报名成功");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

}
