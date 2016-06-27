package com.qysports.funfootball.entity;

import com.boredream.bdcodehelper.entity.ImageUrlInterface;

public class SimpleImageUrl implements ImageUrlInterface {

    private String url;
    private String title;
    private String link;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String getImageUrl() {
        return url;
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
