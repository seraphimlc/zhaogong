package com.dagong.pojo;

/**
 * Created by liuchang on 16/3/24.
 */
public enum  DegreeType {
    NO_LIMIT("不限",1),
    PRIMARY("小学",2),
    JUNIOR("初中",3),
    SENIOR("高中",4),
    COLLAGE("大专",5),
    UNIVERSITY("本科",6),
    MASTER("硕士",7),
    DOCTOR("博士",8),
    TECHNICAL("技校",9),
    PROFESSIONAL("中专",10)
    ;

    DegreeType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public static DegreeType getDegreeType(String name){
        for (DegreeType degreeType : DegreeType.values()) {
            if(degreeType.getName().equals(name)){
                return degreeType;
            }
        }
        return null;
    }
}
