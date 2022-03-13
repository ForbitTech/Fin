package com.forbitbd.myfin.dashboard;

import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.myfin.models.Dashboard;

public interface DashboardContract {

    interface Presenter{
//        void getDashboardData(String projectID);
        void openCreateAccountScene();
        void openAllAccountScene();
        void openCreateTransactionScene();
        void openAllTransactionScene();
        void openTrialBalanceScene();
        void openMonthlyReportScene();
        void openDailyReportScene();
        void openCashFlowScene();
        void openTopTenScene();
        void openLatestTenScene();
        void controlVisibility(SharedProject.Permission financePermission);
    }

    interface View{
        void bind(Dashboard dashboard);

        void openCreateAccountScene();
        void openAllAccountScene();
        void openCreateTransactionScene();
        void openAllTransactionScene();
        void openTrialBalanceScene();
        void openMonthlyReportScene();
        void openDailyReportScene();
        void openCashFlowScene();
        void openTopTenScene();
        void openLatestTenScene();
        void controlVisibility(SharedProject.Permission financePermission);
    }
}
