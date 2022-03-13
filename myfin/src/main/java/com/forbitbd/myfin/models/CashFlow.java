package com.forbitbd.myfin.models;

import java.util.Date;

public class CashFlow {

    private Date date;
    private double amount;


    public CashFlow() {
    }

    public CashFlow(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addAmount(double amount){
        this.amount = this.amount+amount;
    }

    public void reduceAmount(double amount){
        this.amount = this.amount-amount;
    }
}
