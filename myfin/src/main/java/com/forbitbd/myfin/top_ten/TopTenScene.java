package com.forbitbd.myfin.top_ten;

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

public class TopTenScene extends Fragment implements TransactionAdapter.TransactionListener{

    private Communicator communicator;
    private TransactionAdapter adapter;


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
        View view = inflater.inflate(R.layout.fragment_top_ten_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.btn_top_ten);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.addAll(communicator.getDashboard().getTopTen());
    }

    @Override
    public void onItemClick(TransactionResponse transactionResponse) {

    }

    @Override
    public void onAttachmentClick(TransactionResponse transactionResponse) {

    }
}
