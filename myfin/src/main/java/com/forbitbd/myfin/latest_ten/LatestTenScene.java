package com.forbitbd.myfin.latest_ten;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TransactionResponse;
import com.forbitbd.myfin.transactions.TransactionAdapter;


public class LatestTenScene extends Fragment implements TransactionAdapter.TransactionListener{

    private TransactionAdapter adapter;
    private Communicator communicator;



    public LatestTenScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new TransactionAdapter(this,R.layout.item_plain_transaction);
        communicator = (Communicator) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_latest_ten_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.btn_latest_ten);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.addAll(communicator.getDashboard().getLatestTen());
    }

    @Override
    public void onItemClick(TransactionResponse transactionResponse) {

    }

    @Override
    public void onAttachmentClick(TransactionResponse transactionResponse) {

    }
}