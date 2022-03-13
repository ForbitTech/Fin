package com.forbitbd.myfin.monthly_report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TrialBalance;
import com.forbitbd.myfin.trial_balance.TrialBalanceAdapter;


public class SummeryFragment extends Fragment implements TrialBalanceAdapter.TrialBalanceClickListener {


    private TrialBalanceAdapter adapter;

    private int year,month;

    private Communicator communicator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = getArguments().getInt("YEAR");
        month = getArguments().getInt("MONTH");
        communicator = (Communicator) getActivity();
        this.adapter = new TrialBalanceAdapter(this, R.layout.item_trial_balance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trial_balance_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void update(int year,int month){
        adapter.addAll(communicator.getDashboard().getMonthlyTrialBalance(year,month));
    }

    @Override
    public void onResume() {
        super.onResume();

        if(adapter.getItemCount()==0){
            adapter.addAll(communicator.getDashboard().getMonthlyTrialBalance(year,month));
        }
    }

    @Override
    public void onTrialBalanceClick(TrialBalance trialBalance) {

    }
}
