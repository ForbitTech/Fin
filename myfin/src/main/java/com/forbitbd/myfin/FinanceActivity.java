package com.forbitbd.myfin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.forbitbd.androidutils.models.Project;
import com.forbitbd.androidutils.models.SharedProject;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.myfin.models.Account;
import com.forbitbd.myfin.models.Dashboard;
import com.forbitbd.myfin.models.TransactionResponse;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FinanceActivity extends AppCompatActivity implements FinanceContract.View ,Communicator{

    private FinancePresenter mPresenter;

    private SharedProject sharedProject;

    private Scene dashboardScene,createAccountScene,accountsScene,accountDetailScene,
            createTransactionScene,transactionsScene,zoomImageScene,trialBalanceScene,
            monthlyReportScene,dailyTransactionScene,cashFlowScene,mCurrentScene,
            topTenScene,latestTenScene,transactionDetailScene;
    private ConstraintLayout mContainer;

    private Dashboard dashboard;

    private Bitmap transactionBitmap;

    private Account updateAccount;
    private Account detailAccount;
    private TransactionResponse updateTransaction;
    private String zoomImagePath;

    private InterstitialAd mInterstitialAd;


    private static final int CHANGE_BOUND=0;
    private static final int FADE=1;
    private static final int SLIDE=2;

    private int detailAccountOpenFrom;

    private static final int FROM_ACCOUNTS_SCENE=1;
    private static final int FROM_TRIAL_BALANCE_SCENE=2;

    private int click_counter=0;

    private final ActivityResultLauncher<CropImageContractOptions> cropImage =
            registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
                @Override
                public void onActivityResult(CropImageView.CropResult result) {
                    if(result.isSuccessful()){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(FinanceActivity.this.getContentResolver(), result.getUriContent());
                            transactionBitmap = bitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });


    private FullScreenContentCallback callback = new FullScreenContentCallback(){
        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when fullscreen content is dismissed.
            Log.d("HHHH", "The ad was dismissed.");
            loadInterAd();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            // Called when fullscreen content failed to show.
            Log.d("HHHH", "The ad failed to show.");
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when fullscreen content is shown.
            // Make sure to set your reference to null so you don't
            // show it a second time.
            mInterstitialAd = null;
            Log.d("HHHH", "The ad was shown.");
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        this.mPresenter = new FinancePresenter(this);
        this.sharedProject = (SharedProject) getIntent().getSerializableExtra(Constant.PROJECT);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                mPresenter.initialize();
                loadInterAd();
            }
        });



    }

    private void loadInterAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(FinanceActivity.this,getString(R.string.inter_ad_unit), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.d("HHHH", "onAdLoaded");

                        mInterstitialAd.setFullScreenContentCallback(callback);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("HHHH", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

    @Override
    public void setupToolbar(int id) {

        Toolbar toolbar = (Toolbar) findViewById(id);

        setSupportActionBar(toolbar);

        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);



    }

    @Override
    public void setupBanner(int id) {
        AdView mAdView = findViewById(id);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private Transition getTransition(int state){
        Transition transition = null;

        if(state==CHANGE_BOUND){
            transition = new ChangeBounds();
        }else if(state==FADE){
            transition = new Fade();
        }
        return transition;
    }

    @Override
    public void initialize() {
        setupToolbar(R.id.toolbar);
        setupBanner(R.id.adView);

        mContainer = findViewById(R.id.finance_container);
        dashboardScene = Scene.getSceneForLayout(mContainer,R.layout.scene_dashboard,this);
        createAccountScene = Scene.getSceneForLayout(mContainer,R.layout.scene_create_account,this);
        createTransactionScene = Scene.getSceneForLayout(mContainer,R.layout.scene_create_transaction,this);
        accountsScene = Scene.getSceneForLayout(mContainer,R.layout.scene_accounts,this);
        accountDetailScene = Scene.getSceneForLayout(mContainer,R.layout.scene_account_detail,this);
        transactionsScene = Scene.getSceneForLayout(mContainer,R.layout.scene_transactions,this);
        zoomImageScene = Scene.getSceneForLayout(mContainer,R.layout.scene_zoom_image,this);
        trialBalanceScene = Scene.getSceneForLayout(mContainer,R.layout.scene_trial_balance,this);
        monthlyReportScene = Scene.getSceneForLayout(mContainer,R.layout.scene_monthly_report,this);
        dailyTransactionScene = Scene.getSceneForLayout(mContainer,R.layout.scene_daily_transaction,this);
        cashFlowScene = Scene.getSceneForLayout(mContainer,R.layout.scene_cash_flow,this);
        topTenScene = Scene.getSceneForLayout(mContainer,R.layout.scene_top_ten,this);
        latestTenScene = Scene.getSceneForLayout(mContainer,R.layout.scene_latest_ten,this);
        transactionDetailScene = Scene.getSceneForLayout(mContainer,R.layout.scene_transaction_detail,this);

        mPresenter.getDashboard(sharedProject.getProject().get_id());



//        dashboardScene.enter();
    }

    @Override
    public void enterDashboardScene(Dashboard dashboard) {
        this.dashboard = dashboard;
        dashboardScene.enter();
        mCurrentScene = dashboardScene;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void openCreateAccountScene(Account account) {
        this.updateAccount = account;
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(createAccountScene,transition);
        mCurrentScene = createAccountScene;
    }

    @Override
    public void openAccountsScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(accountsScene,transition);
        mCurrentScene = accountsScene;
    }


    public void openAccountsScene(int transitionState) {
        Transition transition = getTransition(transitionState);
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(accountsScene,transition);
        mCurrentScene = accountsScene;
    }

    @Override
    public void openTransactionsScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(transactionsScene,transition);
        mCurrentScene = transactionsScene;
    }

    @Override
    public void openCreateTransactionScene(TransactionResponse transactionResponse) {
//        this.transactionBitmap = null;
        this.updateTransaction = transactionResponse;
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(createTransactionScene,transition);
        mCurrentScene = createTransactionScene;
    }

    @Override
    public void openTransactionDetailScene(TransactionResponse transactionResponse) {
        this.updateTransaction = transactionResponse;
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(transactionDetailScene,transition);
        mCurrentScene = transactionDetailScene;
    }

    @Override
    public void backToDashboard() {

        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(dashboardScene,transition);
        mCurrentScene = dashboardScene;
    }

    @Override
    public void removeAccountFromDashboard(Account account) {
        dashboard.getAccounts().remove(account);
        dashboard.update();
    }

    @Override
    public void addAccountToDashboard(Account account) {
        dashboard.getAccounts().add(account);
        dashboard.update();
        onBackPressed();
    }

    @Override
    public void updateAccountToDashboard(Account account) {
        dashboard.getAccounts().set(dashboard.getAccounts().indexOf(updateAccount),account);
        dashboard.update();
        onBackPressed();
        updateAccount = null;
    }

    @Override
    public void openAccountDetailScene(Account account,int fromWhichScene) {
        this.detailAccountOpenFrom = fromWhichScene;
        this.detailAccount = account;
        Transition transition = new Fade();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(accountDetailScene,transition);
        mCurrentScene = accountDetailScene;
    }

    @Override
    public void openTrialBalanceScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(trialBalanceScene,transition);
        mCurrentScene = trialBalanceScene;
    }

    @Override
    public void openMonthlyReportScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(monthlyReportScene,transition);
        mCurrentScene = monthlyReportScene;
    }

    @Override
    public void openDailyReportScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(dailyTransactionScene,transition);
        mCurrentScene = dailyTransactionScene;
    }

    @Override
    public void openCashFlowScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(cashFlowScene,transition);
        mCurrentScene = cashFlowScene;
    }

    @Override
    public void openTopTenScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(topTenScene,transition);
        mCurrentScene = topTenScene;
    }

    @Override
    public void openLatestTenScene() {
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(latestTenScene,transition);
        mCurrentScene = latestTenScene;
    }

    @Override
    public Project getProject() {
        return sharedProject.getProject();
    }

    @Override
    public Dashboard getDashboard() {
        return dashboard;
    }

    @Override
    public Bitmap getTransactionBitmap() {
        return transactionBitmap;
    }

    @Override
    public Account getUpdateAccount() {
        return updateAccount;
    }

    @Override
    public TransactionResponse getUpdateTransaction() {
        return updateTransaction;
    }

    @Override
    public void deleteTransactionFromDashboard() {
        dashboard.getTransactions().remove(updateTransaction);
        dashboard.update();
        updateTransaction=null;
        openTransactionsScene();
    }

    @Override
    public void updateTransactionInDashboard(TransactionResponse newTransaction) {
        dashboard.getTransactions().set(dashboard.getTransactions().indexOf(updateTransaction),newTransaction);
        dashboard.update();
        onBackPressed();
        updateTransaction = null;
        transactionBitmap = null;
    }

    @Override
    public void openZoomImageScene(String path) {
        this.zoomImagePath = path;
        Transition transition = new ChangeBounds();
        transition.setDuration(500);
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.go(zoomImageScene,transition);
        mCurrentScene = zoomImageScene;

    }

    @Override
    public String getZoomImagePath() {
        return zoomImagePath;
    }

    @Override
    public Account getDetailAccount() {
        return detailAccount;
    }

    @Override
    public byte[] getTransactionByteArray() {
        if(transactionBitmap!=null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transactionBitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
            byte[] bytes = bos.toByteArray();
            return bytes;
        }

        return null;
    }

    @Override
    public void addTransactionToDashboard(TransactionResponse transactionResponse) {
        transactionBitmap = null;
        dashboard.getTransactions().add(transactionResponse);
        dashboard.update();
        onBackPressed();
    }

    @Override
    public SharedProject.Permission getFinancePermission() {
        return sharedProject.getFinance();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(FinanceActivity.this);
        } else {
            Log.d("HHHH", "The interstitial ad wasn't ready yet.");
        }
    }

    @Override
    public void openCropImageActivity(int width,int height){

        CropImageContractOptions options = new CropImageContractOptions(null, new CropImageOptions())
                .setScaleType(CropImageView.ScaleType.CENTER)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON);

        cropImage.launch(options);

    }

    @Override
    public void onBackPressed() {
        if(mCurrentScene!=dashboardScene){
            if(mCurrentScene==createAccountScene && updateAccount!=null){
                openAccountsScene();
            }else if(mCurrentScene==createTransactionScene && updateTransaction!=null){
                openTransactionsScene();
            }else if(mCurrentScene==zoomImageScene){
                openTransactionsScene();
            }else if(mCurrentScene==accountDetailScene){
                if(detailAccountOpenFrom==FROM_ACCOUNTS_SCENE){
                    openAccountsScene(FADE);
                }else if(detailAccountOpenFrom==FROM_TRIAL_BALANCE_SCENE){
                    openTrialBalanceScene();
                }

            } else{
                backToDashboard();
            }

        }else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home){
            onBackPressed();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}