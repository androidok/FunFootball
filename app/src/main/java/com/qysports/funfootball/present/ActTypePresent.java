package com.qysports.funfootball.present;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.DisplayUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.listener.OnStringSelectedListener;
import com.qysports.funfootball.utils.DialogUtils;
import com.qysports.funfootball.utils.StringUtils;

import java.util.ArrayList;

public class ActTypePresent {

    private Context context;
    private LinearLayout ll_content;

    public ActTypePresent(LinearLayout ll_content) {
        context = ll_content.getContext();
        this.ll_content = ll_content;
    }

    public void showTypePickerDialog(OnStringSelectedListener listener) {
        DialogUtils.showActTypeDialog(context, listener);
    }

    public void setTypes(String actTypes) {
        ll_content.removeAllViews();
        ArrayList<String> types = StringUtils.splitString(
                actTypes, CommonConstants.SEPARATOR.ACT_TYPES);
        for(String type : types) {
            TextView tv = (TextView) View.inflate(context, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_content.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(context, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(type);
            ll_content.addView(tv);
        }
    }
}
