package com.forbitbd.myfin.dashboard;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.myfin.api.FinanceClient;
import com.forbitbd.myfin.models.Dashboard;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPresenter implements DashboardContract.Presenter {

    private DashboardContract.View mView;


    public DashboardPresenter(DashboardContract.View mView) {
        this.mView = mView;
    }


    @Override
    public void openCreateAccountScene() {
        mView.openCreateAccountScene();
    }

    @Override
    public void openAllAccountScene() {
        mView.openAllAccountScene();
    }

    @Override
    public void openCreateTransactionScene() {
        mView.openCreateTransactionScene();
    }

    @Override
    public void openAllTransactionScene() {
        mView.openAllTransactionScene();
    }

    @Override
    public void openTrialBalanceScene() {
        mView.openTrialBalanceScene();
    }

    @Override
    public void openMonthlyReportScene() {
        mView.openMonthlyReportScene();
    }

    @Override
    public void openDailyReportScene() {
        mView.openDailyReportScene();
    }

    @Override
    public void openCashFlowScene() {
        mView.openCashFlowScene();
    }

    @Override
    public void openTopTenScene() {
        mView.openTopTenScene();
    }

    @Override
    public void openLatestTenScene() {
        mView.openLatestTenScene();
    }

    @Override
    public void controlVisibility(SharedProject.Permission financePermission) {
        mView.controlVisibility(financePermission);
    }
}
