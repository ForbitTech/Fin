package com.forbitbd.myfin.create_account;

import android.util.Log;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.myfin.api.FinanceClient;
import com.forbitbd.myfin.models.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountPresenter implements CreateAccountContract.Presenter {

    private CreateAccountContract.View mView;

    public CreateAccountPresenter(CreateAccountContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void backToDashboard() {
        mView.backToDashboard();
    }

    @Override
    public void updateUI(Account account) {
        mView.updateUI(account);
    }

    @Override
    public void updateAccount(Account account) {
        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);
        client.updateAccount(account.getProject(),account.get_id(),account)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.isSuccessful()){
                            Log.d("HHHHH",response.body().getType()+"");
                            mView.updateAccountToDashboard(response.body());
                        }else {
                            mView.showToast("Fetch Error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {
                        mView.showToast("Server Error");
                    }
                });
    }

    @Override
    public void saveAccount(Account account) {

        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);
        client.saveAccount(account.getProject(),account)
                .enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        if(response.isSuccessful()){
                            mView.addAccountToDashboard(response.body());
                        }else if(response.code()==300){
                            mView.showToast("Account Already Exist");
                        }else{
                            mView.showToast("Error Happened in Saving Account");
                        }

                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {

                    }
                });

    }

    @Override
    public boolean validate(Account account) {
        mView.clearPreError();

        if(account.getName()==null || account.getName().equals("")){
            mView.showValidationError("Account Name Should not empty",1);
            return false;
        }

        return true;
    }

    @Override
    public Account createAccountFromData() {
        return mView.createAccountFromData();
    }
}
