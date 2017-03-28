package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 3/27/17.
 */

public class NewsPaper {

    public int content_provider;
    public String cost;
    public String  logo;
    public String method;
    public String paper_name;
    public String validity;

    public NewsPaper(){

    }

    public NewsPaper(String cost, String logo, String method, String paper_name, String validity, int content_provider) {
        this.cost = cost;
        this.logo = logo;
        this.method = method;
        this.paper_name = paper_name;
        this.validity = validity;
        this.content_provider = content_provider;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
