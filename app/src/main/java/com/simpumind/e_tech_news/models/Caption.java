package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 7/1/17.
 */

public class Caption {

    public String english;
    public String french;


    public Caption() {
    }

    public Caption(String english, String french) {
        this.english = english;
        this.french = french;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Caption caption = (Caption) o;

        if (english != null ? !english.equals(caption.english) : caption.english != null)
            return false;
        return french != null ? french.equals(caption.french) : caption.french == null;

    }

    @Override
    public int hashCode() {
        int result = english != null ? english.hashCode() : 0;
        result = 31 * result + (french != null ? french.hashCode() : 0);
        return result;
    }
}
