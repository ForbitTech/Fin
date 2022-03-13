package com.forbitbd.myfin;

import com.forbitbd.myfin.models.Dashboard;

public interface FinanceContract {

    interface Presenter{
        void initialize();
        void getDashboard(String projectId);
    }

    interface View{
        void setupToolbar(int id);
        void setupBanner(int id);
        void initialize();
        void enterDashboardScene(Dashboard dashboard);
    }
}
