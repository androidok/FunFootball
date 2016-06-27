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

public class AgePickerPresent {

    private Context context;
    private LinearLayout ll_age_range_content;

    public AgePickerPresent(LinearLayout ll_age_range_content) {
        context = ll_age_range_content.getContext();
        this.ll_age_range_content = ll_age_range_content;
    }

    public void showAgePickerDialog(OnStringSelectedListener listener) {
        DialogUtils.showAgeRangeDialog(context, listener);
    }

    public void setAgeRange(String ageRange) {
        ll_age_range_content.removeAllViews();
        ArrayList<String> ageRanges = StringUtils.splitString(
                ageRange, CommonConstants.SEPARATOR.RANGE_AGES);
        for(String age : ageRanges) {
            TextView tv = (TextView) View.inflate(context, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_age_range_content.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(context, 15), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(age);
            ll_age_range_content.addView(tv);
        }
    }
}
