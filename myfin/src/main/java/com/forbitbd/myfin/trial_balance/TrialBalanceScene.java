package com.forbitbd.myfin.trial_balance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TrialBalance;


public class TrialBalanceScene extends Fragment implements TrialBalanceAdapter.TrialBalanceClickListener{
    
    private Communicator communicator;
    private TrialBalanceAdapter adapter;

   

    public TrialBalanceScene() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.communicator = (Communicator) getActivity();
        adapter = new TrialBalanceAdapter(this,R.layout.item_trial_balance);
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

    @Override
    public void onResume() {
        super.onResume();
//        adapter.clear();
        communicator.setTitle(communicator.getProject().getName().concat(" ").concat(getString(R.string.trial_balance)));
        adapter.addAll(communicator.getDashboard().getTrialBalances());
    }

    @Override
    public void onTrialBalanceClick(TrialBalance trialBalance) {
        communicator.openAccountDetailScene(trialBalance.getAccount(),2);
    }
}