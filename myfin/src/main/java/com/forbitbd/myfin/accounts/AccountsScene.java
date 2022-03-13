package com.forbitbd.myfin.accounts;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.forbitbd.androidutils.dialog.delete.DeleteDialog;
import com.forbitbd.androidutils.dialog.delete.DialogClickListener;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.Account;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AccountsScene extends Fragment implements AccountsSceneContract.View ,AccountAdapter.AccountClickListener, SearchView.OnQueryTextListener {

    private AccountsScenePresenter mPresenter;
    private Communicator communicator;

    private SearchView mSearchView;

    private AccountAdapter adapter;



    public AccountsScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new AccountsScenePresenter(this);
        communicator = (Communicator) getActivity();
        adapter = new AccountAdapter(this,communicator.getFinancePermission());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_accounts_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        mSearchView = view.findViewById(R.id.btn_accounts);
//        ImageView iconClose = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
//        iconClose.setImageResource(R.drawable.ic_outline_search_24);
        ImageView searchIcon = (ImageView)mSearchView.findViewById(R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_outline_search_24);

        mSearchView.setOnQueryTextListener(this);









    }

    @Override
    public void onStart() {
        super.onStart();

        List<Account> accounts = communicator.getDashboard().getAccounts();

        Collections.sort(accounts, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

        for(Account x:accounts){
            adapter.addAccount(x);
        }


    }

    @Override
    public void startAccountDetailScene(Account account) {
        communicator.openAccountDetailScene(account,1);
    }

    @Override
    public void editAccountRequest(Account account) {
        communicator.openCreateAccountScene(account);
    }

    @Override
    public void showDeleteDialog(final Account account) {
        final DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setCancelable(false);
        deleteDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                deleteDialog.dismiss();
                mPresenter.deleteAccountFromDatabase(account);
            }
        });


        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,"Delete Account Also Remove All Related Transactions??");
        deleteDialog.setArguments(bundle);
        deleteDialog.show(getChildFragmentManager(),"DELETE");

    }

    @Override
    public void removeAccountFromAdapter(Account account) {
        adapter.removeAccount(account);
        communicator.removeAccountFromDashboard(account);
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