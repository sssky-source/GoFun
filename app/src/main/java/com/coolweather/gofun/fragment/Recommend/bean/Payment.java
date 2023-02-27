package com.coolweather.gofun.fragment.Recommend.bean;

//悬赏
public class Payment {

    //悬赏价钱
    private Integer payment1;

    //类型 买卖
    private Integer type;

    public Payment(Integer payment1, Integer type) {
        this.payment1 = payment1;
        this.type = type;
    }

    public Integer getPayment1() {
        return payment1;
    }

    public void setPayment1(Integer payment1) {
        this.payment1 = payment1;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
