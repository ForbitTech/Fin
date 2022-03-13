package com.forbitbd.myfin.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {

    private String _id;
    private String name;
    private String project;
    private int type;
    private double amount;
    private double opening_balance;
    private Date created_at;


    public Account() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public double getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(double opening_balance) {
        this.opening_balance = opening_balance;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
