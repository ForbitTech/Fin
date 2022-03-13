package com.forbitbd.myfin.create_transaction;

import android.util.Log;

import com.forbitbd.androidutils.api.ServiceGenerator;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myfin.api.FinanceClient;
import com.forbitbd.myfin.models.Transaction;
import com.forbitbd.myfin.models.TransactionResponse;
import com.google.android.gms.common.config.GservicesValue;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTransactionPresenter implements CreateTransactionContract.Presenter {

    private CreateTransactionContract.View mView;

    public CreateTransactionPresenter(CreateTransactionContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void openCalendar() {
        mView.openCalendar();
    }

    @Override
    public TransactionResponse getTransactionFromFormData() {
        return mView.getTransactionFromFormData();
    }

    @Override
    public boolean validate(TransactionResponse transaction) {
        mView.clearPreError();

        if(transaction.getDate()==null){
            mView.showValidationError("Please Select a Transaction Date",1);
            return false;
        }

        if( transaction.getFrom().get_id()==null || transaction.getFrom().get_id().equals("")){
            mView.showValidationError("Please Select a Account from Dropdown",2);
            return false;
        }

        if(transaction.getTo().get_id()==null || transaction.getTo().get_id().equals("")){
            mView.showValidationError("Please Select a Account from Dropdown",3);
            return false;
        }



        if(transaction.getFrom().get_id().equals(transaction.getTo().get_id())){
            mView.showValidationError("Transaction not possible between same account",4);
            return false;
        }

        if(transaction.getPurpose().equals("")){
            mView.showValidationError("Please describe transaction purpose",5);
            return false;
        }

        if(transaction.getAmount()<=0){
            mView.showValidationError("Transaction Amount Should be a Positive Value",6);
            return false;
        }

        return true;
    }

    @Override
    public void updateUI(TransactionResponse transaction) {
        mView.updateUI(transaction);
    }

    @Override
    public void updateTransaction(TransactionResponse transaction,byte[] bytes) {
        MultipartBody.Part part = preparePart(bytes);
        HashMap<String, RequestBody> map = getMap(transaction);

        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);
        client.updateTransaction(transaction.getProject(),transaction.get_id(),part,map)
                .enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                        Log.d("HHHHH","Transaction Updated");
                        mView.updateTransactionInDashboard(response.body());
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        mView.showErrorToast("Server Error");
                    }
                });


    }


    private MultipartBody.Part preparePart(byte[] bytes){
        MultipartBody.Part part=null;

        if(bytes!=null){
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
            part = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        }
        return part;
    }

    private HashMap<String, RequestBody> getMap(TransactionResponse transaction){


        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), MyUtil.getStringDate(transaction.getDate()));
        RequestBody from = RequestBody.create(MediaType.parse("text/plain"), transaction.getFrom().get_id());
        RequestBody to = RequestBody.create(MediaType.parse("text/plain"), transaction.getTo().get_id());
        RequestBody invoice_no = RequestBody.create(MediaType.parse("text/plain"), transaction.getInvoice_no());
        RequestBody purpose = RequestBody.create(MediaType.parse("text/plain"), transaction.getPurpose());
        RequestBody project = RequestBody.create(MediaType.parse("text/plain"), transaction.getProject());
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transaction.getAmount()));

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("date", date);
        map.put("from", from);
        map.put("to", to);
        map.put("invoice_no", invoice_no);
        map.put("purpose", purpose);
        map.put("project", project);
        map.put("amount", amount);

        return map;
    }

    @Override
    public void saveTransaction(TransactionResponse transaction, byte[] bytes) {
        if(bytes==null){
            Log.d("HHHHH","Byte Null");
        }else{
            Log.d("HHHHH","Byte Not Null");
        }

        HashMap<String, RequestBody> map = getMap(transaction);
        MultipartBody.Part part=preparePart(bytes);
//
//        if(bytes!=null){
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
//            part = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
//        }
//
//        RequestBody date = RequestBody.create(MediaType.parse("text/plain"), MyUtil.getStringDate(transaction.getDate()));
//        RequestBody from = RequestBody.create(MediaType.parse("text/plain"), transaction.getFrom().get_id());
//        RequestBody to = RequestBody.create(MediaType.parse("text/plain"), transaction.getTo().get_id());
//        RequestBody invoice_no = RequestBody.create(MediaType.parse("text/plain"), transaction.getInvoice_no());
//        RequestBody purpose = RequestBody.create(MediaType.parse("text/plain"), transaction.getPurpose());
//        RequestBody project = RequestBody.create(MediaType.parse("text/plain"), transaction.getProject());
//        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(transaction.getAmount()));
//
//        HashMap<String, RequestBody> map = new HashMap<>();
//        map.put("date", date);
//        map.put("from", from);
//        map.put("to", to);
//        map.put("invoice_no", invoice_no);
//        map.put("purpose", purpose);
//        map.put("project", project);
//        map.put("amount", amount);



        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);

        client.saveTransaction(transaction.getProject(),part,map)
                .enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                        if(response.isSuccessful()){
                            mView.showErrorToast("Done");
                            mView.saveComplete(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        mView.showErrorToast("Failed to save transaction");
                    }
                });
    }

    @Override
    public void showDeleteDialog() {
        mView.showDeleteDialog();
    }

    @Override
    public void deleteTransaction(TransactionResponse transactionResponse) {
        FinanceClient client = ServiceGenerator.createService(FinanceClient.class);
        client.deleteTransaction(transactionResponse.getProject(),transactionResponse.get_id())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            Log.d("HHHHH"," "+"Success");
                            mView.deleteFromDashboard();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d("HHHHH"," "+t.getMessage());
                    }
                });
    }
}
