package com.licc.task.enums;


public enum EIacAdStatus {
    DAISHUA(0,"待刷"),
    JINGXINGZHONG(1,"进行中"),
    WANCHENG(2,"完成");
    private int key;
    private String value;
    EIacAdStatus(int key, String value){
        this.key = key;
        this.value = value;
    }
    public int getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }

}
