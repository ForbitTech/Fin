package com.forbitbd.myfin.transactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.myfin.R;
import com.forbitbd.myfin.models.TransactionResponse;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> implements Filterable {


    private List<TransactionResponse> transactionResponseList;
    private List<TransactionResponse> originalList;
    private int layout;
    private TransactionListener listener;

    public TransactionAdapter(TransactionListener listener,int layout) {
        this.layout = layout;
        this.listener = listener;
        this.transactionResponseList = new ArrayList<>();
    }

    @Override
    public Filter getFilter() {
        if(originalList==null){
            this.originalList = transactionResponseList;
        }
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                List<TransactionResponse> filteredList = new ArrayList<>();

                if(charString.isEmpty()){
                    filteredList = originalList;
                }else{
                    List<TransactionResponse> tmpList = new ArrayList<>();
                    for (TransactionResponse x: originalList){
                        if(
                                x.getPurpose().toLowerCase().contains(charString.toLowerCase())
                                        || x.getInvoice_no().toLowerCase().contains(charString.toLowerCase())
                                        || x.getTo().getName().toLowerCase().contains(charString.toLowerCase())
                                        || x.getFrom().getName().toLowerCase().contains(charString.toLowerCase())
                        ){
                            tmpList.add(x);
                        }
                    }

                    filteredList = tmpList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values=filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                transactionResponseList = (ArrayList<TransactionResponse>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        TransactionResponse transactionResponse = transactionResponseList.get(position);
        holder.bind(transactionResponse);

    }

    public void addAll(List<TransactionResponse> transactionResponseList){
        this.transactionResponseList = transactionResponseList;
        notifyDataSetChanged();
    }

    public void add(TransactionResponse transactionResponse){
        transactionResponseList.add(transactionResponse);
        int position = transactionResponseList.indexOf(transactionResponse);
        notifyItemInserted(position);
    }

    public void clear(){
        this.transactionResponseList.clear();
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return transactionResponseList.size();
    }

    class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate,tvAccount,tvPurpose,tvAmount,tvMonthYear;
        ImageView ivAttach;

        public TransactionHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.date);
            tvMonthYear = itemView.findViewById(R.id.month_year);
            tvAccount = itemView.findViewById(R.id.account);
            tvPurpose = itemView.findViewById(R.id.purpose);
            tvAmount = itemView.findViewById(R.id.amount);

            ivAttach = itemView.findViewById(R.id.attach);

            if(ivAttach!=null){
                ivAttach.setOnClickListener(this);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(v==ivAttach){
                listener.onAttachmentClick(transactionResponseList.get(getAdapterPosition()));
            }else if(v==itemView){
                listener.onItemClick(transactionResponseList.get(getAdapterPosition()));
            }

        }

        public void bind(TransactionResponse transactionResponse){
            String date = MyUtil.getStringDate(transactionResponse.getDate());

            String[] dateArr = date.split("-");

            if(tvDate!=null){
                tvDate.setText(dateArr[0]);
            }


            tvMonthYear.setText(dateArr[1].concat(" - ").concat(dateArr[2]));
            tvAccount.setText("From ".concat(transactionResponse.getFrom().getName())
                    .concat(" to ").concat(transactionResponse.getTo().getName()));

            tvPurpose.setText(transactionResponse.getPurpose());
            tvAmount.setText(String.valueOf(transactionResponse.getAmount()));
        }
    }

    public interface TransactionListener{
        void onItemClick(TransactionResponse transactionResponse);
        void onAttachmentClick(TransactionResponse transactionResponse);
    }
}
