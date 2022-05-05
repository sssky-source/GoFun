package com.coolweather.gofun.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private Bitmap headimage;
    private String email;

    public User(){}

    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    public User(String username,String password,String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getHeadimage() {
        return headimage;
    }

    public void setHeadimage(Bitmap headimage) {
        this.headimage = headimage;
    }
}
