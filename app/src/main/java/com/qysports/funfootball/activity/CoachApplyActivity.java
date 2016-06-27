package com.qysports.funfootball.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.FormItem;
import com.boredream.bdcodehelper.entity.city.CityModel;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.FormItemsPresent;
import com.boredream.bdcodehelper.utils.ImageUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.listener.OnStringSelectedListener;
import com.qysports.funfootball.net.GlideHelper;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.utils.DialogUtils;
import com.qysports.funfootball.utils.StringUtils;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class CoachApplyActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_PICK_CITY = 110;

    private LinearLayout ll_container;
    private FormItemsPresent formItemsPresent;
    private RadioGroup rg_accept_training;
    private RelativeLayout rl_avatar;
    private ImageView iv_add_avatar;
    private ImageView iv_avatar;
    private RelativeLayout rl_cert;
    private ImageView iv_add_cert;
    private ImageView iv_cert;
    private EditText et_career;
    private Button btn_submit;

    private int age;
    private CityModel city;
    private int gender;
    private int trainingYears;
    private Uri avatarUri;
    private Uri certUri;
    private String career;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_apply);

        initView();
    }

    private void initView() {
        initBackTitle("教练加盟");

        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        initFormItem();
        rg_accept_training = (RadioGroup) findViewById(R.id.rg_accept_training);
        rl_avatar = (RelativeLayout) findViewById(R.id.rl_avatar);
        iv_add_avatar = (ImageView) findViewById(R.id.iv_add_avatar);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        rl_cert = (RelativeLayout) findViewById(R.id.rl_cert);
        iv_add_cert = (ImageView) findViewById(R.id.iv_add_cert);
        iv_cert = (ImageView) findViewById(R.id.iv_cert);
        et_career = (EditText) findViewById(R.id.et_career);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        iv_add_avatar.setOnClickListener(this);
        iv_add_cert.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private void initFormItem() {
        List<FormItem> formItems = new ArrayList<>();
        formItems.add(FormItem.getInputItem("教练姓名", "请输入您的姓名"));
        formItems.add(FormItem.getInputItem("年龄", "请输入您的年龄"));
        formItems.add(FormItem.getSelectItem("城市", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getSelectItem("性别", R.mipmap.ic_chevron_right_grey600_24dp));
        formItems.add(FormItem.getInputItem("身份证", "请输入身份证号码"));
        formItems.add(FormItem.getInputItem("执教年龄", "请输入年限"));
        formItems.add(FormItem.getInputItem("证书等级", "请输入证书等级"));
        formItemsPresent = new FormItemsPresent(ll_container);
        formItemsPresent.load(formItems, new Integer[]{}, new FormItemsPresent.OnSelectItemClickListener() {
            @Override
            public void onSelectItemClick(String leftText) {
                switch (leftText) {
                    case "城市":
                        pickCity();
                        break;
                    case "性别":
                        showGenderPickerDialog(leftText);
                        break;
                }
            }
        });
    }

    private void pickCity() {
        Intent intent = new Intent(CoachApplyActivity.this, CityActivity.class);
        intent.putExtra("setSingleChoose", true);
        startActivityForResult(intent, REQUEST_CODE_PICK_CITY);
    }

    private void showGenderPickerDialog(final String leftText) {
        Map<Integer, String> genderMap = CommonConstants.MATCH_GENDERS;
        genderMap.remove(CommonConstants.MATCH_GENDERS_ALL);
        String[] genders = genderMap.values().toArray(new String[genderMap.size()]);
        DialogUtils.showSingleChoiceDialog(this, genders,
                new OnStringSelectedListener() {
                    @Override
                    public void onStringSelected(String str) {
                        gender = StringUtils.getMatchGenderNum(str);
                        formItemsPresent.showMidText(leftText, str);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_avatar:
                ImageUtils.showImagePickDialog(CoachApplyActivity.this);
                break;
            case R.id.iv_add_cert:
                ImageUtils.showImagePickDialog(CoachApplyActivity.this, 1);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    /**
     * TODO 输入验证,可根据需要自行修改补充
     */
    private void submit() {
        if (!formItemsPresent.validate()) {
            return;
        }

        age = Integer.parseInt(formItemsPresent.getString("年龄"));
        if (age <= 0) {
            showToast("请输入正确的年龄");
            return;
        }

        trainingYears = Integer.parseInt(formItemsPresent.getString("执教年龄"));
        if (trainingYears <= 0) {
            showToast("请输入正确的执教年龄");
            return;
        }

        if(avatarUri == null) {
            showToast("请上传个人照片");
            return;
        }

        if(certUri == null) {
            showToast("请上传教练证书照片");
            return;
        }

        career = et_career.getText().toString().trim();
        if (TextUtils.isEmpty(career)) {
            showToast("请输入个人经历");
            return;
        }

        showProgressDialog();
        uploadImage();
    }

    private FileUploadResponse avatarFileUploadResponse;
    private FileUploadResponse certFileUploadResponse;

    private void uploadImage() {
        HttpRequest.fileUpload(this, avatarUri, iv_avatar.getWidth(), iv_avatar.getHeight(),
                new Subscriber<FileUploadResponse>(){

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showToast("教练加盟申请提交失败");
                    }

                    @Override
                    public void onNext(FileUploadResponse fileUploadResponse) {
                        avatarFileUploadResponse = fileUploadResponse;
                        handleUploadResponse();
                    }
                });

        HttpRequest.fileUpload(this, certUri, iv_cert.getWidth(), iv_cert.getHeight(),
                new Subscriber<FileUploadResponse>(){

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                        showToast("教练加盟申请提交失败");
                    }

                    @Override
                    public void onNext(FileUploadResponse fileUploadResponse) {
                        certFileUploadResponse = fileUploadResponse;
                        handleUploadResponse();
                    }
                });
    }

    private void handleUploadResponse() {
        if(avatarFileUploadResponse != null && certFileUploadResponse != null) {

            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("coachName", formItemsPresent.getString("教练姓名"));
            updateParams.put("coachAge", age);
            updateParams.put("coachLocation", formItemsPresent.getString("城市"));
            updateParams.put("coachGender", gender);
            updateParams.put("idCard", formItemsPresent.getString("身份证"));
            updateParams.put("trainingYears", trainingYears);
            updateParams.put("certLevel", formItemsPresent.getString("证书等级"));
            updateParams.put("acceptTraining", rg_accept_training.getCheckedRadioButtonId() == R.id.rb_yes);
            updateParams.put("coachAvatarImg", HttpRequest.FILE_HOST + avatarFileUploadResponse.getUrl());
            updateParams.put("coachCertImg", HttpRequest.FILE_HOST + certFileUploadResponse.getUrl());
            updateParams.put("career", career);

            updateParams.put("role", User.ROLE_COACH_APPLYING);

            Observable<BaseEntity> observable = HttpRequest.getApiService().updateUserById(
                    UserInfoKeeper.getCurrentUser().getObjectId(), updateParams);
            ObservableDecorator.decorate(this, observable)
                    .subscribe(new SimpleSubscriber<BaseEntity>(this) {
                        @Override
                        public void onNext(BaseEntity baseEntity) {
                            dismissProgressDialog();
                            showToast("教练加盟提交成功，请等待审核");
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

    private void updateImage() {
        if(avatarUri != null) {
            rl_avatar.setVisibility(View.VISIBLE);
            iv_add_avatar.setVisibility(View.GONE);

            GlideHelper.showImage(this, avatarUri.toString(), iv_avatar);
        } else {
            rl_avatar.setVisibility(View.GONE);
            iv_add_avatar.setVisibility(View.VISIBLE);
        }

        if(certUri != null) {
            rl_cert.setVisibility(View.VISIBLE);
            iv_add_cert.setVisibility(View.GONE);

            GlideHelper.showImage(this, certUri.toString(), iv_cert);
        } else {
            rl_cert.setVisibility(View.GONE);
            iv_add_cert.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PICK_CITY:
                city = (CityModel) data.getSerializableExtra("city");
                formItemsPresent.showMidText("城市", city.name);
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA:
                avatarUri = ImageUtils.imageUriFromCamera;
                break;
            case ImageUtils.REQUEST_CODE_FROM_ALBUM:
                avatarUri = data.getData();
                break;
            case ImageUtils.REQUEST_CODE_FROM_CAMERA + 1:
                certUri = ImageUtils.imageUriFromCamera;
                break;
            case ImageUtils.REQUEST_CODE_FROM_ALBUM + 1:
                certUri = data.getData();
                break;
        }

        updateImage();
    }
}
