package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.listener.OnStringSelectedListener;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.present.AgePickerPresent;
import com.qysports.funfootball.present.CourseTypePickerPresent;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.present.Image3UploadPresent;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.List;

import rx.Observable;

public class CoursePublishActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_title;
    private EditText et_location;
    private EditText et_person_num;
    private EditText et_cost;
    private LinearLayout ll_age_range;
    private LinearLayout ll_age_range_content;
    private AgePickerPresent agePickerPresent;
    private LinearLayout ll_type;
    private LinearLayout ll_type_content;
    private CourseTypePickerPresent courseTypePickerPresent;
    private EditText et_equip;
    private EditText et_desc;
    private Button btn_submit;

    private Image3UploadPresent image3UploadPresent;
    private DatePickerPresent datePickerPresent;
    private String ageRange;
    private String courseTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_publish);

        initView();
//        mockData();
    }

    private void mockData() {
        et_title.setText("课程" + System.currentTimeMillis());
        et_location.setText("静安区工人体育场");
        et_person_num.setText("4");
        et_cost.setText("2000.00");
        et_equip.setText("穿着钱宝衣服");
        et_desc.setText("如果有下雨天则延期");
    }

    private void initView() {
        initBackTitle("发布课程");
        image3UploadPresent = new Image3UploadPresent(this, findViewById(R.id.include_upload_3image));
        datePickerPresent = new DatePickerPresent(this, "课程", findViewById(R.id.include_range_date));

        et_title = (EditText) findViewById(R.id.et_title);
        et_location = (EditText) findViewById(R.id.et_location);
        et_person_num = (EditText) findViewById(R.id.et_person_num);
        et_cost = (EditText) findViewById(R.id.et_cost);
        ll_age_range = (LinearLayout) findViewById(R.id.ll_age_range);
        ll_age_range_content = (LinearLayout) findViewById(R.id.ll_age_range_content);
        agePickerPresent = new AgePickerPresent(ll_age_range_content);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        ll_type_content = (LinearLayout) findViewById(R.id.ll_type_content);
        courseTypePickerPresent = new CourseTypePickerPresent(ll_type_content);
        et_equip = (EditText) findViewById(R.id.et_equip);
        et_desc = (EditText) findViewById(R.id.et_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        ll_age_range.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    /**
     * TODO 输入验证,可根据需要自行修改补充
     */
    private void submit() {
        // 开始验证输入内容
        String title = et_title.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "title不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (datePickerPresent.getStartDate() == null || datePickerPresent.getStartDate() == null) {
            showToast("课程日期不能为空");
            return;
        }

        if (datePickerPresent.getStartTime() == null || datePickerPresent.getEndTime() == null) {
            showToast("课程时间不能为空");
            return;
        }

        if (datePickerPresent.getSelectedWeek().size() == 0) {
            showToast("请选择每周需要哪几天上课");
            return;
        }

        String location = et_location.getText().toString().trim();
        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "location不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String num = et_person_num.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            Toast.makeText(this, "num不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Integer personNum = Integer.parseInt(num);
        if (personNum <= 0) {
            showToast("请输入正确的人数");
            return;
        }

        String costStr = et_cost.getText().toString().trim();
        if (TextUtils.isEmpty(costStr)) {
            Toast.makeText(this, "cost不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Float cost = Float.parseFloat(costStr);
        if (personNum <= 0) {
            showToast("请输入正确的费用");
            return;
        }

        String equip = et_equip.getText().toString().trim();
        if (TextUtils.isEmpty(equip)) {
            Toast.makeText(this, "equip不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String desc = et_desc.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "desc不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (image3UploadPresent.getImgUris() == null || image3UploadPresent.getImgUris().size() == 0) {
            showToast("请至少上传一张图片");
            return;
        }

        // 新建评论对象
        User currentUser = UserInfoKeeper.getCurrentUser();
        currentUser.set__type(Pointer.TYPE);
        currentUser.setClassName("_User");

        Course course = new Course();
        course.setCity(AddressData.currentCity.name);
        course.setCoach(currentUser);
        course.setTitle(title);
        course.setType(courseTypes);
        course.setDate(datePickerPresent.getDateString());
        course.setWeeks(datePickerPresent.getWeekStr());
        course.setLocation(location);
        course.setPersonNum(personNum);
        course.setCost(cost);
        course.setRange(ageRange);
        course.setEquip(equip);
        course.setDesc(desc);

        showProgressDialog();
        uploadImage(course);
    }

    private void uploadImage(final Course course) {
        image3UploadPresent.upload(new Image3UploadPresent.OnAllUploadedListener() {
            @Override
            public void onAllUploaded(List<FileUploadResponse> responses) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < responses.size(); i++) {
                    if (i > 0) {
                        sb.append(CommonConstants.SEPARATOR.IMAGE_UPLOAD_URLS);
                    }
                    sb.append(HttpRequest.FILE_HOST + responses.get(i).getUrl());
                }

                course.setImgUrls(sb.toString());

                publicCourse(course);
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                showToast("课程发布失败");
            }
        });
    }

    private void publicCourse(Course course) {
        Observable<BaseEntity> observable = HttpRequest.getApiService().addCourse(course);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();
                        showToast("课程发布成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_age_range:
                agePickerPresent.showAgePickerDialog(new OnStringSelectedListener() {
                    @Override
                    public void onStringSelected(String str) {
                        ageRange = str;
                        agePickerPresent.setAgeRange(ageRange);
                    }
                });
                break;
            case R.id.ll_type:
                courseTypePickerPresent.showTypePickerDialog(new OnStringSelectedListener() {
                    @Override
                    public void onStringSelected(String str) {
                        courseTypes = str;
                        courseTypePickerPresent.setTypes(courseTypes);
                    }
                });
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        image3UploadPresent.handleActivityResult(requestCode, resultCode, data);
    }
}
