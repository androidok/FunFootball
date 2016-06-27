package com.qysports.funfootball.entity;


import android.content.Context;

import com.boredream.bdcodehelper.entity.Pointer;
import com.qysports.funfootball.utils.UserInfoKeeper;

public class EntityTableInit {
    public static void initTable(Context context) {
        Course course = new Course();

        // 新建评论对象
        User coach = UserInfoKeeper.getCurrentUser();
        coach.set__type(Pointer.TYPE);
        coach.setClassName("_User");

        course.setCoach(coach);
        course.setLocation("上海市");
        course.setCost(1000);
        course.setDate("2016-12-21 12:22");
        course.setPersonNum(5);
        course.setTitle("课程A");

//        ObservableDecorator.decorate(context,
//                HttpRequest.getApiService().addCourse(course)).subscribe(
//                    new SimpleSubscriber<BaseEntity>(context) {
//
//                    });
    }
}
