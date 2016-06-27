package com.qysports.funfootball.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.boredream.bdcodehelper.utils.ToastUtils;
import com.qysports.funfootball.R;
import com.qysports.funfootball.adapter.AgeRangeGridAdapter;
import com.qysports.funfootball.adapter.MatchNumGridAdapter;
import com.qysports.funfootball.adapter.TypeGridAdapter;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.listener.OnStringSelectedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DialogUtils extends com.boredream.bdcodehelper.utils.DialogUtils {

    public static void showAgeRangeDialog(final Context context, final OnStringSelectedListener listener) {
        final AgeRangeGridAdapter adapter = new AgeRangeGridAdapter(context, CommonConstants.AGE_RANGES);

        View view = View.inflate(context, R.layout.dialog_agerange_picker, null);
        GridView gv_time = (GridView) view.findViewById(R.id.gv_age_range);
        gv_time.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_positive), null)
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener == null) {
                            return;
                        }

                        ArrayList<String> selectedAges = adapter.getSelectedAges();
                        if (selectedAges.size() > 4) {
                            ToastUtils.showToast(context, "最多选择4个年龄段");
                            return;
                        }

                        Collections.sort(selectedAges, new Comparator<String>() {
                            @Override
                            public int compare(String s, String t1) {
                                Integer i = Integer.parseInt(s.substring(1));
                                Integer ti = Integer.parseInt(t1.substring(1));
                                return i.compareTo(ti);
                            }
                        });

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedAges.size(); i++) {
                            if (i > 0) {
                                sb.append(CommonConstants.SEPARATOR.RANGE_AGES);
                            }
                            sb.append(selectedAges.get(i));
                        }

                        // 判断连续
                        if (selectedAges.size() > 1) {
                            boolean isSeries = true;
                            int lastAge = -1;
                            for (String ageRange : selectedAges) {
                                int age = Integer.parseInt(ageRange.substring(1));
                                if (lastAge != -1) {
                                    if (age - lastAge != 1) {
                                        isSeries = false;
                                        break;
                                    }
                                    lastAge = age;
                                } else {
                                    lastAge = age;
                                }
                            }

                            if (!isSeries) {
                                ToastUtils.showToast(context, "年龄段必须连续");
                                return;
                            }
                        }

                        listener.onStringSelected(sb.toString());
                        dialog.dismiss();
                    }
                });
    }

    public static void showCourseTypeDialog(final Context context, final OnStringSelectedListener listener) {
        View view = View.inflate(context, R.layout.dialog_course_types, null);
        GridView gv_course_type = (GridView) view.findViewById(R.id.gv_course_type);
        final EditText et_custom_course_type1 = (EditText) view.findViewById(R.id.et_custom_course_type1);
        final EditText et_custom_course_type2 = (EditText) view.findViewById(R.id.et_custom_course_type2);

        final TypeGridAdapter adapter = new TypeGridAdapter(context, CommonConstants.COURSE_TYPES);
        gv_course_type.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_positive), null)
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener == null) {
                            return;
                        }

                        ArrayList<String> selectedTypes = new ArrayList<>();
                        selectedTypes.addAll(adapter.getSelectedTypes());

                        String customType1 = et_custom_course_type1.getText()
                                .toString().trim().replace('、', '，');
                        String customType2 = et_custom_course_type2.getText()
                                .toString().trim().replace('、', '，');

                        if (!TextUtils.isEmpty(customType1)) {
                            if (selectedTypes.contains(customType1)) {
                                ToastUtils.showToast(context, "课程类型名不能重复");
                                return;
                            } else {
                                selectedTypes.add(customType1);
                            }
                        }

                        if (!TextUtils.isEmpty(customType2)) {
                            if (selectedTypes.contains(customType2)) {
                                ToastUtils.showToast(context, "课程类型名不能重复");
                                return;
                            } else {
                                selectedTypes.add(customType2);
                            }
                        }

                        if (selectedTypes.size() > 2) {
                            ToastUtils.showToast(context, "课程类型最多只能选择两个");
                            return;
                        }

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedTypes.size(); i++) {
                            if (i > 0) {
                                sb.append(CommonConstants.SEPARATOR.COURSE_TYPES);
                            }
                            sb.append(selectedTypes.get(i));
                        }

                        listener.onStringSelected(sb.toString());
                        dialog.dismiss();
                    }
                });
    }

    public static void showActTypeDialog(final Context context, final OnStringSelectedListener listener) {
        View view = View.inflate(context, R.layout.dialog_act_types, null);
        GridView gv_type = (GridView) view.findViewById(R.id.gv_type);
        final EditText et_custom_type = (EditText) view.findViewById(R.id.et_custom_type);

        final TypeGridAdapter adapter = new TypeGridAdapter(context, CommonConstants.ACT_TYPES);
        gv_type.setAdapter(adapter);

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_positive), null)
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener == null) {
                            return;
                        }

                        ArrayList<String> selectedTypes = new ArrayList<>();
                        selectedTypes.addAll(adapter.getSelectedTypes());

                        String customType = et_custom_type.getText()
                                .toString().trim().replace('、', '，');

                        if (!TextUtils.isEmpty(customType)) {
                            if (selectedTypes.contains(customType)) {
                                ToastUtils.showToast(context, "课程类型名不能重复");
                                return;
                            } else {
                                selectedTypes.add(customType);
                            }
                        }

                        if (selectedTypes.size() > 2) {
                            ToastUtils.showToast(context, "课程类型最多只能选择两个");
                            return;
                        }

                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < selectedTypes.size(); i++) {
                            if (i > 0) {
                                sb.append(CommonConstants.SEPARATOR.ACT_TYPES);
                            }
                            sb.append(selectedTypes.get(i));
                        }

                        listener.onStringSelected(sb.toString());
                        dialog.dismiss();
                    }
                });
    }

    private static AlertDialog singleChoiceDialog;
    public static void showSingleChoiceDialog(Context context, final String[] items, final OnStringSelectedListener listener) {
        singleChoiceDialog = new AlertDialog.Builder(context).setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        singleChoiceDialog.dismiss();

                        if (listener == null) {
                            return;
                        }

                        listener.onStringSelected(items[position]);
                    }
                })
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .create();
        singleChoiceDialog.show();
    }

    private static AlertDialog matchNumDialog;

    public static void showMatchNumDialog(final Context context, final OnStringSelectedListener listener) {
        View view = View.inflate(context, R.layout.dialog_match_nums, null);
        GridView gv_nums = (GridView) view.findViewById(R.id.gv_nums);

        final MatchNumGridAdapter adapter = new MatchNumGridAdapter(
                context, CommonConstants.MATCH_NUMS,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        if(listener != null) {
                            listener.onStringSelected(CommonConstants.MATCH_NUMS.get(position));
                        }
                        matchNumDialog.dismiss();
                    }
                });
        gv_nums.setAdapter(adapter);

        matchNumDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setNegativeButton(context.getString(com.boredream.bdcodehelper.R.string.dialog_negative), null)
                .create();
        matchNumDialog.show();
    }
}
