package com.coolweather.gofun.fragment.Message.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ChartMessageBean implements MultiItemEntity {
    private int itemType;
    private Integer activity_id;
    private Integer message_id;
    private Integer userid;
    private String message;
    private String createtime;
    private String username;
    private String image;

    public ChartMessageBean(int itemType,String message, String username, String image) {
        this.itemType = itemType;
        this.message = message;
        this.username = username;
        this.image = image;
    }

    public Integer getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(Integer activity_id) {
        this.activity_id = activity_id;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
