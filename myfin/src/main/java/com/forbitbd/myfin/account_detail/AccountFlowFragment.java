package com.forbitbd.myfin.account_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.forbitbd.androidutils.customView.MadView;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.Account;
import com.forbitbd.myfin.models.TransactionResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AccountFlowFragment extends Fragment implements MadView.MyListener {

    private MadView madView;

    private List<Account> accountList;

    private TextView tvRefToAccName,tvRefToAccValue,tvAccToRefName,tvAccToRefValue;

    private Account refAccount;

    private int selectedPosition=0;
    private List<TransactionResponse> transactionList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_flow, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //tvRefToAccName,tvRefToAccValue,tvAccToRefName,tvAccToRefValue
        madView = view.findViewById(R.id.madView);
        tvRefToAccName = view.findViewById(R.id.ref_to_acc_name);
        tvRefToAccValue = view.findViewById(R.id.ref_to_acc_value);
        tvAccToRefName = view.findViewById(R.id.acc_to_ref_name);
        tvAccToRefValue = view.findViewById(R.id.acc_to_ref_value);
        madView.setMyListener(this);
    }


    private boolean exist(Account account){
        for (Account x:accountList){
            if(x.get_id().equals(account.get_id())){
                return true;
            }
        }

        return false;
    }

    public void render(Account account, List<TransactionResponse> transactionList){
        madView.setRefrence(account.getName());
        this.refAccount = account;
        this.transactionList = transactionList;

        for (TransactionResponse x:transactionList){
            if(!x.getTo().get_id().equals(account.get_id())){
                if(!exist(x.getTo())){
                    accountList.add(x.getTo());
                }

            }

            if(!x.getFrom().get_id().equals(account.get_id())){
                if(!exist(x.getFrom())){
                    accountList.add(x.getFrom());
                }

            }


        }

        List<String> uu = new ArrayList<>();
        for (Account x: accountList){
            uu.add(x.getName());
        }

        madView.setStringList(uu);

        updateUI();
    }

    private void updateUI(){
        Account selectedAccount = accountList.get(selectedPosition);
        if(selectedAccount !=null){
            tvRefToAccName.setText(refAccount.getName()+" to "+selectedAccount.getName());
            tvAccToRefName.setText(selectedAccount.getName()+" to "+refAccount.getName());
            Map<String, String> values = getValues(selectedAccount);
            tvRefToAccValue.setText(values.get("refToAcc"));
            tvAccToRefValue.setText(values.get("accToRef"));
        }
    }

    private Map<String, String> getValues(Account account){
        Map<String, String> retMap = new HashMap<>();
        double refToAcc=0;
        double accToRef=0;

        for (TransactionResponse x:transactionList){
            if(x.getFrom().get_id().equals(refAccount.get_id()) && x.getTo().get_id().equals(account.get_id())){
                refToAcc= refToAcc+x.getAmount();
            }

            if(x.getTo().get_id().equals(refAccount.get_id()) && x.getFrom().get_id().equals(account.get_id())){
                accToRef= accToRef+x.getAmount();
            }
        }
        retMap.put("refToAcc", String.valueOf(refToAcc));
        retMap.put("accToRef", String.valueOf(accToRef));

        return retMap;
    }

    @Override
    public void itemTouch(int position) {
        this.selectedPosition = position;
        updateUI();
    }
}