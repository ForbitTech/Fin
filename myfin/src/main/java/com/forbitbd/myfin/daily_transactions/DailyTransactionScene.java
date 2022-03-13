package com.forbitbd.myfin.daily_transactions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forbitbd.androidutils.dialog.DatePickerListener;
import com.forbitbd.androidutils.dialog.MyDatePickerFragment;
import com.forbitbd.androidutils.utils.Constant;
import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myfin.Communicator;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TransactionResponse;
import com.forbitbd.myfin.transactions.TransactionAdapter;

import java.util.Date;


public class DailyTransactionScene extends Fragment implements TransactionAdapter.TransactionListener , View.OnClickListener {

    private TextView tvDate;
    private Date date;


    private TransactionAdapter adapter;
    private Communicator communicator;



    public DailyTransactionScene() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        communicator = (Communicator) getActivity();
        date = new Date();
        adapter = new TransactionAdapter(this,R.layout.item_plain_transaction);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_transaction_scene, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvDate = view.findViewById(R.id.date);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        ImageView ivCalendar = view.findViewById(R.id.fab_calender);
        ivCalendar.setOnClickListener(this);

        tvDate.setText("Transaction On ".concat(MyUtil.getStringDate(date)));
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
        adapter.addAll(communicator.getDashboard().getDailyTransactions(date));
    }

    @Override
    public void onClick(View v) {
        openCalender();
    }

    private void openCalender() {

        MyDatePickerFragment myDateDialog = new MyDatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE,getString(R.string.select_transaction_date));
        myDateDialog.setArguments(bundle);
        myDateDialog.setCancelable(false);
        myDateDialog.setDatePickerListener(new DatePickerListener() {
            @Override
            public void onDatePick(long time) {
                date = new Date(time);
                tvDate.setText("Transaction On ".concat(MyUtil.getStringDate(date)));
                adapter.addAll(communicator.getDashboard().getDailyTransactions(date));
            }
        });
        myDateDialog.show(getChildFragmentManager(),"FFFF");

    }
}