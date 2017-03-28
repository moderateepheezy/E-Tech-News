package com.simpumind.e_tech_news.models;

/**
 * Created by simpumind on 3/27/17.
 */

public class User {

    public String user_id;
    public String username;
    public String email;
    public String msisdn;
    public String physical_address;
    public String password;

    public User(String user_id, String username, String email, String msisdn, String physical_address, String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.msisdn = msisdn;
        this.physical_address = physical_address;
        this.password = password;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPhysical_address() {
        return physical_address;
    }

    public void setPhysical_address(String physical_address) {
        this.physical_address = physical_address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
