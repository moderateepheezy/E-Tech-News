package com.simpumind.e_tech_news.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by simpumind on 3/27/17.
 */

public class NewsPaper implements Serializable{

    public String  logo;
    public String method;
    public String paper_name;
    public Boolean display;
    public HashMap<String, News> news;
    public String status;

    public NewsPaper() {
    }

    public NewsPaper(String logo, String method, String paper_name, Boolean display, HashMap<String, News> news, String status) {
        this.logo = logo;
        this.method = method;
        this.paper_name = paper_name;
        this.display = display;
        this.news = news;
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public HashMap<String, News> getNews() {
        return news;
    }

    public void setNews(HashMap<String, News> news) {
        this.news = news;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsPaper newsPaper = (NewsPaper) o;

        if (logo != null ? !logo.equals(newsPaper.logo) : newsPaper.logo != null) return false;
        if (method != null ? !method.equals(newsPaper.method) : newsPaper.method != null)
            return false;
        if (paper_name != null ? !paper_name.equals(newsPaper.paper_name) : newsPaper.paper_name != null)
            return false;
        if (display != null ? !display.equals(newsPaper.display) : newsPaper.display != null)
            return false;
        if (news != null ? !news.equals(newsPaper.news) : newsPaper.news != null) return false;
        return status != null ? status.equals(newsPaper.status) : newsPaper.status == null;

    }

    @Override
    public int hashCode() {
        int result = logo != null ? logo.hashCode() : 0;
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (paper_name != null ? paper_name.hashCode() : 0);
        result = 31 * result + (display != null ? display.hashCode() : 0);
        result = 31 * result + (news != null ? news.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
