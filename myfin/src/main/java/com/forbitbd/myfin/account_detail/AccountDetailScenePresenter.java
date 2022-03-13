package com.forbitbd.myfin.account_detail;

import com.forbitbd.myfin.models.AccountInfo;

public class AccountDetailScenePresenter implements AccountDetailSceneContract.Presenter {

    private AccountDetailSceneContract.View mView;

    public AccountDetailScenePresenter(AccountDetailSceneContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void renderAccountInfo(AccountInfo info) {
        mView.renderAccountInfo(info);
    }
}
