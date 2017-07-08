package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 7/1/17.
 */

public class Content {

    public String english;
    public String french;


    public Content() {
    }

    public Content(String english, String french) {
        this.english = english;
        this.french = french;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        if (english != null ? !english.equals(content.english) : content.english != null)
            return false;
        return french != null ? french.equals(content.french) : content.french == null;

    }

    @Override
    public int hashCode() {
        int result = english != null ? english.hashCode() : 0;
        result = 31 * result + (french != null ? french.hashCode() : 0);
        return result;
    }
}
