package com.forbitbd.myfin.account_detail;


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
import com.forbitbd.myfin.models.TransactionResponse;
import com.forbitbd.myfin.transactions.TransactionAdapter;


import java.util.List;


public class AccountTransactionFragment extends Fragment
        implements TransactionAdapter.TransactionListener {


    private TransactionAdapter adapter;
    private Communicator communicator;


    public AccountTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        communicator = (Communicator) getActivity();
        adapter = new TransactionAdapter(this,R.layout.item_plain_transaction);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_transaction, container, false);
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
        if(adapter.getItemCount()==0){
            adapter.clear();
            for (TransactionResponse x: communicator.getDashboard().getFilterTransaction(communicator.getDetailAccount())){
                adapter.add(x);
            }
        }

    }

    public void render(List<TransactionResponse> transactionResponseList) {
        adapter.clear();
        for (TransactionResponse x:transactionResponseList){
            adapter.add(x);
        }
    }





    @Override
    public void onItemClick(TransactionResponse transactionResponse) {
        communicator.openTransactionDetailScene(transactionResponse);
    }

    @Override
    public void onAttachmentClick(TransactionResponse transactionResponse) {

    }
}
