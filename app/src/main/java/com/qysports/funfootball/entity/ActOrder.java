package com.qysports.funfootball.entity;

import com.qysports.funfootball.base.BaseEntity;

public class ActOrder extends BaseEntity {
    //    课程报名订单
//    课程 course
//    报名用户 user
//    家长名 parentName
//    孩子名 studentName
//    孩子年龄 age
//    手机 phone
    private Act act;
    private User user;
    private String parentName;
    private String studentName;
    private int age;
    private String phone;

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
