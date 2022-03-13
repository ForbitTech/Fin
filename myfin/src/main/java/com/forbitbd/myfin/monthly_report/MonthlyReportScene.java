package com.forbitbd.myfin.monthly_report;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.Calendar;


public class MonthlyReportScene extends Fragment implements View.OnClickListener {
    
    private Communicator communicator;

    private TextView tvStatus;
    private TextView tvPrev,tvNext;
    private BottomNavigationView bottomNavigationView;

    private int currentMonth,currentYear;

    

    public MonthlyReportScene() {
        // Required empty public constructor
    }

   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = (Communicator) getActivity();
        this.currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_report_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvPrev = view.findViewById(R.id.prev);
        tvNext = view.findViewById(R.id.next);
        tvStatus = view.findViewById(R.id.status);
        tvStatus.setText(getStringDate());


        tvPrev.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.summery){
                    loadFragment(new SummeryFragment());
                    return true;
                }else if(id==R.id.transactions){
                    loadFragment(new AllFragment());
                    return true;
                }
                return false;
            }
        });

        loadFragment(new SummeryFragment());
    }

    public void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.TRANSACTION, (Serializable) communicator.getDashboard().getTransactions());
        bundle.putInt("YEAR",currentYear);
        bundle.putInt("MONTH",currentMonth);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.monthly_container, fragment, "CURRENT_TRANSACTION");
        transaction.commit();
    }

    private String getStringDate(){
        String month = getResources().getStringArray(R.array.month_array)[currentMonth];
        return month+" - "+currentYear;
    }

    private void increase(){
        currentMonth++;
        if(currentMonth>11){
            currentYear++;
            currentMonth= currentMonth%12;
        }
    }

    private void decrease(){
        currentMonth--;
        if(currentMonth<0){
            currentYear--;
            currentMonth= currentMonth+12;
        }
    }

    @Override
    public void onClick(View v) {
        if(v==tvPrev){
            decrease();
        }else if(v==tvNext){
            increase();
        }
        update();
    }


    private void update(){
        if(getChildFragmentManager().findFragmentByTag("CURRENT_TRANSACTION") instanceof AllFragment){
            AllFragment af = (AllFragment) getChildFragmentManager().findFragmentByTag("CURRENT_TRANSACTION");
            af.update(currentYear,currentMonth);
        }else if(getChildFragmentManager().findFragmentByTag("CURRENT_TRANSACTION") instanceof SummeryFragment){
            SummeryFragment allF = (SummeryFragment) getChildFragmentManager().findFragmentByTag("CURRENT_TRANSACTION");
            allF.update(currentYear,currentMonth);
        }
        tvStatus.setText(getStringDate());
    }

    @Override
    public void onResume() {
        super.onResume();
        communicator.setTitle(communicator.getProject().getName().concat(" ").concat(getString(R.string.monthly_report)));
    }
}