package com.forbitbd.myfin.transactions;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TransactionResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TransactionsScene extends Fragment implements TransactionsSceneContract.View, TransactionAdapter.TransactionListener , SearchView.OnQueryTextListener{

    private TransactionsScenePresenter mPresenter;
    private Communicator communicator;

    private TransactionAdapter adapter;

    RecyclerView recyclerView;
    private SearchView mSearchView;



    public TransactionsScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new TransactionAdapter(this,R.layout.item_new_transaction);

        mPresenter = new TransactionsScenePresenter(this);
        communicator = (Communicator) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions_scene, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        communicator.setTitle(communicator.getProject().getName().concat(" ").concat(getString(R.string.transactions)));
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        mSearchView = view.findViewById(R.id.btn_transactions);
//        ImageView iconClose = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
//        iconClose.setImageResource(R.drawable.ic_outline_search_24);
        ImageView searchIcon = (ImageView)mSearchView.findViewById(R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_outline_search_24);

        mSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        List<TransactionResponse> transactionResponseList = communicator.getDashboard().getTransactions();

        Collections.sort(transactionResponseList, new Comparator<TransactionResponse>() {
            @Override
            public int compare(TransactionResponse o1, TransactionResponse o2) {
                return (int) (o2.getDate().getTime()-o1.getDate().getTime());
            }
        });
        adapter.addAll(transactionResponseList);

    }


    @Override
    public void onItemClick(TransactionResponse transactionResponse) {
        communicator.openCreateTransactionScene(transactionResponse);
    }

    @Override
    public void onAttachmentClick(TransactionResponse transactionResponse) {
        if(transactionResponse.getImage()!=null && !transactionResponse.getImage().equals("")){
            communicator.openZoomImageScene(transactionResponse.getImage());
        }else {
            communicator.showToast("Image Not Found");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}