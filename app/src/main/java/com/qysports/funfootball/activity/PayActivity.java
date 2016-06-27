package com.qysports.funfootball.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boredream.bdcodehelper.view.WrapHeightListView;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.PayTypeAdapter;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.entity.PayType;

import java.util.ArrayList;
import java.util.List;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_money;
    private WrapHeightListView lv_pay_type;
    private Button btn_pay;

    private PayTypeAdapter adapter;
    private List<PayType> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        initView();
        initData();
    }

    private void initView() {
        initBackTitle("支付");

        tv_money = (TextView) findViewById(R.id.tv_money);
        lv_pay_type = (WrapHeightListView) findViewById(R.id.lv_pay_type);
        btn_pay = (Button) findViewById(R.id.btn_pay);

        btn_pay.setOnClickListener(this);
    }

    private void initData() {
        initPayType();
        adapter = new PayTypeAdapter(this, datas);
        lv_pay_type.setAdapter(adapter);
    }

    private void initPayType() {
        datas.add(new PayType(1, "支付宝"));
        datas.add(new PayType(2, "微信支付"));
    }

    private void submit() {
        int position = lv_pay_type.getCheckedItemPosition();
        PayType payType = adapter.getItem(position);
        showToast("使用 " + payType.name + " 支付");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                submit();
                break;
        }
    }
}
