package com.forbitbd.myfin.accounts;

import com.forbitbd.myfin.models.Account;

public interface AccountsSceneContract {

    interface Presenter{
        void deleteAccountFromDatabase(Account account);
    }

    interface View{
        void removeAccountFromAdapter(Account account);
    }
}
