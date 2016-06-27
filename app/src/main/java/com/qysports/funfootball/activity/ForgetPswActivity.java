package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.GroupEtEmptyCheckPresent;
import com.boredream.bdcodehelper.utils.DateUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class ForgetPswActivity extends BaseActivity implements View.OnClickListener {

    // 总倒计时60秒
    private static final long TOTCAL_TIME = 60 * DateUtils.ONE_SECOND_MILLIONS;
    // 每次减少1秒
    private static final long COUNT_DOWN_INTERVAL = DateUtils.ONE_SECOND_MILLIONS;

    private EditText et_verification_code;
    private Button btn_code_info;
    private EditText et_username;
    private EditText et_password;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        initView();
    }

    private void initView() {
        initBackTitle("忘记密码");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        btn_code_info = (Button) findViewById(R.id.btn_code_info);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_code_info.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        setCheckListener();
    }

    private void setCheckListener() {
        new GroupEtEmptyCheckPresent(et_username, et_password, et_verification_code)
                .check(new GroupEtEmptyCheckPresent.OnEtEmptyListener() {
                    @Override
                    public void onEtEmpty(boolean hasOneEmpty) {
                        btn_next.setEnabled(!hasOneEmpty);
                    }
                });
    }

    private void getSmsCode() {
        String username = et_username.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast(getString(R.string.empty_username));
            return;
        }

        showProgressDialog();
        Map<String, Object> params = new HashMap<>();
        params.put("mobilePhoneNumber", username);
        Observable<Object> observable = HttpRequest.getApiService().requestSmsCode(params);
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object o) {
                        dismissProgressDialog();

                        // 短信验证码发送成功后,开始倒计时
                        startCountDown();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
//
//        // FIXME
//        startCountDown();
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        showToast("短信验证码发送成功");

        btn_code_info.setText("60秒");
        btn_code_info.setEnabled(false);

        // 倒计时开始,共60秒,每次减少1秒
        CountDownTimer timer = new CountDownTimer(TOTCAL_TIME, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long l) {
                // 重新获取(60)
                String restTime = (int) (l / COUNT_DOWN_INTERVAL) + "秒";
                btn_code_info.setText(restTime);
            }

            @Override
            public void onFinish() {
                // 倒计时结束,重置按钮
                btn_code_info.setText("重新获取");
                btn_code_info.setEnabled(true);
            }
        };
        timer.start();
    }

    /**
     * 重置密码
     */
    private void resetPswBySmsCode() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast(getString(R.string.empty_username));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(getString(R.string.empty_password));
            return;
        }

        // validate
        String code = et_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("password", password);
        Observable<Object> observable = HttpRequest.getApiService().resetPasswordBySmsCode(code, params);
        showProgressDialog();
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<Object>(this) {
                    @Override
                    public void onNext(Object user) {
                        // 密码重置成功,跳转到登录页
                        dismissProgressDialog();
                        clearIntent2Login();
                        showToast("密码重置成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });
    }

//    /**
//     * 修改密码
//     */
//    private void modifyPassword() {
//        String username = et_username.getText().toString().trim();
//        String password = et_password.getText().toString().trim();
//
//        if (TextUtils.isEmpty(username)) {
//            showToast(getString(R.string.empty_username));
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            showToast(getString(R.string.empty_password));
//            return;
//        }
//
//        // validate
//        String code = et_verification_code.getText().toString().trim();
//        if (TextUtils.isEmpty(code)) {
//            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("username", username);
//        map.put("password", password);
//        Observable<BaseResponse> observable = HttpRequest.getApiService().resetPasswordBySmsCode(map);
//        showProgressDialog();
//        ObservableDecorator.decorate(this, observable)
//                .subscribe(new SimpleSubscriber<BaseResponse>(this) {
//                    @Override
//                    public void onNext(BaseResponse response) {
//                        // include token
//                        dismissProgressDialog();
//                        showToast(response.getMessage());
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        dismissProgressDialog();
//                    }
//                });
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code_info:
                getSmsCode();
                break;
            case R.id.btn_next:
                resetPswBySmsCode();
                break;
        }
    }

}

