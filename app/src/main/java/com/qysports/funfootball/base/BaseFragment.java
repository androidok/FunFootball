package com.qysports.funfootball.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boredream.bdcodehelper.utils.ToastUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.activity.CityActivity;
import com.qysports.funfootball.activity.MainActivity;

public abstract class BaseFragment extends Fragment {

    protected String TAG;
    protected MainActivity activity;
    // progressDialog/sp/application 等使用activity.xx 调用

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();

        activity = (MainActivity) getActivity();
    }

    protected void initCityTitle(View view, String title, String city) {
        LinearLayout ll_city = (LinearLayout) view.findViewById(R.id.ll_city);
        TextView tv_city = (TextView) view.findViewById(R.id.tv_city);
        TextView titlebar_tv = (TextView) view.findViewById(R.id.titlebar_tv);

        ll_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2Activity(CityActivity.class);
            }
        });
        titlebar_tv.setText(title);
        if(city == null) {
            ll_city.setVisibility(View.GONE);
        } else {
            ll_city.setVisibility(View.VISIBLE);
            tv_city.setText(city);
        }
    }

    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(activity, tarActivity);
        startActivity(intent);
    }

    protected void showToast(String msg) {
        ToastUtils.showToast(activity, msg, Toast.LENGTH_SHORT);
    }

    protected void showLog(String msg) {
        Log.i(TAG, msg);
    }

    protected void showProgressDialog() {
        activity.showProgressDialog();
    }

    protected void dismissProgressDialog() {
        activity.dismissProgressDialog();
    }
}
