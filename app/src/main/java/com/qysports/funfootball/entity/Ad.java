package com.qysports.funfootball.entity;

import com.boredream.bdcodehelper.entity.ImageUrlInterface;
import com.qysports.funfootball.base.BaseEntity;

public class Ad extends BaseEntity implements ImageUrlInterface {

    /**
     * 1-课程，2-赛事，3-活动
     */
    private int type;
    private String title;
    private String imgUrl;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getImageUrl() {
        return imgUrl;
    }

    @Override
    public String getImageTitle() {
        return title;
    }

    @Override
    public String getImageLink() {
        return link;
    }
}
