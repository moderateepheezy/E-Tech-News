package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 3/27/17.
 */

public class News {

    public String caption;
    public String content;
    public String newspaper_id;
    public String thumbnail;
    public String user;
    public Long created_on;


    public News(){

    }

    public News(String caption, String content, String newspaper_id, String thumbnail, String user, Long created_on) {
        this.caption = caption;
        this.content = content;
        this.newspaper_id = newspaper_id;
        this.thumbnail = thumbnail;
        this.user = user;
        this.created_on = created_on;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNewspaper_id() {
        return newspaper_id;
    }

    public void setNewspaper_id(String newspaper_id) {
        this.newspaper_id = newspaper_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
