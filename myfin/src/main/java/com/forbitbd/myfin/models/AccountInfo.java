package com.forbitbd.myfin.models;

public class AccountInfo {
    private String name;
    private int count;
    private double debit;
    private double credit;
    private double opening_balance;
    private String balance_text;


    public AccountInfo() {
    }

    public AccountInfo(String name ,int count, double debit, double credit, double opening_balance, String balance_text) {
        this.name = name;
        this.count = count;
        this.debit = debit;
        this.credit = credit;
        this.opening_balance = opening_balance;
        this.balance_text = balance_text;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getBalance() {
        return opening_balance+debit-credit;
    }

    public double getOpening_balance() {
        return opening_balance;
    }

    public void setOpening_balance(double opening_balance) {
        this.opening_balance = opening_balance;
    }

    public String getBalance_text() {
        return balance_text;
    }

    public void setBalance_text(String balance_text) {
        this.balance_text = balance_text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
