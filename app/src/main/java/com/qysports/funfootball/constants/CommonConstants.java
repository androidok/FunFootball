package com.qysports.funfootball.constants;

import com.qysports.funfootball.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO replace your app constants
 * 常用常量
 */
public class CommonConstants {

    public static final int COURSE_MAX_DAY = 20;

    private CommonConstants() {
    }

    /**
     * SharedPreferences 名称
     */
    public static final String SP_NAME = "funfootball_config";

    /**
     * 每页加载的数量
     */
    public static final int COUNT_OF_PAGE = 10;

    /**
     * 保存图片等的文件夹名称
     */
    public static final String DIR_NAME = "FunFootBall";

    public static final ArrayList<String> AGE_RANGES;
    public static final ArrayList<String> COURSE_TYPES;
    public static final ArrayList<String> ACT_TYPES;
    public static final ArrayList<String> MATCH_NUMS;

    public static final Map<Integer, String> MATCH_GENDERS;
    public static final int MATCH_GENDERS_MALE = 1;
    public static final int MATCH_GENDERS_FEMALE = 2;
    public static final int MATCH_GENDERS_ALL = 3;

    static {
        AGE_RANGES = new ArrayList<>();
        for (int i = 4; i <= 12; i++) {
            AGE_RANGES.add("U" + i);
        }

        COURSE_TYPES = new ArrayList<>();
        COURSE_TYPES.add("基础训练");
        COURSE_TYPES.add("进阶训练");
        COURSE_TYPES.add("技巧练习");
        COURSE_TYPES.add("体能健康");
        COURSE_TYPES.add("分组对抗");
        COURSE_TYPES.add("体验课程");
        COURSE_TYPES.add("长期课程");
        COURSE_TYPES.add("短期课程");
        COURSE_TYPES.add("暑假课程");
        COURSE_TYPES.add("寒假课程");

        ACT_TYPES = new ArrayList<>();
        ACT_TYPES.add("亲自活动");
        ACT_TYPES.add("拓展活动");
        ACT_TYPES.add("免费课程");

        MATCH_GENDERS = new HashMap<>();
        MATCH_GENDERS.put(MATCH_GENDERS_MALE, "男");
        MATCH_GENDERS.put(MATCH_GENDERS_FEMALE, "女");
        MATCH_GENDERS.put(MATCH_GENDERS_ALL, "不限");

        MATCH_NUMS = new ArrayList<>();
        for (int i = 4; i <= 11; i++) {
            MATCH_NUMS.add(StringUtils.getMatchNumStr(i));
        }
    }

    public static class SEPARATOR {
        public static final String IMAGE_UPLOAD_URLS = "、";
        public static final String RANGE_AGES = "、";
        public static final String COURSE_TYPES = "、";
        public static final String ACT_TYPES = "、";
        public static final String DATE = "、";
        public static final String WEEK = "、";
    }
}
