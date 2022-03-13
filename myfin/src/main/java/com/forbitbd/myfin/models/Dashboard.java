package com.forbitbd.myfin.models;


import com.forbitbd.androidutils.utils.MyUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Dashboard {

    private double income;
    private double expenses;
    private double balance;
    private int accounts_count;
    private int transactions_count;
    private List<Account> accounts;
    private List<TransactionResponse> transactions;


    public Dashboard() {
    }


    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getAccounts_count() {
        return accounts_count;
    }

    public void setAccounts_count(int accounts_count) {
        this.accounts_count = accounts_count;
    }

    public int getTransactions_count() {
        return transactions_count;
    }

    public void setTransactions_count(int transactions_count) {
        this.transactions_count = transactions_count;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }

    public void update(){
        this.accounts_count = accounts.size();
        this.transactions_count = transactions.size();

        List<Account> incomeAccounts = getIncomeAccounts();
        List<Account> expenseAccounts = getExpenseAccounts();

        List<TransactionResponse> cashInTransactions = getCashInTransaction("Cash");
        List<TransactionResponse> cashOutTransactions = getCashOutTransaction("Cash");
        this.balance = getTotal(cashInTransactions)-getTotal(cashOutTransactions);
        this.income = getIncomeTotal(incomeAccounts);
        this.expenses = getExpensesTotal(expenseAccounts);
    }

    private List<Account> getIncomeAccounts(){
        List<Account> tmpList = new ArrayList<>();
        for(Account x:accounts){
            if(x.getType()==1){
                tmpList.add(x);
            }
        }
        return tmpList;
    }

    private List<Account> getExpenseAccounts(){
        List<Account> tmpList = new ArrayList<>();
        for(Account x:accounts){
            if(x.getType()==0){
                tmpList.add(x);
            }
        }
        return tmpList;
    }

    public double getExpensesTotal(List<Account> expensesAccounts){
        double amount = 0;

        for(Account x:expensesAccounts){
            List<TransactionResponse> trs = getCashInTransaction(x.getName());
            amount = amount+getTotal(trs);
        }
        return amount;
    }

    public double getIncomeTotal(List<Account> incomeAccounts){
        double amount = 0;

        for(Account x:incomeAccounts){
            List<TransactionResponse> trs = getCashOutTransaction(x.getName());
            amount = amount+getTotal(trs);
        }
        return amount;
    }

    private List<TransactionResponse> getCashInTransaction(String accountName){
        List<TransactionResponse> tmpList = new ArrayList<>();
        for(TransactionResponse x:transactions){
            if(x.getTo().getName().equals(accountName)){
                tmpList.add(x);
            }
        }
        return tmpList;
    }

    private List<TransactionResponse> getCashOutTransaction(String accountName){
        List<TransactionResponse> tmpList = new ArrayList<>();
        for(TransactionResponse x:transactions){
            if(x.getFrom().getName().equals(accountName)){
                tmpList.add(x);
            }
        }
        return tmpList;
    }

    private double getTotal(List<TransactionResponse> transactionResponses){
        double total = 0;
        for(TransactionResponse x:transactionResponses){
            total = total+x.getAmount();
        }
        return total;
    }

    public AccountInfo getAccountInfo(Account account){
        double debit=0,credit=0;
        int count = 0;

        for (TransactionResponse x: transactions){
            if(x.getFrom().get_id().equals(account.get_id())){
                credit=credit+x.getAmount();
                count++;
            }else if(x.getTo().get_id().equals(account.get_id())){
                debit = debit+x.getAmount();
                count++;
            }
        }

        String text ="";
        if(debit>credit){
            text = "Balance C/D on Credit";
        }else{
            text = "Balance C/D on Debit";
        }

//        double balance = Math.abs(debit-credit);

        return new AccountInfo(account.getName(),count,debit,credit,account.getOpening_balance(),text);

    }

    public List<TransactionResponse> getFilterTransaction(Account account){
        List<TransactionResponse> tmpList = new ArrayList<>();

        for(TransactionResponse x:transactions){
            if(x.getFrom().get_id().equals(account.get_id()) || x.getTo().get_id().equals(account.get_id())){
                tmpList.add(x);
            }
        }

        return tmpList;
    }

    public List<TransactionResponse> getInTransactions(Account account){
        List<TransactionResponse> tmpList = new ArrayList<>();

        for(TransactionResponse x:transactions){
            if(x.getTo().get_id().equals(account.get_id())){
                tmpList.add(x);
            }
        }

        return tmpList;
    }

    public List<TransactionResponse> getOutTransactions(Account account){
        List<TransactionResponse> tmpList = new ArrayList<>();

        for(TransactionResponse x:transactions){
            if(x.getFrom().get_id().equals(account.get_id())){
                tmpList.add(x);
            }
        }

        return tmpList;
    }


    public List<TrialBalance> getTrialBalances(){
        List<TrialBalance> trialBalanceList = new ArrayList<>();

        for (Account x : accounts){
            trialBalanceList.add(new TrialBalance(x));
        }

        for (TrialBalance x: trialBalanceList){

            for (TransactionResponse y: transactions){
                if(x.getAccount().get_id().equals(y.getFrom().get_id()) ){
                    x.setCredit(x.getCredit()+y.getAmount());
                }else if(x.getAccount().get_id().equals(y.getTo().get_id())){
                    x.setDebit(x.getDebit()+y.getAmount());
                }
            }
        }

        Collections.sort(trialBalanceList, new Comparator<TrialBalance>() {
            @Override
            public int compare(TrialBalance o1, TrialBalance o2) {
                return (int) (Math.max(o2.getDebit(),o2.getCredit()) - Math.max(o1.getDebit(),o1.getCredit()));
            }
        });

        return trialBalanceList;
    }


    public List<TrialBalance> getMonthlyTrialBalance(int year,int month){
        List<TransactionResponse> tmpList = new ArrayList<>();
        List<Account> accountList = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        for (TransactionResponse x: transactions){
            c.setTime(x.getDate());

            if(c.get(Calendar.YEAR)==year && c.get(Calendar.MONTH)==month){
                tmpList.add(x);
                if(!exists(accountList,x.getFrom())){
                    accountList.add(x.getFrom());
                }

                if(!exists(accountList,x.getTo())){
                    accountList.add(x.getTo());
                }


            }
        }

        List<TrialBalance> trialBalanceList = new ArrayList<>();

        for (Account x : accountList){
            trialBalanceList.add(new TrialBalance(x));
        }

        for (TrialBalance x: trialBalanceList){

            for (TransactionResponse y: tmpList){
                if(x.getAccount().get_id().equals(y.getFrom().get_id()) ){
                    x.setCredit(x.getCredit()+y.getAmount());
                }else if(x.getAccount().get_id().equals(y.getTo().get_id())){
                    x.setDebit(x.getDebit()+y.getAmount());
                }
            }
        }

        Collections.sort(trialBalanceList, new Comparator<TrialBalance>() {
            @Override
            public int compare(TrialBalance o1, TrialBalance o2) {
                return (int) (Math.max(o2.getDebit(),o2.getCredit()) - Math.max(o1.getDebit(),o1.getCredit()));
            }
        });



        return trialBalanceList;
    }

    private boolean exists(List<Account> accountList,Account account){
        for(Account x: accountList){
            if(x.get_id().equals(account.get_id())){
                return true;
            }
        }

        return false;
    }


    public List<TransactionResponse> getMonthlyTransaction(int year,int month){
        List<TransactionResponse> tmpList = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        for (TransactionResponse x: transactions){
            c.setTime(x.getDate());

            if(c.get(Calendar.YEAR)==year && c.get(Calendar.MONTH)==month){
                tmpList.add(x);
            }
        }

        return tmpList;
    }

    public List<TransactionResponse> getDailyTransactions(Date date){
        List<TransactionResponse> tmpLIst = new ArrayList<>();

        for (TransactionResponse x: transactions){
            if(MyUtil.getStringDate(date).equals(MyUtil.getStringDate(x.getDate()))){
                tmpLIst.add(x);
            }
        }

        return tmpLIst;
    }

    public List<CashFlow> getCashFlow(){
        List<TransactionResponse> cashTransactionList = new ArrayList<>();

        for (TransactionResponse x: transactions){
            if(x.getFrom().getName().equals("Cash") || x.getTo().getName().equals("Cash") ){
                cashTransactionList.add(x);
            }
        }


        Collections.sort(cashTransactionList, new Comparator<TransactionResponse>() {
            @Override
            public int compare(TransactionResponse o1, TransactionResponse o2) {
                return (int) (o2.getDate().getTime()-o1.getDate().getTime());
            }
        });





        int duration = Math.abs(MyUtil.getDuration(cashTransactionList.get(cashTransactionList.size()-1).getDate().getTime(),cashTransactionList.get(0).getDate().getTime()));

        Date startDate = cashTransactionList.get(0).getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime (startDate);


        List<CashFlow> cashFlowList = new ArrayList<>();

        for (int i=0;i<duration;i++){
            if(i==0){
                CashFlow cashFlow = new CashFlow(startDate);
                //cashFlowList.add(new CashFlow(startDate));

                for (TransactionResponse x:cashTransactionList){
                    if(MyUtil.getStringDate(cashFlow.getDate()).equals(MyUtil.getStringDate(x.getDate()))){
                        if(x.getFrom().getName().equals("Cash")){
                            cashFlow.reduceAmount(x.getAmount());
                        }else{
                            cashFlow.addAmount(x.getAmount());
                        }
                    }
                }
                cashFlowList.add(cashFlow);

            }else{
                cal.add(Calendar.DATE,-1);
                CashFlow cashFlow = new CashFlow(cal.getTime());

                for (TransactionResponse x:cashTransactionList){
                    if(MyUtil.getStringDate(cashFlow.getDate()).equals(MyUtil.getStringDate(x.getDate()))){
                        if(x.getFrom().getName().equals("Cash")){
                            cashFlow.reduceAmount(x.getAmount());
                        }else{
                            cashFlow.addAmount(x.getAmount());
                        }
                    }
                }

                cashFlowList.add(cashFlow);

            }
        }

        return cashFlowList;
    }


    public List<TransactionResponse> getTopTen(){
        List<TransactionResponse> transactions = new ArrayList<>();
        transactions.addAll(this.transactions);

        Collections.sort(transactions, new Comparator<TransactionResponse>() {
            @Override
            public int compare(TransactionResponse o1, TransactionResponse o2) {
                return (int) (o2.getAmount()-o1.getAmount());
            }
        });

        if(transactions.size()<10){
            return transactions;
        }

        return transactions.subList(0,10);
    }

    public List<TransactionResponse> getLatestTen(){
        List<TransactionResponse> transactions = new ArrayList<>();
        transactions.addAll(this.transactions);

        Collections.sort(transactions, new Comparator<TransactionResponse>() {
            @Override
            public int compare(TransactionResponse o1, TransactionResponse o2) {
                return (int) (o2.getDate().getTime()-o1.getDate().getTime());
            }
        });

        if(transactions.size()<10){
            return transactions;
        }

        return transactions.subList(0,10);
    }
}
