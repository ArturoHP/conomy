package com.navtech.conomy.classes;

public class Transaction {

    double amount;
    String date;
    int month;
    String name;
    int type;
    int week_month_number;
    int week_year_number;
    boolean done;
    String key;


    public Transaction(double amount, String date, int month, String name, int type, int week_month_number, int week_year_number,boolean done,String key) {
        this.amount = amount;
        this.date = date;
        this.month = month;
        this.name = name;
        this.type = type;
        this.week_month_number = week_month_number;
        this.week_year_number = week_year_number;
        this.done = done;
        this.key = key;
    }

    public Transaction(){

    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeek_month_number() {
        return week_month_number;
    }

    public void setWeek_month_number(int week_month_number) {
        this.week_month_number = week_month_number;
    }

    public int getWeek_year_number() {
        return week_year_number;
    }

    public void setWeek_year_number(int week_year_number) {
        this.week_year_number = week_year_number;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
