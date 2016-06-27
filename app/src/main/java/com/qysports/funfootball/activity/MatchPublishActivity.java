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
import com.boredream.bdcodehelper.entity.FormItem;
import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.FormItemsPresent;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Match;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.listener.OnStringSelectedListener;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.present.AgePickerPresent;
import com.qysports.funfootball.present.DatePickerPresent;
import com.qysports.funfootball.present.Image3UploadPresent;
import com.qysports.funfootball.utils.DialogUtils;
import com.qysports.funfootball.utils.StringUtils;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class MatchPublishActivity extends BaseActivity {

    private FormItemsPresent formItemsPresent1;
    private DatePickerPresent datePickerPresent;
    private FormItemsPresent formItemsPresent2;
    private EditText et_desc;
    private Image3UploadPresent image3UploadPresent;
    private Button btn_submit;

    private String ageRange;
    private int matchNum;
    private Integer gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_publish);

        initView();
    }

    private void initView() {
        initBackTitle("自主邀约");

        initFormItem1();
        datePickerPresent = new DatePickerPresent(this, "比赛",
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
        formItems.add(FormItem.getInputItem("赛事名称", "请输入赛事名称"));
        formItems.add(FormItem.getInputItem("比赛地点", "请输入地址"));
        formItemsPresent1 = new FormItemsPresent((LinearLayout) findViewById(R.id.ll_container1));
        formItemsPresent1.load(formItems, new Integer[]{}, null);
    }

    private void initFormItem2() {
        final List<FormItem> formItems = new ArrayList<>();
        formItems.add(FormItem.getSelectItem("性别要求", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getSelectItem("适合年龄", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getSelectItem("比赛类型", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getInputItem("装备要求", "请填写所需的装备要求"));
        formItemsPresent2 = new FormItemsPresent((LinearLayout) findViewById(R.id.ll_container2));
        formItemsPresent2.load(formItems, new Integer[]{}, new FormItemsPresent.OnSelectItemClickListener() {
            @Override
            public void onSelectItemClick(String leftText) {
                switch (leftText) {
                    case "性别要求":
                        showGenderPickerDialog(leftText);
                        break;
                    case "适合年龄":
                        showAgePickerDialog(leftText);
                        break;
                    case "比赛类型":
                        showMatchNumDialog(leftText);
                        break;
                }
            }
        });
    }

    private void showGenderPickerDialog(final String leftText) {
        String[] genders = CommonConstants.MATCH_GENDERS.values().toArray(
                new String[CommonConstants.MATCH_GENDERS.size()]);
        DialogUtils.showSingleChoiceDialog(this, genders,
                new OnStringSelectedListener() {
                    @Override
                    public void onStringSelected(String str) {
                        gender = StringUtils.getMatchGenderNum(str);
                        formItemsPresent2.showMidText(leftText, str);
                    }
                });

    }

    private void showAgePickerDialog(String leftText) {
        LinearLayout ll_mid_container = formItemsPresent2.showMidContainer(leftText);
        final AgePickerPresent agePickerPresent = new AgePickerPresent(ll_mid_container);
        agePickerPresent.showAgePickerDialog(new OnStringSelectedListener() {
            @Override
            public void onStringSelected(String str) {
                ageRange = str;
                agePickerPresent.setAgeRange(ageRange);
            }
        });
    }

    private void showMatchNumDialog(final String leftText) {
        DialogUtils.showMatchNumDialog(this, new OnStringSelectedListener() {
            @Override
            public void onStringSelected(String str) {
                try {
                    matchNum = Integer.parseInt(str);
                } catch (Exception e) {
                    matchNum = 11;
                }
                formItemsPresent2.showMidText(leftText, str);
            }
        });
    }

    /**
     * TODO 输入验证,可根据需要自行修改补充
     */
    private void submit() {
        // 开始验证输入内容
        if (!formItemsPresent1.validate()) {
            return;
        }

        if (!datePickerPresent.validate()) {
            return;
        }

        if (!formItemsPresent2.validate()) {
            return;
        }

        String desc = et_desc.getText().toString().trim();
        if (TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "比赛简介不能为空", Toast.LENGTH_SHORT).show();
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

        Match match = new Match();
        match.setCity(AddressData.currentCity.name);
        match.setCoach(currentUser);
        match.setTitle(formItemsPresent1.getString("赛事名称"));
        match.setLocation(formItemsPresent1.getString("比赛地点"));
        match.setDate(datePickerPresent.getDateString());
        match.setGender(gender);
        match.setMatchNum(matchNum);
        match.setRange(ageRange);
        match.setEquip(formItemsPresent2.getString("装备要求"));
        match.setDesc(desc);

        showProgressDialog();
        uploadImage(match);
    }

    private void uploadImage(final Match match) {
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

                match.setImgUrls(sb.toString());

                publicMatch(match);
            }

            @Override
            public void onError(Throwable e) {
                dismissProgressDialog();
                showToast("比赛发布失败");
            }
        });
    }

    private void publicMatch(Match match) {
        Observable<BaseEntity> observable = HttpRequest.getApiService().addMatch(match);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                    @Override
                    public void onNext(BaseEntity baseEntity) {
                        dismissProgressDialog();
                        showToast("比赛发布成功");
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
