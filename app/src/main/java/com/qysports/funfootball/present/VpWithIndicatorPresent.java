package com.qysports.funfootball.present;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.FragPagerAdapter;
import com.qysports.funfootball.base.BaseActivity;
import com.qysports.funfootball.base.BaseFragment;

import java.util.List;

public class VpWithIndicatorPresent {

    private BaseActivity context;
    private RadioGroup rg_container;
    private ViewPager vp;

    public VpWithIndicatorPresent(BaseActivity context, RadioGroup rg_container, ViewPager vp) {
        this.context = context;
        this.rg_container = rg_container;
        this.vp = vp;
    }

    public void load(final String[] titles, List<BaseFragment> fragments) {
        FragmentPagerAdapter adapter = new FragPagerAdapter(
                context.getSupportFragmentManager(), fragments);
        vp.setAdapter(adapter);

        for (int i = 0; i < titles.length; i++) {
            RadioButton rb = (RadioButton) View.inflate(context, R.layout.rb_top_tab, null);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    0, RadioGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            rb.setLayoutParams(params);
            rb.setText(titles[i]);
            final int index = i;
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vp.setCurrentItem(index);
                }
            });
            rg_container.addView(rb);
        }
        checkRadioButton(0);

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkRadioButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        vp.addOnPageChangeListener(listener);
    }

    private void checkRadioButton(int position) {
        RadioButton rb = (RadioButton) rg_container.getChildAt(position);
        rb.setChecked(true);
    }
}
