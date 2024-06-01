package com.example.mego;

public class DataClass {
    private String dataTitle;
    private String dataDesc;
    private String day;
    private String month;
    private String year;
    private String key;
    private String dateID;
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getDataTitle() {
        return dataTitle;
    }
    public String getDataDesc() {
        return dataDesc;
    }
    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
    public String getDateID() {
        return dateID;
    }
    public DataClass(String dataTitle, String dataDesc, String day,String month,String year,String dateID) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dateID = dateID;
    }
    public DataClass(){
    }
}
