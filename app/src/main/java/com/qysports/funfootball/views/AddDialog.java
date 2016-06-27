package com.qysports.funfootball.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qysports.funfootball.R;

public class AddDialog extends Dialog implements View.OnClickListener {

    private static final long ANIM_DURATION = 100;
    private static final long ROTATE_DEGREE = 45;

    private ImageView btn_add;
    private LinearLayout ll_publish_top;
    private LinearLayout ll_publish1;
    private LinearLayout ll_publish2;
    private LinearLayout ll_publish_bottom;
    private LinearLayout ll_publish3;
    private LinearLayout ll_publish4;

    private View.OnClickListener onClickListener;

    public AddDialog(Context context) {
        this(context, R.style.DialogAdd);
    }

    public AddDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    public AddDialog(Context context, View.OnClickListener onClickListener) {
        this(context);
        this.onClickListener = onClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add);

        initView();
        startShowAnim();
    }

    private void initView() {
        btn_add = (ImageView) findViewById(R.id.btn_add);
        ll_publish_top = (LinearLayout) findViewById(R.id.ll_publish_top);
        ll_publish1 = (LinearLayout) findViewById(R.id.ll_publish1);
        ll_publish2 = (LinearLayout) findViewById(R.id.ll_publish2);
        ll_publish_bottom = (LinearLayout) findViewById(R.id.ll_publish_bottom);
        ll_publish3 = (LinearLayout) findViewById(R.id.ll_publish3);
        ll_publish4 = (LinearLayout) findViewById(R.id.ll_publish4);

        btn_add.setOnClickListener(this);
        ll_publish1.setOnClickListener(onClickListener);
        ll_publish2.setOnClickListener(onClickListener);
        ll_publish3.setOnClickListener(onClickListener);
        ll_publish4.setOnClickListener(onClickListener);
    }

    private void startShowAnim() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, ROTATE_DEGREE,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(ANIM_DURATION);
        rotateAnimation.setFillAfter(true);
        btn_add.startAnimation(rotateAnimation);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 2.0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0
        );
        translateAnimation.setDuration(ANIM_DURATION * 2);
        ll_publish_top.startAnimation(translateAnimation);
        ll_publish_bottom.startAnimation(translateAnimation);
    }

    private void startDismissAnim() {
        btn_add.setEnabled(false);

        RotateAnimation rotateAnimation = new RotateAnimation(
                ROTATE_DEGREE, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(ANIM_DURATION);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        btn_add.startAnimation(rotateAnimation);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 2.0f
        );
        translateAnimation.setDuration(ANIM_DURATION * 2);
        ll_publish_top.startAnimation(translateAnimation);
        ll_publish_bottom.startAnimation(translateAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                startDismissAnim();
                break;
        }
    }
}
