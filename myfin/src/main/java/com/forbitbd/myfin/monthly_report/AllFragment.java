package com.forbitbd.myfin.monthly_report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TransactionResponse;
import com.forbitbd.myfin.transactions.TransactionAdapter;


public class AllFragment extends Fragment implements TransactionAdapter.TransactionListener{

    private int year,month;
    private TransactionAdapter adapter;
    private Communicator communicator;



    public AllFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        year = getArguments().getInt("YEAR");
        month = getArguments().getInt("MONTH");
        communicator = (Communicator) getActivity();
        adapter = new TransactionAdapter(this,R.layout.item_plain_transaction);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_five, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void update(int year,int month){
        adapter.addAll(communicator.getDashboard().getMonthlyTransaction(year,month));
    }

    @Override
    public void onItemClick(TransactionResponse transactionResponse) {

    }

    @Override
    public void onAttachmentClick(TransactionResponse transactionResponse) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter.getItemCount()==0){
            adapter.addAll(communicator.getDashboard().getMonthlyTransaction(year,month));
        }
    }
}