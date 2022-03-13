package com.forbitbd.myfin.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.androidutils.utils.AppPreference;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.Dashboard;
import com.google.android.material.button.MaterialButton;


public class DashboardScene extends Fragment implements DashboardContract.View, View.OnClickListener {


    private Communicator communicator;

    private DashboardPresenter mPresenter;

    private TextView tvBalance,tvIncome,tvExpenses,tvNumberOfAccounts,tvNumberOfTransactions;

    private MaterialButton btnCreateAccount,btnCreateTransaction,btnAccounts,
            btnTransactions,btnTrialBalance,btnMonthlyReport,
            btnDailyReport,btnCashFlow,btnTopTen,btnLatestTen;



    public DashboardScene() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = (Communicator) getActivity();
        mPresenter = new DashboardPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvBalance = view.findViewById(R.id.balance_in_cash);
        tvIncome = view.findViewById(R.id.total_income);
        tvExpenses = view.findViewById(R.id.total_expenses);
        tvNumberOfAccounts = view.findViewById(R.id.number_of_accounts);
        tvNumberOfTransactions = view.findViewById(R.id.number_of_transactions);

        btnCreateAccount = view.findViewById(R.id.btn_create_account);
        btnAccounts = view.findViewById(R.id.btn_accounts);
        btnCreateTransaction = view.findViewById(R.id.btn_create_transactions);
        btnTransactions = view.findViewById(R.id.btn_transactions);
        btnTrialBalance = view.findViewById(R.id.btn_trial_balance);
        btnMonthlyReport = view.findViewById(R.id.btn_monthly_report);
        btnDailyReport = view.findViewById(R.id.btn_daily_report);
        btnCashFlow = view.findViewById(R.id.btn_cash_flow);
        btnTopTen = view.findViewById(R.id.btn_top_ten);
        btnLatestTen = view.findViewById(R.id.btn_latest_ten);

        btnCreateAccount.setOnClickListener(this);
        btnAccounts.setOnClickListener(this);
        btnCreateTransaction.setOnClickListener(this);
        btnTransactions.setOnClickListener(this);
        btnTrialBalance.setOnClickListener(this);
        btnMonthlyReport.setOnClickListener(this);
        btnDailyReport.setOnClickListener(this);
        btnCashFlow.setOnClickListener(this);
        btnTopTen.setOnClickListener(this);
        btnLatestTen.setOnClickListener(this);


        bind(communicator.getDashboard());


    }


    @Override
    public void onResume() {
        super.onResume();
        communicator.setTitle(communicator.getProject().getName().concat(" ").concat(getString(R.string.overview)));
    }

    @Override
    public void bind(Dashboard dashboard) {
        tvBalance.setText(String.valueOf(dashboard.getBalance()));
        tvIncome.setText(String.valueOf(dashboard.getIncome()));
        tvExpenses.setText(String.valueOf(dashboard.getExpenses()));
        tvNumberOfAccounts.setText(String.valueOf(dashboard.getAccounts_count()).concat(" Nos"));
        tvNumberOfTransactions.setText(String.valueOf(dashboard.getTransactions_count()).concat(" Nos"));

        mPresenter.controlVisibility(communicator.getFinancePermission());
    }

    @Override
    public void openCreateAccountScene() {
        communicator.openCreateAccountScene(null);
    }

    @Override
    public void openAllAccountScene() {
        communicator.openAccountsScene();
    }

    @Override
    public void openCreateTransactionScene() {
        communicator.openCreateTransactionScene(null);
    }

    @Override
    public void openAllTransactionScene() {
        communicator.openTransactionsScene();
    }

    @Override
    public void openTrialBalanceScene() {
        communicator.openTrialBalanceScene();
    }

    @Override
    public void openMonthlyReportScene() {
        communicator.openMonthlyReportScene();
    }

    @Override
    public void openDailyReportScene() {
        communicator.openDailyReportScene();
    }

    @Override
    public void openCashFlowScene() {
        communicator.openCashFlowScene();
    }

    @Override
    public void openTopTenScene() {
        communicator.openTopTenScene();
    }

    @Override
    public void openLatestTenScene() {
        communicator.openLatestTenScene();
    }

    @Override
    public void controlVisibility(SharedProject.Permission financePermission) {
        btnCreateAccount.setEnabled(financePermission.isWrite());
        btnCreateTransaction.setEnabled(financePermission.isWrite());
    }

    @Override
    public void onClick(View v) {
        AppPreference.getInstance(getContext()).increaseCounter();
        int counter = AppPreference.getInstance(getContext()).getCounter();
        if(v==btnCreateAccount){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openCreateAccountScene();
            }

        }else if(v==btnAccounts){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openAllAccountScene();
            }

        }else if(v==btnCreateTransaction){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openCreateTransactionScene();
            }

        }else if(v==btnTransactions){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openAllTransactionScene();
            }

        }else if(v==btnTrialBalance){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openTrialBalanceScene();
            }

        }else if(v==btnMonthlyReport){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openMonthlyReportScene();
            }

        }else if(v==btnDailyReport){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openDailyReportScene();
            }

        }else if(v==btnCashFlow){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openCashFlowScene();
            }

        }else if(v==btnTopTen){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openTopTenScene();
            }

        }else if(v==btnLatestTen){
            if(counter%5==0){
                communicator.showInterstitialAd();
            }else{
                mPresenter.openLatestTenScene();
            }

        }
    }
}