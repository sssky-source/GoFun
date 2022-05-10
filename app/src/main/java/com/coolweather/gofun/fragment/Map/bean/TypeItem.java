package com.coolweather.gofun.fragment.Map.bean;

import java.io.Serializable;

public class TypeItem implements Serializable {
    private int image;
    private String type;

    public TypeItem(int image,String type){
        this.image = image;
        this.type = type;
    }

    public TypeItem(){

    }

    public int getImage() {
        return image;
    }


    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
