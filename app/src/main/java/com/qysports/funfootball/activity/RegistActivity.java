package com.qysports.funfootball.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.boredream.bdcodehelper.present.GroupEtEmptyCheckPresent;
import com.boredream.bdcodehelper.utils.DateUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.utils.UserInfoKeeper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class RegistActivity extends BaseActivity implements View.OnClickListener {


    // 总倒计时60秒
    private static final long TOTCAL_TIME = 60 * DateUtils.ONE_SECOND_MILLIONS;
    // 每次减少1秒
    private static final long COUNT_DOWN_INTERVAL = DateUtils.ONE_SECOND_MILLIONS;

    private EditText et_verification_code;
    private Button btn_code_info;
    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_agree;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initView();
    }

    private void initView() {
        initBackTitle("新用户注册");

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);
        btn_code_info = (Button) findViewById(R.id.btn_code_info);
        cb_agree = (CheckBox) findViewById(R.id.cb_agree);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_code_info.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        setCheckListener();
    }

    private boolean isAllEtChecked = false;
    private void setCheckListener() {
        new GroupEtEmptyCheckPresent(et_username, et_password, et_verification_code)
                .check(new GroupEtEmptyCheckPresent.OnEtEmptyListener() {
                    @Override
                    public void onEtEmpty(boolean hasOneEmpty) {
                        isAllEtChecked = !hasOneEmpty;
                        btn_next.setEnabled(isAllEtChecked && cb_agree.isChecked());
                    }
                });
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                btn_next.setEnabled(isAllEtChecked && cb_agree.isChecked());
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
     * 注册
     */
    private void register() {
        String username = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            showToast("请输入您的手机号");
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

        User user = new User();
        user.setMobilePhoneNumber(username);
        user.setUsername(username);
        user.setNickname(username);
        user.setPassword(password);
        user.setSmsCode(code);
        Observable<User> observable = HttpRequest.getApiService().userRegist(user);
        showProgressDialog();
        ObservableDecorator.decorate(this, observable)
                .subscribe(new SimpleSubscriber<User>(this) {
                    @Override
                    public void onNext(User user) {
                        // include token
                        dismissProgressDialog();
                        UserInfoKeeper.setCurrentUser(user);
                        // 注册成功,显示成功提示框
                        showRegistSuccessDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        dismissProgressDialog();
                    }
                });

    }

    /**
     * 注册成功提示框,提醒用户完善资料
     */
    private void showRegistSuccessDialog() {
        new AlertDialog.Builder(this)
                .setMessage("感谢您成为我们的会员，我们将为您提供最优质最完善的青少年足球培训服务。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        intent2Activity(MainActivity.class);
                    }
                })
                .setCancelable(false) // cancelable设为false,防止用户点击返回键关闭对话框停留在当前页面
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_code_info:
                getSmsCode();
                break;
            case R.id.btn_next:
                register();
                break;
        }
    }

}

