package com.forbitbd.myfin.account_detail;

import com.forbitbd.myfin.models.AccountInfo;

public interface AccountDetailSceneContract {

    interface Presenter{
        void renderAccountInfo(AccountInfo info);
    }

    interface View{
        void renderAccountInfo(AccountInfo info);
    }
}
