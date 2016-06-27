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

public class CourseTypePickerPresent {

    private Context context;
    private LinearLayout ll_type_content;

    public CourseTypePickerPresent(LinearLayout ll_type_content) {
        context = ll_type_content.getContext();
        this.ll_type_content = ll_type_content;
    }

    public void showTypePickerDialog(OnStringSelectedListener listener) {
        DialogUtils.showCourseTypeDialog(context, listener);
    }

    public void setTypes(String courseTypes) {
        ll_type_content.removeAllViews();
        ArrayList<String> types = StringUtils.splitString(
                courseTypes, CommonConstants.SEPARATOR.COURSE_TYPES);
        for(String type : types) {
            TextView tv = (TextView) View.inflate(context, R.layout.tv_correct_primary_stroke, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            if(ll_type_content.getChildCount() > 0) {
                params.setMargins(DisplayUtils.dp2px(context, 16), 0, 0, 0);
            }
            tv.setLayoutParams(params);
            tv.setText(type);
            ll_type_content.addView(tv);
        }
    }
}
