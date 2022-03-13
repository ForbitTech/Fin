package com.forbitbd.myfin.trial_balance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TrialBalance;

import java.util.ArrayList;
import java.util.List;

public class TrialBalanceAdapter extends RecyclerView.Adapter<TrialBalanceAdapter.TrialBalanceHolder> {

    private List<TrialBalance> trialBalanceList;
    private TrialBalanceClickListener listener;
    private int layoutId;

    public TrialBalanceAdapter(TrialBalanceClickListener listener, int layoutId) {
        this.listener = listener;
        this.layoutId = layoutId;
        this.trialBalanceList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrialBalanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new TrialBalanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrialBalanceHolder holder, int position) {
        TrialBalance trialBalance = trialBalanceList.get(position);
        holder.bind(trialBalance);

    }

    @Override
    public int getItemCount() {
        return trialBalanceList.size();
    }

    public void add(TrialBalance trialBalance){
        trialBalanceList.add(trialBalance);
        int pos = trialBalanceList.indexOf(trialBalance);
        notifyItemInserted(pos);
    }

    public void clear(){
        this.trialBalanceList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<TrialBalance> trialBalanceList){
        this.trialBalanceList = trialBalanceList;
        notifyDataSetChanged();
    }

    class TrialBalanceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvAccountName,tvDebit,tvCredit;

        public TrialBalanceHolder(@NonNull View itemView) {
            super(itemView);
            tvAccountName = itemView.findViewById(R.id.name);
            tvDebit = itemView.findViewById(R.id.debit);
            tvCredit = itemView.findViewById(R.id.credit);
            itemView.setOnClickListener(this);
        }


        public void bind(TrialBalance trialBalance){
            tvAccountName.setText(trialBalance.getAccount().getName());
            tvDebit.setText(String.valueOf(trialBalance.getDebit()));
            tvCredit.setText(String.valueOf(trialBalance.getCredit()));
        }

        @Override
        public void onClick(View v) {
            listener.onTrialBalanceClick(trialBalanceList.get(getAdapterPosition()));
        }
    }


    public interface TrialBalanceClickListener{
        void onTrialBalanceClick(TrialBalance trialBalance);
    }


}
