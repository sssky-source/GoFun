package com.coolweather.gofun.fragment.Mine.bean;

import java.util.List;

public class TagChange {
    private Integer id;
    private String name;
    public List<Tag> Tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTag() {
        return Tag;
    }

    public void setTag(List<Tag> Tag) {
        this.Tag = Tag;
    }
}
