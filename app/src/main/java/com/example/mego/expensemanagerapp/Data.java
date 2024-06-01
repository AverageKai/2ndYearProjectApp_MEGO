package com.example.mego.expensemanagerapp;

public class Data {

    private int amount;
    private String type;
    private String note;
    private String id;
    private String date;

    public Data(int amount, String type, String note, String id, String date) {
        this.amount = amount;
        this.type = type;
        this.note = note;
        this.id = id;
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getNote() {
        return note;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getData() {
        return data;
    }

    private String data;

    public Data(){

    }
}
