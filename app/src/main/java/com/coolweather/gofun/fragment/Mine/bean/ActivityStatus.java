package com.coolweather.gofun.fragment.Mine.bean;

//获取活动状态ID 根据ID区分创建 加入 收藏
public class ActivityStatus {
    private Integer id;
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
