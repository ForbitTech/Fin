package com.forbitbd.myfin;

import android.graphics.Bitmap;

import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.myfin.models.Account;
import com.forbitbd.myfin.models.Dashboard;
import com.forbitbd.myfin.models.TransactionResponse;

public interface Communicator {

    void setTitle(String title);

    void openCreateAccountScene(Account account);
    void openAccountsScene();
    void openTransactionsScene();
    void openCreateTransactionScene(TransactionResponse transactionResponse);
    void openTransactionDetailScene(TransactionResponse transactionResponse);
    void backToDashboard();
    void removeAccountFromDashboard(Account account);
    void addAccountToDashboard(Account account);
    void updateAccountToDashboard(Account account);
    void openAccountDetailScene(Account account,int fromWhichScene);
    void openTrialBalanceScene();
    void openMonthlyReportScene();
    void openDailyReportScene();
    void openCashFlowScene();
    void openTopTenScene();
    void openLatestTenScene();
    void openCropImageActivity(int width,int height);


    Project getProject();
    Dashboard getDashboard();
    Bitmap getTransactionBitmap();
    Account getUpdateAccount();
    Account getDetailAccount();
    byte[] getTransactionByteArray();
    void addTransactionToDashboard(TransactionResponse transactionResponse);

    TransactionResponse getUpdateTransaction();
    void deleteTransactionFromDashboard();
    void updateTransactionInDashboard(TransactionResponse newTransaction);
    void openZoomImageScene(String path);
    String getZoomImagePath();

    SharedProject.Permission getFinancePermission();

    void showToast(String message);

    void hideKeyboard();

    void showInterstitialAd();
}
