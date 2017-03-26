package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 3/25/17.
 */

public class MyList {

    private String vendorName;
    private int vendorIcon;

    //constructor initializing values
    public MyList(String vendorName, int vendorIcon) {
        this.vendorName = vendorName;
        this.vendorIcon = vendorIcon;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getVendorIcon() {
        return vendorIcon;
    }

    public void setVendorIcon(int vendorIcon) {
        this.vendorIcon = vendorIcon;
    }

}
