package com.forbitbd.myfin.create_account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.Account;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;


public class CreateAccountScene extends Fragment implements View.OnClickListener, CreateAccountContract.View {


    private Communicator communicator;
    private CreateAccountPresenter mPresenter;

    private AutoCompleteTextView etAccountType;
    private MaterialButton btnBack,btnCreate;
    private TextInputLayout tiAccountName,tiAccountType,tiOpeningBalance;
    private TextInputEditText etAccountName,etOpeningBalance;

    private TextView tvTitle;





    private static final String[] ACCOUNT_TYPE = new String[] {
            "Expenses", "Income","None"
    };




    public CreateAccountScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new CreateAccountPresenter(this);
        communicator= (Communicator) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account_scene, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.title);
        tiAccountName = view.findViewById(R.id.ti_account_name);
        tiAccountType = view.findViewById(R.id.ti_account_type);
        tiOpeningBalance = view.findViewById(R.id.ti_opening_balance);

        etAccountName = view.findViewById(R.id.et_account_name);
        etAccountType = view.findViewById(R.id.et_account_type);
        etOpeningBalance = view.findViewById(R.id.et_opening_balance);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,ACCOUNT_TYPE);
        etAccountType.setAdapter(typeAdapter);
        etAccountType.setText(ACCOUNT_TYPE[2],false);

        btnBack = view.findViewById(R.id.btn_back);
        btnCreate = view.findViewById(R.id.btn_create_account);

        btnBack.setOnClickListener(this);
        btnCreate.setOnClickListener(this);

        if(communicator.getUpdateAccount()!=null){
            mPresenter.updateUI(communicator.getUpdateAccount());
        }

    }

    @Override
    public void onClick(View v) {
        if(v==btnBack){
            if(communicator.getUpdateAccount()!=null){
                communicator.openAccountsScene();
            }else{
                mPresenter.backToDashboard();
            }

        }else if(v==btnCreate){
            communicator.hideKeyboard();
            Account account = mPresenter.createAccountFromData();
            boolean valid = mPresenter.validate(account);

            if(!valid){
                return;
            }

            if(communicator.getUpdateAccount()!=null){
                mPresenter.updateAccount(account);
            }else {
                mPresenter.saveAccount(account);
            }
        }
    }

    @Override
    public void clearPreError() {
        tiAccountName.setErrorEnabled(false);
        tiAccountType.setErrorEnabled(false);
        tiOpeningBalance.setErrorEnabled(false);
    }

    @Override
    public void showValidationError(String message, int fieldId) {
        if(fieldId==1){
            tiAccountName.setError(message);
            etAccountName.requestFocus();
        }
    }

    @Override
    public void backToDashboard() {
        communicator.backToDashboard();
    }

    @Override
    public void showToast(String message) {
       communicator.showToast(message);
    }

    @Override
    public void updateUI(Account account) {
        tvTitle.setText(getString(R.string.update_account));
        btnCreate.setText(getString(R.string.update));
        etAccountName.setText(account.getName());
        etAccountType.setText(ACCOUNT_TYPE[account.getType()],false);
        etOpeningBalance.setText(String.valueOf(account.getOpening_balance()));
        etOpeningBalance.setEnabled(false);

    }

    @Override
    public void addAccountToDashboard(Account account) {
        communicator.addAccountToDashboard(account);
    }

    @Override
    public void updateAccountToDashboard(Account account) {
        communicator.updateAccountToDashboard(account);
    }

    @Override
    public Account createAccountFromData() {
        Account account;
        if(communicator.getUpdateAccount()==null){
            account = new Account();
        }else {
            account = communicator.getUpdateAccount();
        }
        account.setProject(communicator.getProject().get_id());
        account.setName(etAccountName.getText().toString().trim());
        account.setType(Arrays.asList(ACCOUNT_TYPE).indexOf(etAccountType.getText().toString()));
        Log.d("HHHHH",account.getType()+"");
        try {
            account.setOpening_balance(Double.parseDouble(etOpeningBalance.getText().toString()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return account;
    }
}