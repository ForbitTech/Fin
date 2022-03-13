package com.forbitbd.myfin.accounts;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.myfin.api.FinanceClient;
import com.forbitbd.myfin.models.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountsScenePresenter implements AccountsSceneContract.Presenter {

    private AccountsSceneContract.View mView;

    public AccountsScenePresenter(AccountsSceneContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void deleteAccountFromDatabase(Account account) {
        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);

        client.deleteAccount(account.getProject(),account.get_id())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            mView.removeAccountFromAdapter(account);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
    }
}
