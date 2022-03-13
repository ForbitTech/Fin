package com.forbitbd.myfin.create_transaction;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.dialog.delete.DeleteDialog;
import com.forbitbd.androidutils.dialog.delete.DialogClickListener;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.Account;
import com.forbitbd.myfin.models.TransactionResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Date;


public class CreateTransactionScene extends Fragment implements CreateTransactionContract.View, View.OnClickListener {

    private CreateTransactionPresenter mPresenter;
    private Communicator communicator;


    private MaterialButton btnBrowse,btnCreateTransaction,btnBack;
    private TextInputLayout tiDate,tiTransactionFrom,tiTransactionTo,tiInvoice,tiPurpose,tiAmount;
    private TextInputEditText etDate,etInvoice,etPurpose,etAmount;
    private AutoCompleteTextView etTransactionFrom,etTransactionTo;
    private ImageView ivImage;
    private TextView tvTitle;

    private Collection<Account> fromAccounts,toAccounts;

    private Date date;

    ArrayAdapter<Account> fromAdapter,toAdapter;

    private Bitmap bitmap;





    public CreateTransactionScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CreateTransactionPresenter(this);
        communicator= (Communicator) getActivity();

        fromAccounts = Collections2.filter(communicator.getDashboard().getAccounts(), new Predicate<Account>() {
            @Override
            public boolean apply(@NullableDecl Account input) {
                if(input.getType()!=0){
                    return true;
                }
                return false;
            }
        });

        toAccounts = Collections2.filter(communicator.getDashboard().getAccounts(), new Predicate<Account>() {
            @Override
            public boolean apply(@NullableDecl Account input) {
                if(input.getType()!=1){
                    return true;
                }
                return false;
            }
        });

        date = new Date();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_transaction_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btnBack = view.findViewById(R.id.btn_back);
        btnCreateTransaction = view.findViewById(R.id.btn_create_transactions);
        btnBrowse = view.findViewById(R.id.btn_browse);

        tiDate = view.findViewById(R.id.ti_date);
        tiTransactionFrom = view.findViewById(R.id.ti_transaction_from);
        tiTransactionTo = view.findViewById(R.id.ti_transaction_to);
        tiInvoice = view.findViewById(R.id.ti_invoice);
        tiPurpose = view.findViewById(R.id.ti_purpose);
        tiAmount = view.findViewById(R.id.ti_amount);

        etDate = view.findViewById(R.id.et_date);
        etTransactionFrom = view.findViewById(R.id.et_transaction_from);
        etTransactionTo = view.findViewById(R.id.et_transaction_to);
        etInvoice = view.findViewById(R.id.et_invoice);
        etPurpose = view.findViewById(R.id.et_purpose);
        etAmount = view.findViewById(R.id.et_amount);
        ivImage = view.findViewById(R.id.image);
        tvTitle = view.findViewById(R.id.title);

        fromAdapter = new ArrayAdapter<Account>(getContext(),android.R.layout.simple_list_item_1);
        toAdapter = new ArrayAdapter<Account>(getContext(),android.R.layout.simple_list_item_1);
        etTransactionFrom.setAdapter(fromAdapter);
        etTransactionTo.setAdapter(toAdapter);

//        mPresenter.loadSpinner();

        for (Account x:fromAccounts){
            fromAdapter.add(x);
        }

        for (Account x:toAccounts){
            toAdapter.add(x);
        }

        etDate.setText(MyUtil.getStringDate(date));

        btnBack.setOnClickListener(this);
        btnCreateTransaction.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        etDate.setOnClickListener(this);


        if(communicator.getUpdateTransaction()!=null){
            mPresenter.updateUI(communicator.getUpdateTransaction());
        }

        btnCreateTransaction.setEnabled(communicator.getFinancePermission().isUpdate());
//        btnBrowse.setEnabled(communicator.getFinancePermission().isWrite());
        btnBack.setEnabled(communicator.getFinancePermission().isDelete());

    }

    @Override
    public void clearPreError() {
        tiDate.setErrorEnabled(false);
        tiTransactionFrom.setErrorEnabled(false);
        tiTransactionTo.setErrorEnabled(false);
        tiInvoice.setErrorEnabled(false);
        tiPurpose.setErrorEnabled(false);
        tiAmount.setErrorEnabled(false);
    }

    @Override
    public void showValidationError(String message, int fieldId) {
        switch (fieldId){
            case 1:
                tiDate.setError(message);
                break;

            case 2:
                tiTransactionFrom.setError(message);
                break;

            case 3:
                tiTransactionTo.setError(message);
                break;
            case 4:
                tiTransactionFrom.setError(message);
                tiTransactionTo.setError(message);
                break;

            case 5:
                tiPurpose.setError(message);
                etPurpose.requestFocus();
                break;

            case 6:
                tiAmount.setError(message);
                etAmount.requestFocus();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnBack){
            if(communicator.getUpdateTransaction()==null){
                communicator.backToDashboard();
            }else{
                // When Button in delete mode
                mPresenter.showDeleteDialog();
            }

        }else if(v==etDate){
            mPresenter.openCalendar();
        }else if(v==btnBrowse){
            communicator.openCropImageActivity(9,16);
        }else if(v==btnCreateTransaction){
            communicator.hideKeyboard();
            TransactionResponse transaction = mPresenter.getTransactionFromFormData();
            boolean valid =mPresenter.validate(transaction);

            if(!valid){
                return;
            }

            if(communicator.getUpdateTransaction()!=null){
                mPresenter.updateTransaction(transaction,getByteArray());
            }else {
                mPresenter.saveTransaction(transaction,getByteArray());
            }


        }
    }

    private byte[] getByteArray() {
        if(bitmap!=null){
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
            byte[] bytes = bos.toByteArray();
            return bytes;
        }

        return null;
    }



    @Override
    public void onResume() {
        super.onResume();
        // get Data From Activity
        if(communicator.getTransactionBitmap()!=null){
            this.bitmap = communicator.getTransactionBitmap();
            ivImage.setImageBitmap(communicator.getTransactionBitmap());
        }
    }

    @Override
    public void openCalendar() {

        MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE,getString(R.string.select_transaction_date));
        myDateDialog.setArguments(bundle);
        myDateDialog.setCancelable(false);
        myDateDialog.setDatePickerListener(new DatePickerListener() {
            @Override
            public void onDatePick(long time) {
                date = new Date(time);
                if (communicator.getUpdateTransaction()!=null){
                    communicator.getUpdateTransaction().setDate(date);
                }
                etDate.setText(MyUtil.getStringDate(date));
            }
        });
        myDateDialog.show(getChildFragmentManager(),"FFFF");

    }

    @Override
    public void showErrorToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public TransactionResponse getTransactionFromFormData() {
        TransactionResponse transaction;
        if(communicator.getUpdateTransaction()==null){
            transaction = new TransactionResponse();
            transaction.setDate(date);
        }else {
            transaction = communicator.getUpdateTransaction();
        }
        transaction.setProject(communicator.getProject().get_id());
        transaction.setInvoice_no(etInvoice.getText().toString().trim());
        transaction.setPurpose(etPurpose.getText().toString().trim());




        try {
            if(transaction.getFrom()!=null){
                transaction.getFrom().set_id(getFromId(etTransactionFrom.getText().toString()));
            }else {
                Account account = new Account();
                account.set_id(getFromId(etTransactionFrom.getText().toString()));
                transaction.setFrom(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if(transaction.getTo()!=null){
                transaction.getTo().set_id(getToId(etTransactionTo.getText().toString()));
            }else {
                Account account = new Account();
                account.set_id(getToId(etTransactionTo.getText().toString()));
                transaction.setTo(account);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            transaction.setAmount(Double.parseDouble(etAmount.getText().toString().trim()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return transaction;
    }

    @Override
    public void saveComplete(TransactionResponse transaction) {
        communicator.addTransactionToDashboard(transaction);
    }

    @Override
    public void updateUI(TransactionResponse transaction) {
        tvTitle.setText(getString(R.string.update_transaction));

        etDate.setText(MyUtil.getStringDate(transaction.getDate()));
        etTransactionFrom.setText(transaction.getFrom().getName());
        etTransactionTo.setText(transaction.getTo().getName());
        etInvoice.setText(transaction.getInvoice_no());
        etPurpose.setText(transaction.getPurpose());
        etAmount.setText(String.valueOf(transaction.getAmount()));

        if(transaction.getImage()!=null && !transaction.getImage().equals("")){
            Picasso.get().load(transaction.getImage()).into(ivImage);
        }

        btnCreateTransaction.setText(getString(R.string.update));
        btnBack.setText(getString(R.string.delete));



    }

    @Override
    public void updateTransactionInDashboard(TransactionResponse transaction) {
        communicator.updateTransactionInDashboard(transaction);
    }


    @Override
    public void showDeleteDialog() {
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CONTENT,"Do you really want to delete this transaction??");
        deleteDialog.setArguments(bundle);
        deleteDialog.setListener(new DialogClickListener() {
            @Override
            public void positiveButtonClick() {
                deleteDialog.dismiss();
                mPresenter.deleteTransaction(communicator.getUpdateTransaction());
            }
        });
        deleteDialog.show(getChildFragmentManager(),"HJHHJHJ");
    }

    @Override
    public void deleteFromDashboard() {

        communicator.deleteTransactionFromDashboard();
    }

    private String getFromId(String name){
        for(Account x:fromAccounts){
            if(x.getName().equals(name)){
                return x.get_id();
            }
        }
        return null;
    }

    private String getToId(String name){
        for(Account x:toAccounts){
            if(x.getName().equals(name)){
                return x.get_id();
            }
        }
        return null;
    }
}