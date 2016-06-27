package com.qysports.funfootball.utils;

import android.text.TextUtils;

import com.qysports.funfootball.constants.CommonConstants;

import java.util.ArrayList;
import java.util.Map;

public class StringUtils extends com.boredream.bdcodehelper.utils.StringUtils {

    public static String getSplitDate(String date) {
        String[] dates = date.split(CommonConstants.SEPARATOR.DATE);
        return TextUtils.isEmpty(dates[1]) ? dates[0] : dates[0] + "—" + dates[1];
    }

    public static String getSplitTime(String date) {
        String[] dates = date.split(CommonConstants.SEPARATOR.DATE);
        return dates[2] + "—" + dates[3];
    }

    public static ArrayList<String> splitString(String srcStr, String separator) {
        ArrayList<String> list = new ArrayList<>();

        if(srcStr != null) {
            for(String str : srcStr.split(separator)) {
                if(TextUtils.isEmpty(str)) {
                    continue;
                }

                list.add(str);
            }
        }

        return list;
    }

    public static String getMatchNumStr(int matchNum) {
        return matchNum + "人制";
    }

    public static String getGenderStr(int gender) {
        return CommonConstants.MATCH_GENDERS.get(gender);
    }

    public static int getMatchGenderNum(String str) {
        for(Map.Entry<Integer, String> entry : CommonConstants.MATCH_GENDERS.entrySet()) {
            if(entry.getValue().equals(str)) {
                return entry.getKey();
            }
        }
        return CommonConstants.MATCH_GENDERS_ALL;
    }
}
