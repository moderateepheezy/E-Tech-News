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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (caption != null ? !caption.equals(news.caption) : news.caption != null) return false;
        if (content != null ? !content.equals(news.content) : news.content != null) return false;
        if (newspaper_id != null ? !newspaper_id.equals(news.newspaper_id) : news.newspaper_id != null)
            return false;
        if (thumbnail != null ? !thumbnail.equals(news.thumbnail) : news.thumbnail != null)
            return false;
        if (user != null ? !user.equals(news.user) : news.user != null) return false;
        return created_on != null ? created_on.equals(news.created_on) : news.created_on == null;

    }

    @Override
    public int hashCode() {
        int result = caption != null ? caption.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (newspaper_id != null ? newspaper_id.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (created_on != null ? created_on.hashCode() : 0);
        return result;
    }
}
