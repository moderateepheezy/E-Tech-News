package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 3/27/17.
 */

public class NewsPaper {

    public String cost;
    public String  logo;
    public String method;
    public String paper_name;
    public String validity;

    public NewsPaper(){

    }

    public NewsPaper(String cost, String logo, String method, String paper_name, String validity) {
        this.cost = cost;
        this.logo = logo;
        this.method = method;
        this.paper_name = paper_name;
        this.validity = validity;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsPaper newsPaper = (NewsPaper) o;

        if (cost != null ? !cost.equals(newsPaper.cost) : newsPaper.cost != null) return false;
        if (logo != null ? !logo.equals(newsPaper.logo) : newsPaper.logo != null) return false;
        if (method != null ? !method.equals(newsPaper.method) : newsPaper.method != null)
            return false;
        if (paper_name != null ? !paper_name.equals(newsPaper.paper_name) : newsPaper.paper_name != null)
            return false;
        return validity != null ? validity.equals(newsPaper.validity) : newsPaper.validity == null;

    }

    @Override
    public int hashCode() {
        int result = cost != null ? cost.hashCode() : 0;
        result = 31 * result + (logo != null ? logo.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (paper_name != null ? paper_name.hashCode() : 0);
        result = 31 * result + (validity != null ? validity.hashCode() : 0);
        return result;
    }


}
