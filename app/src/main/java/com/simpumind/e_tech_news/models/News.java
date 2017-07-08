package com.simpumind.e_tech_news.models;

import java.io.Serializable;

/**
 * Created by simpumind on 3/27/17.
 */

public class News{

    public Caption caption;
    public Content content;
    public String newspaper_id;
    public String thumbnail;
    public String main_news_id;
    public Boolean status;
    public long created_on;


    public News(){

    }

    public News(Caption caption, Content content, String newspaper_id, String thumbnail, String main_news_id, Boolean status, long created_on) {
        this.caption = caption;
        this.content = content;
        this.newspaper_id = newspaper_id;
        this.thumbnail = thumbnail;
        this.main_news_id = main_news_id;
        this.status = status;
        this.created_on = created_on;
    }

    public Caption getCaption() {
        return caption;
    }

    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
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

    public String getMain_news_id() {
        return main_news_id;
    }

    public void setMain_news_id(String main_news_id) {
        this.main_news_id = main_news_id;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public long getCreated_on() {
        return created_on;
    }

    public void setCreated_on(long created_on) {
        this.created_on = created_on;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (status != news.status) return false;
        if (created_on != news.created_on) return false;
        if (caption != null ? !caption.equals(news.caption) : news.caption != null) return false;
        if (content != null ? !content.equals(news.content) : news.content != null) return false;
        if (newspaper_id != null ? !newspaper_id.equals(news.newspaper_id) : news.newspaper_id != null)
            return false;
        if (thumbnail != null ? !thumbnail.equals(news.thumbnail) : news.thumbnail != null)
            return false;
        return main_news_id != null ? main_news_id.equals(news.main_news_id) : news.main_news_id == null;

    }

    @Override
    public int hashCode() {
        int result = caption != null ? caption.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (newspaper_id != null ? newspaper_id.hashCode() : 0);
        result = 31 * result + (thumbnail != null ? thumbnail.hashCode() : 0);
        result = 31 * result + (main_news_id != null ? main_news_id.hashCode() : 0);
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (int) (created_on ^ (created_on >>> 32));
        return result;
    }
}
