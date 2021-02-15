package com.impulsed.currencyrate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class PBRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "PB_FRAGMENT_ADAPTER";

    List<ExchangeRate> items;
    private final ItemViewHolder.OnItemClickListener onItemClickListener;

    public PBRecyclerAdapter(List<ExchangeRate> items, ItemViewHolder.OnItemClickListener listener) {
        this.items = items;
        this.onItemClickListener = listener;
        Log.d(TAG, "constructor");
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.pb_item, parent, false);
            Log.d(TAG, "onCreateViewHolder");
            return new ItemViewHolder(item, onItemClickListener);

    }

    public ExchangeRate getItem(int position){
        return items.get(position);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ExchangeRate data;
        DecimalFormat df = new DecimalFormat("##.###");
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        data = items.get(position);
        String curr = data.getCurrency();
        String sale = String.valueOf(df.format(data.getSaleRate()));
        String pur = String.valueOf(df.format(data.getPurchaseRate()));
        itemViewHolder.pbItemCurrency.setText(curr);
        itemViewHolder.pbItemSale.setText(sale);
        itemViewHolder.pbItemPurchase.setText(pur);
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        if (items==null)
            return 0;
        Log.d(TAG, "getItemCount");
        return items.size();
    }
    
    public void setNewData(List<ExchangeRate> list) {
        items = list;
        notifyDataSetChanged();
        Log.d(TAG, "setNewData");
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pbItemCurrency, pbItemPurchase, pbItemSale;

        OnItemClickListener onItemClickListener;

        public ItemViewHolder(View v, OnItemClickListener onItemClickListener) {
            super(v);

                pbItemCurrency = v.findViewById(R.id.pbCurrencyTVIt);
                pbItemPurchase = v.findViewById(R.id.pbPurchaseTVIt);
                pbItemSale = v.findViewById(R.id.pbSaleTVIt);
                this.onItemClickListener = onItemClickListener;
                v.setOnClickListener(this);
            Log.d(TAG, "ViewHolder_constructor");
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
