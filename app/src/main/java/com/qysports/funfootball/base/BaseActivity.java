package com.qysports.funfootball.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.boredream.bdcodehelper.base.BoreBaseActivity;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.qysports.funfootball.activity.LoginActivity;
import com.qysports.funfootball.activity.MainActivity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.net.HttpRequest;
import com.qysports.funfootball.net.SimpleSubscriber;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.umeng.analytics.MobclickAgent;

import rx.Observable;

public class BaseActivity extends BoreBaseActivity {

    public BaseApplication application;
    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    /**
     * 获取收藏课程并保存到本地
     *
     * @param subscriber 代理回调
     */
    protected void getCollectCourse(final SimpleSubscriber<ListResponse<Course>> subscriber) {
        Observable<ListResponse<Course>> observable = HttpRequest.getCollectCourse();
        ObservableDecorator.decorate(this, observable).subscribe(
                new SimpleSubscriber<ListResponse<Course>>(this) {

                    @Override
                    public void onError(Throwable e) {
                        if(subscriber != null) {
                            subscriber.onError(e);
                        }
                    }

                    @Override
                    public void onNext(ListResponse<Course> response) {
                        User currentUser = UserInfoKeeper.getCurrentUser();
                        currentUser.setCollectCourseList(response.getResults());

                        if(subscriber != null) {
                            subscriber.onNext(response);
                        }
                    }
                }
        );
    }

    /**
     * 清空任务栈跳转至登录页
     */
    protected void clearIntent2Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 退出程序
     */
    protected void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
