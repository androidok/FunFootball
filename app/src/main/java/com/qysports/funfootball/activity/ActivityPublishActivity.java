package com.qysports.funfootball.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boredream.bdcodehelper.db.AddressData;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.FormItem;
import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.FormItemsPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.listener.OnStringSelectedListener;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.present.ActTypePresent;
import com.qysports.funfootball.present.AgePickerPresent;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.present.Image3UploadPresent;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ActivityPublishActivity extends BaseActivity {

    private FormItemsPresent formItemsPresent1;
    private DatePickerPresent datePickerPresent;
    private FormItemsPresent formItemsPresent2;
    private EditText et_desc;
    private Image3UploadPresent image3UploadPresent;
    private Button btn_submit;

    private String ageRange;
    private String actTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_publish);

        initView();
    }

    private void initView() {
        initBackTitle("发布活动");

        initFormItem1();
        datePickerPresent = new DatePickerPresent(this, "活动",
                findViewById(R.id.include_range_date),
                CalendarPickerView.SelectionMode.SINGLE);
        initFormItem2();
        image3UploadPresent = new Image3UploadPresent(this, findViewById(R.id.include_upload_3image));
        et_desc = (EditText) findViewById(R.id.et_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void initFormItem1() {
        List<FormItem> formItems = new ArrayList<>();
        formItems.add(FormItem.getInputItem("活动名称", "请输入活动名称"));
        formItems.add(FormItem.getInputItem("活动地点", "请输入地址"));
        formItemsPresent1 = new FormItemsPresent((LinearLayout) findViewById(R.id.ll_container1));
        formItemsPresent1.load(formItems, new Integer[]{}, null);
    }

    private void initFormItem2() {
        final List<FormItem> formItems = new ArrayList<>();
        formItems.add(FormItem.getInputItem("活动人数", "请填写人数", InputType.TYPE_CLASS_NUMBER));
        formItems.add(FormItem.getSelectItem("适合年龄", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getSelectItem("活动类型", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getInputItem("装备要求", "请填写所需的装备要求"));
        formItemsPresent2 = new FormItemsPresent((LinearLayout) findViewById(R.id.ll_container2));
        formItemsPresent2.load(formItems, new Integer[]{}, new FormItemsPresent.OnSelectItemClickListener() {
            @Override
            public void onSelectItemClick(String leftText) {
                switch (leftText) {
                    case "适合年龄":
                        showAgePickerDialog(leftText);
                        break;
                    case "活动类型":
                        showCourseTypePickerDialog(leftText);
                        break;
                }
            }
        });
    }

    private AgePickerPresent agePickerPresent;
    private void showAgePickerDialog(String age) {
        LinearLayout ll_mid_container = formItemsPresent2.showMidContainer(age);
        agePickerPresent = new AgePickerPresent(ll_mid_container);
        agePickerPresent.showAgePickerDialog(new OnStringSelectedListener() {
            @Override
            public void onStringSelected(String str) {
                ageRange = str;
                agePickerPresent.setAgeRange(ageRange);
            }
        });
    }

    private ActTypePresent actTypePickerPresent;
    private void showCourseTypePickerDialog(String type) {
        LinearLayout ll_mid_container = formItemsPresent2.showMidContainer(type);
        actTypePickerPresent = new ActTypePresent(ll_mid_container);
        actTypePickerPresent.showTypePickerDialog(new OnStringSelectedListener() {
            @Override
            public void onStringSelected(String str) {
                actTypes = str;
                actTypePickerPresent.setTypes(actTypes);
            }
        });
    }

    /**
     * TODO 输入验证,可根据需要自行修改补充
     */
    private void submit() {
        // 开始验证输入内容
        if(!formItemsPresent1.validate()) {
            return;
        }

        if(!datePickerPresent.validate()) {
            return;
        }

        if(!formItemsPresent2.validate()) {
            return;
        }

        String num = formItemsPresent2.getString("活动人数");
        Integer personNum = Integer.parseInt(num);
        if (personNum <= 0) {
            showToast("请输入正确的人数");
            return;
        }

        String desc = et_desc.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "活动简介不能为空", Toast.LENGTH_SHORT).show();
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

        Act act = new Act();
        act.setCity(AddressData.currentCity.name);
        act.setCoach(currentUser);
        act.setTitle(formItemsPresent1.getString("活动名称"));
        act.setLocation(formItemsPresent1.getString("活动地点"));
        act.setDate(datePickerPresent.getDateString());
        act.setPersonNum(personNum);
        act.setType(actTypes);
        act.setRange(ageRange);
        act.setEquip(formItemsPresent2.getString("装备要求"));
        act.setDesc(desc);

        showProgressDialog();
        uploadImage(act);
    }

    private void uploadImage(final Act act) {
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

                act.setImgUrls(sb.toString());

                publicAct(act);
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                showToast("活动发布失败");
            }
        });
    }

    private void publicAct(Act act) {
        Observable<BaseEntity> observable = HttpRequest.getApiService().addAct(act);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();
                        showToast("活动发布成功");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        image3UploadPresent.handleActivityResult(requestCode, resultCode, data);
    }
}
