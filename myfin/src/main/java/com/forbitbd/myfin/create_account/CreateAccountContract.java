package com.forbitbd.myfin.create_account;

import com.forbitbd.myfin.Form;
import com.forbitbd.myfin.models.Account;

public interface CreateAccountContract {

    interface Presenter{
        void backToDashboard();
        void updateUI(Account account);
        void updateAccount(Account account);
        void saveAccount(Account account);
        boolean validate(Account account);
        Account createAccountFromData();
    }

    interface View extends Form{
        void backToDashboard();
        void showToast(String message);
        void updateUI(Account account);
        void addAccountToDashboard(Account account);
        void updateAccountToDashboard(Account account);
        Account createAccountFromData();
    }
}
