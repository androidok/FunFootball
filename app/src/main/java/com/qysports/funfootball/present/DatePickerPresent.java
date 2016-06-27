package com.qysports.funfootball.present;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.bdcodehelper.utils.DateUtils;
import com.boredream.bdcodehelper.utils.ToastUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.TimeGridAdapter;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.Match;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DatePickerPresent implements View.OnClickListener {

    private Context context;
    private String title;
    private View include_range_date;
    private CalendarPickerView.SelectionMode mode;

    private TextView tv_title;
    private LinearLayout ll_date;
    private TextView tv_date;
    private LinearLayout ll_time;
    private TextView tv_time;
    private TextView tv_week1;
    private TextView tv_week2;
    private TextView tv_week3;
    private TextView tv_week4;
    private TextView tv_week5;
    private TextView tv_week6;
    private TextView tv_week7;

    private Date startDate;
    private Date endDate;
    private Date startTime;
    private Date endTime;

    private List<Integer> selectedWeek = new ArrayList<>();

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<Integer> getSelectedWeek() {
        return selectedWeek;
    }

    public DatePickerPresent(Context context, String title, View include_range_date) {
        this(context, title, include_range_date, CalendarPickerView.SelectionMode.RANGE);
    }

    public DatePickerPresent(Context context, String title, View include_range_date,
                             CalendarPickerView.SelectionMode mode) {
        this.context = context;
        this.title = title;
        this.include_range_date = include_range_date;
        this.mode = mode;

        initView();
        initTime();
    }

    private void initView() {
        tv_title = (TextView) include_range_date.findViewById(R.id.tv_title);
        ll_date = (LinearLayout) include_range_date.findViewById(R.id.ll_date);
        tv_date = (TextView) include_range_date.findViewById(R.id.tv_date);
        ll_time = (LinearLayout) include_range_date.findViewById(R.id.ll_time);
        tv_time = (TextView) include_range_date.findViewById(R.id.tv_time);
        tv_week1 = (TextView) include_range_date.findViewById(R.id.tv_week1);
        tv_week2 = (TextView) include_range_date.findViewById(R.id.tv_week2);
        tv_week3 = (TextView) include_range_date.findViewById(R.id.tv_week3);
        tv_week4 = (TextView) include_range_date.findViewById(R.id.tv_week4);
        tv_week5 = (TextView) include_range_date.findViewById(R.id.tv_week5);
        tv_week6 = (TextView) include_range_date.findViewById(R.id.tv_week6);
        tv_week7 = (TextView) include_range_date.findViewById(R.id.tv_week7);

        tv_title.setText(title + "时间");
        ll_date.setOnClickListener(this);
        ll_time.setOnClickListener(this);

        setWeekClickable(mode != CalendarPickerView.SelectionMode.SINGLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                showCalendarDialog();
                break;
            case R.id.ll_time:
                showTimeDialog();
                break;
            case R.id.tv_week1:
                toggleWeek(Calendar.MONDAY);
                break;
            case R.id.tv_week2:
                toggleWeek(Calendar.TUESDAY);
                break;
            case R.id.tv_week3:
                toggleWeek(Calendar.WEDNESDAY);
                break;
            case R.id.tv_week4:
                toggleWeek(Calendar.THURSDAY);
                break;
            case R.id.tv_week5:
                toggleWeek(Calendar.FRIDAY);
                break;
            case R.id.tv_week6:
                toggleWeek(Calendar.SATURDAY);
                break;
            case R.id.tv_week7:
                toggleWeek(Calendar.SUNDAY);
                break;
        }
    }

    public void showCalendarDialog() {
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        View view = View.inflate(context, R.layout.dialog_calendarpicker, null);
        final CalendarPickerView calendar = (CalendarPickerView) view.findViewById(R.id.cpv);
        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());

        calendar.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                List<Date> selectedDates = calendar.getSelectedDates();
                if (selectedDates.size() == 1) {
                    int dayCount = DateUtils.calculateDayDiff(date, selectedDates.get(0)) + 1;
                    if (dayCount > CommonConstants.COURSE_MAX_DAY) {
                        ToastUtils.showToast(context, "课程最多不能超过" + CommonConstants.COURSE_MAX_DAY + "天");
                        return true;
                    }
                }
                return false;
            }
        });

        calendar.init(new Date(), nextYear.getTime()) //
                .inMode(mode);

        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Date> selectedDates = calendar.getSelectedDates();

                                handlePickedCalendar(selectedDates);
                            }
                        })
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .show();
    }

    private void handlePickedCalendar(List<Date> dates) {
        // clear
        selectedWeek.clear();
        startDate = null;
        endDate = null;

        // week
        setWeekClickable(dates.size() > 1);
        if(dates.size() == 1) {
            // 只选一天，周直接显示，不能选择
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dates.get(0));
            selectedWeek.add(calendar.get(Calendar.DAY_OF_WEEK));
            setWeekView();
        }

        // date
        if (dates.size() == 0) {
            tv_date.setText("");
        } else if (dates.size() == 1) {
            Date startDate = dates.get(0);

            String dateStr = DateUtils.getDate(
                    DateUtils.date2str(startDate));

            this.startDate = startDate;
            tv_date.setText(dateStr);
        } else if (dates.size() > 1) {
            Date startDate = dates.get(0);
            Date endDate = dates.get(dates.size() - 1);

            String startDateStr = DateUtils.getDate(
                    DateUtils.date2str(startDate));
            String endDateStr = DateUtils.date2str(
                    endDate, "MM月dd日");
            String dateStr = startDateStr + " ~ " + endDateStr;

            this.startDate = startDate;
            this.endDate = endDate;
            tv_date.setText(dateStr);
        }
    }

    /**
     * @param week 周几，在Calendar.MONDAY ~ Calendar.SUNDAY之间选择
     */
    private void toggleWeek(Integer week) {
        if (!selectedWeek.contains(week)) {
            selectedWeek.add(week);
        } else {
            selectedWeek.remove(week);
        }
        setWeekView();
    }

    private void setWeekView() {
        int containBg = R.drawable.oval_primary_solid;
        int nonContainBg = R.drawable.oval_primary_light_solid;

        int containTvColor = context.getResources().getColor(R.color.white);
        int nonContainTvColor = context.getResources().getColor(R.color.gray);

        tv_week1.setBackgroundResource(selectedWeek.contains(Calendar.MONDAY) ? containBg : nonContainBg);
        tv_week1.setTextColor(selectedWeek.contains(Calendar.MONDAY) ? containTvColor : nonContainTvColor);

        tv_week2.setBackgroundResource(selectedWeek.contains(Calendar.TUESDAY) ? containBg : nonContainBg);
        tv_week2.setTextColor(selectedWeek.contains(Calendar.TUESDAY) ? containTvColor : nonContainTvColor);

        tv_week3.setBackgroundResource(selectedWeek.contains(Calendar.WEDNESDAY) ? containBg : nonContainBg);
        tv_week3.setTextColor(selectedWeek.contains(Calendar.WEDNESDAY) ? containTvColor : nonContainTvColor);

        tv_week4.setBackgroundResource(selectedWeek.contains(Calendar.THURSDAY) ? containBg : nonContainBg);
        tv_week4.setTextColor(selectedWeek.contains(Calendar.THURSDAY) ? containTvColor : nonContainTvColor);

        tv_week5.setBackgroundResource(selectedWeek.contains(Calendar.FRIDAY) ? containBg : nonContainBg);
        tv_week5.setTextColor(selectedWeek.contains(Calendar.FRIDAY) ? containTvColor : nonContainTvColor);

        tv_week6.setBackgroundResource(selectedWeek.contains(Calendar.SATURDAY) ? containBg : nonContainBg);
        tv_week6.setTextColor(selectedWeek.contains(Calendar.SATURDAY) ? containTvColor : nonContainTvColor);

        tv_week7.setBackgroundResource(selectedWeek.contains(Calendar.SUNDAY) ? containBg : nonContainBg);
        tv_week7.setTextColor(selectedWeek.contains(Calendar.SUNDAY) ? containTvColor : nonContainTvColor);
    }

    ///////////////////////////////     time    ///////////////////////////////

    private ArrayList<Date> times = new ArrayList<>();
    private TimeGridAdapter timeGridAdapter;

    private void initTime() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, 8);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);

        Calendar noonCalendar = Calendar.getInstance();
        noonCalendar.set(Calendar.HOUR_OF_DAY, 11);
        noonCalendar.set(Calendar.MINUTE, 45);
        noonCalendar.set(Calendar.SECOND, 0);
        Date noonDate = noonCalendar.getTime();

        Calendar afterCalendar = Calendar.getInstance();
        afterCalendar.set(Calendar.HOUR_OF_DAY, 17);
        afterCalendar.set(Calendar.MINUTE, 30);
        afterCalendar.set(Calendar.SECOND, 0);
        Date afterDate = afterCalendar.getTime();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, 21);
        endCalendar.set(Calendar.MINUTE, 45);
        endCalendar.set(Calendar.SECOND, 0);

        for (; startCalendar.before(endCalendar); startCalendar.add(Calendar.MINUTE, 30)) {
            Date time = startCalendar.getTime();
            times.add(time);
        }

        timeGridAdapter = new TimeGridAdapter(context, times, noonDate, afterDate);
    }

    public void showTimeDialog() {
        View view = View.inflate(context, R.layout.dialog_timepicker, null);
        GridView gv_time = (GridView) view.findViewById(R.id.gv_time);
        gv_time.setAdapter(timeGridAdapter);

        new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_positive),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Date startDate = timeGridAdapter.getStartDate();
                                Date endDate = timeGridAdapter.getEndDate();

                                handlePickedTime(startDate, endDate);
                            }
                        })
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .show();
    }

    private void handlePickedTime(Date startTime, Date endTime) {
        if (startTime == null || endTime == null) {
            ToastUtils.showToast(context, "请选择开始和结束时间");
            return;
        }

        this.startTime = startTime;
        this.endTime = endTime;

        String startDateStr = DateUtils.date2str(startTime, "HH:mm");
        String endDateStr = DateUtils.date2str(endTime, "HH:mm");
        String dateStr = startDateStr + " ~ " + endDateStr;

        tv_time.setText(dateStr);
    }

    public boolean validate() {
        if (getStartDate() == null || getStartDate() == null) {
            ToastUtils.showToast(context, title + "日期不能为空");
            return false;
        }

        if (getStartTime() == null || getEndTime() == null) {
            ToastUtils.showToast(context, title + "活动时间不能为空");
            return false;
        }

        return true;
    }

    public String getDateString() {
        StringBuilder sb = new StringBuilder();

        String startDateStr = DateUtils.getDate(
                DateUtils.date2str(startDate));
        String endDateStr = endDate == null ? "" :
                DateUtils.date2str(endDate, "MM月dd日");
        String startTimeStr = DateUtils.date2str(
                startTime, "HH:mm");
        String endTimeStr = DateUtils.date2str(
                endTime, "HH:mm");

        sb.append(startDateStr)
                .append(CommonConstants.SEPARATOR.DATE)
                .append(endDateStr)
                .append(CommonConstants.SEPARATOR.DATE)
                .append(startTimeStr)
                .append(CommonConstants.SEPARATOR.DATE)
                .append(endTimeStr);

        return sb.toString();
    }

    public String getWeekStr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedWeek.size(); i++) {
            if (i > 0) {
                sb.append(CommonConstants.SEPARATOR.WEEK);
            }
            sb.append(selectedWeek.get(i));
        }
        return sb.toString();
    }

    private List<Integer> weekStr2Int(String weekStr) {
        List<Integer> weeks = new ArrayList<>();
        String[] weekStrs = weekStr.split(CommonConstants.SEPARATOR.WEEK);
        for(String s : weekStrs) {
            weeks.add(Integer.parseInt(s));
        }
        return weeks;
    }

    public void showDateInfo(Course course) {
        String[] dates = course.getDate().split(CommonConstants.SEPARATOR.DATE);
        tv_date.setText(TextUtils.isEmpty(dates[1]) ? dates[0] : dates[0] + "—" + dates[1]);
        tv_time.setText(dates[2] + " ~ " + dates[3]);
        setWeekClickable(false);
        showWeek(course.getWeeks());
    }

    public void showDateInfo(Act act) {
        String[] dates = act.getDate().split(CommonConstants.SEPARATOR.DATE);
        tv_date.setText(dates[0]);
        tv_time.setText(dates[2] + " ~ " + dates[3]);
        Calendar calendar = DateUtils.str2calendar(dates[0], DateUtils.PATTERN_DATE);
        setWeekClickable(false);
        showWeek(calendar.get(Calendar.DAY_OF_WEEK) + "");
    }

    public void showDateInfo(Match match) {
        String[] dates = match.getDate().split(CommonConstants.SEPARATOR.DATE);
        tv_date.setText(dates[0]);
        tv_time.setText(dates[2] + " ~ " + dates[3]);
        Calendar calendar = DateUtils.str2calendar(dates[0], DateUtils.PATTERN_DATE);
        setWeekClickable(false);
        showWeek(calendar.get(Calendar.DAY_OF_WEEK) + "");
    }

    public void showWeek(String weekStr) {
        List<Integer> weeks = weekStr2Int(weekStr);

        this.selectedWeek = weeks;
        setWeekView();

        ll_date.setOnClickListener(null);
        ll_time.setOnClickListener(null);

    }

    private void setWeekClickable(boolean clickable) {
        if(clickable) {
            tv_week1.setOnClickListener(this);
            tv_week2.setOnClickListener(this);
            tv_week3.setOnClickListener(this);
            tv_week4.setOnClickListener(this);
            tv_week5.setOnClickListener(this);
            tv_week6.setOnClickListener(this);
            tv_week7.setOnClickListener(this);
        } else {
            tv_week1.setOnClickListener(null);
            tv_week2.setOnClickListener(null);
            tv_week3.setOnClickListener(null);
            tv_week4.setOnClickListener(null);
            tv_week5.setOnClickListener(null);
            tv_week6.setOnClickListener(null);
            tv_week7.setOnClickListener(null);
        }
    }
}
