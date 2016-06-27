package com.qysports.funfootball.entity;

import com.qysports.funfootball.base.BaseEntity;

public class CoachComment extends BaseEntity {
    private String coachUserId;
    private User user;
    /**
     * 评价类型，1-好，2-一般，3-需改善
     */
    private int type;
    private String date;
    private String content;

    public String getCoachUserId() {
        return coachUserId;
    }

    public void setCoachUserId(String coachUserId) {
        this.coachUserId = coachUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
