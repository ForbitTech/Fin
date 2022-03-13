package com.forbitbd.myfin.models;

public class TrialBalance {

    private Account account;
    private double debit;
    private double credit;

    public TrialBalance() {
    }

    public TrialBalance(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
}
