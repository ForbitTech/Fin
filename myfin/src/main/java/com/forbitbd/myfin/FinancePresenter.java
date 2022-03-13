package com.forbitbd.myfin;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.myfin.api.FinanceClient;
import com.forbitbd.myfin.models.Dashboard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancePresenter implements FinanceContract.Presenter {

    private FinanceContract.View mView;

    public FinancePresenter(FinanceContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void initialize() {
        mView.initialize();
    }

    @Override
    public void getDashboard(String projectId) {
        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);

        client.getDashboard(projectId)
                .enqueue(new Callback<Dashboard>() {
                    @Override
                    public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                        if(response.isSuccessful()){
                            mView.enterDashboardScene(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Dashboard> call, Throwable t) {

                    }
                });
    }
}
