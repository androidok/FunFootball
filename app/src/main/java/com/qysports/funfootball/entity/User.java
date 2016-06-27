package com.qysports.funfootball.entity;


import com.qysports.funfootball.base.BaseEntity;

import java.util.ArrayList;

public class User extends BaseEntity {

    public static final int ROLE_STUDENT = 0;
    public static final int ROLE_COACH = 1;
    public static final int ROLE_COACH_APPLYING = 2;

    private String sessionToken;

    private String nickname;

    private String username;

    /**
     * 验证手机号
     */
    private String mobilePhoneNumber;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号验证码,发送短信验证时请求使用
     */
    private String smsCode;

    /**
     * 头像图片地址
     */
    private String avatar;

    /**
     * 性别 0保密, 1男, 2女
     */
    private int gender;

    /**
     * 年龄
     */
    private int age;

    private String location;

    /**
     * 身份 0-用户 1-教练
     */
    private int role;

    // 以下为教练特有的参数
    private String coachName;
    private int coachAge;
    private String coachLocation;
    private int coachGender;
    private String idCard;
    private int trainingYears;
    private String certLevel;
    private boolean acceptTraining;
    private String coachAvatarImg;
    private String coachCertImg;
    private String career;

    private ArrayList<Course> collectCourseList;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public boolean isCoach() {
        return role == ROLE_COACH;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public int getCoachAge() {
        return coachAge;
    }

    public void setCoachAge(int coachAge) {
        this.coachAge = coachAge;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getCoachGender() {
        return coachGender;
    }

    public void setCoachGender(int coachGender) {
        this.coachGender = coachGender;
    }

    public int getTrainingYears() {
        return trainingYears;
    }

    public void setTrainingYears(int trainingYears) {
        this.trainingYears = trainingYears;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoachLocation() {
        return coachLocation;
    }

    public void setCoachLocation(String coachLocation) {
        this.coachLocation = coachLocation;
    }

    public String getCertLevel() {
        return certLevel;
    }

    public void setCertLevel(String certLevel) {
        this.certLevel = certLevel;
    }


    public boolean isAcceptTraining() {
        return acceptTraining;
    }

    public void setAcceptTraining(boolean acceptTraining) {
        this.acceptTraining = acceptTraining;
    }

    public String getCoachAvatarImg() {
        return coachAvatarImg;
    }

    public void setCoachAvatarImg(String coachAvatarImg) {
        this.coachAvatarImg = coachAvatarImg;
    }

    public String getCoachCertImg() {
        return coachCertImg;
    }

    public void setCoachCertImg(String coachCertImg) {
        this.coachCertImg = coachCertImg;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public void setCollectCourseList(ArrayList<Course> collectCourseList) {
        this.collectCourseList = collectCourseList;
    }

    public ArrayList<Course> getCollectCourseList() {
        return collectCourseList;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
