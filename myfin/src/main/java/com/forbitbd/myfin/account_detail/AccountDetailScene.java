package com.forbitbd.myfin.account_detail;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.androidutils.utils.ViewPagerAdapter;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.AccountInfo;
import com.forbitbd.myfin.models.TransactionResponse;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class AccountDetailScene extends Fragment implements AccountDetailSceneContract.View , View.OnClickListener,ParentListener {


    private AccountDetailScenePresenter mPresenter;
    private Communicator communicator;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private RelativeLayout mContainer;
    private LinearLayout mHideContainer;
    private ImageView ivIndicator;
    private int height;
    private boolean isExpand;
    private TextView tvTitle,tvTransactionCount,tvDebit,tvCredit,tvBalanceText,tvBalance,tvOpeningBalance;

    private int currentFragment;



    public AccountDetailScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AccountDetailScenePresenter(this);
        communicator = (Communicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_detail_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {


        mContainer = view.findViewById(R.id.top_container);
        mHideContainer = view.findViewById(R.id.hide_container);
        ivIndicator = view.findViewById(R.id.arrow);

        mHideContainer.post(new Runnable() {
            @Override
            public void run() {
                mHideContainer.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                height = mHideContainer.getMeasuredHeight();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHideContainer.getLayoutParams();
                params.height = 1;

                mHideContainer.setLayoutParams(params);
                mHideContainer.setVisibility(View.VISIBLE);
            }
        });

        mContainer.setOnClickListener(this);

        tvTitle = view.findViewById(R.id.info);
        tvTransactionCount = view.findViewById(R.id.transaction_count);
        tvDebit = view.findViewById(R.id.debit);
        tvCredit = view.findViewById(R.id.credit);
        tvBalanceText = view.findViewById(R.id.balance_txt);
        tvBalance = view.findViewById(R.id.balance);
        tvOpeningBalance = view.findViewById(R.id.opening_balance);




        viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = view.findViewById(R.id.tabs);
//        tabLayout.setTabTextColors(R.color.colorAccent, android.R.color.holo_red_light);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFragment = position;
                renderUI();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPresenter.renderAccountInfo(communicator.getDashboard().getAccountInfo(communicator.getDetailAccount()));
    }

    private void renderUI(){
        if(adapter.getItem(currentFragment) instanceof  AccountTransactionFragment){
            AccountTransactionFragment af = (AccountTransactionFragment) adapter.getItem(currentFragment);
            if(currentFragment==0){
                af.render(communicator.getDashboard().getFilterTransaction(communicator.getDetailAccount()));
            }else if(currentFragment==1){
                af.render(communicator.getDashboard().getInTransactions(communicator.getDetailAccount()));
            }else if(currentFragment==2){
                af.render(communicator.getDashboard().getOutTransactions(communicator.getDetailAccount()));
            }

        }else if(adapter.getItem(currentFragment) instanceof  AccountFlowFragment){
            AccountFlowFragment af = (AccountFlowFragment) adapter.getItem(currentFragment);
            af.render(communicator.getDetailAccount(),communicator.getDashboard().getFilterTransaction(communicator.getDetailAccount()));
        }

    }

    @Override
    public void setExitTransition(@Nullable Object transition) {
//        Fade fade = new Fade();
//        fade.setDuration(1000);
//        fade.setInterpolator(new LinearInterpolator());

        super.setExitTransition(transition);
    }

    @Override
    public void onResume() {
        super.onResume();
//        renderUI();
    }

    @Override
    public void onClick(View v) {
        this.height = MyUtil.getFullHeight(mHideContainer);
        animate();
    }

    private void animate(){
        ValueAnimator animator = ValueAnimator.ofFloat(1,height);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(300);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int value;
                int rotation;

                if (isExpand) {
                    value = (int) (height * (1 - valueAnimator.getAnimatedFraction()));
                    rotation = (int) ((1 - valueAnimator.getAnimatedFraction()) * 180);
                } else {
                    value = (int) (height * valueAnimator.getAnimatedFraction());
                    rotation = (int) (180 * valueAnimator.getAnimatedFraction());
                }

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, value);
                mHideContainer.setLayoutParams(params);
                ivIndicator.setRotation(rotation);
                mHideContainer.requestLayout();

                if (valueAnimator.getAnimatedFraction() == 1) {
                    isExpand = !isExpand;
                }

            }
        });

        animator.start();



    }

    private void setupViewPager(ViewPager viewPager) {
        if(adapter==null){
            adapter = new ViewPagerAdapter(getChildFragmentManager());
        }
        adapter.addFragment(new AccountTransactionFragment(), "ALL");
        adapter.addFragment(new AccountTransactionFragment(), "IN");
        adapter.addFragment(new AccountTransactionFragment(), "OUT");
        adapter.addFragment(new AccountFlowFragment(),"FLOW");
        //adapter.addFragment(new ThreeFragment(), "THREE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void renderAccountInfo(AccountInfo info) {
        tvTitle.setText("General Info of ".concat(info.getName()));
        tvTransactionCount.setText(String.valueOf(info.getCount()));
        tvOpeningBalance.setText(String.valueOf(info.getOpening_balance()));
        tvDebit.setText(String.valueOf(info.getDebit()));
        tvCredit.setText(String.valueOf(info.getCredit()));
        tvBalance.setText(String.valueOf(info.getBalance()));
        tvBalanceText.setText(info.getBalance_text());
    }

    @Override
    public List<TransactionResponse> getTransaction() {
        return null;
    }
}