package com.impulsed.currencyrate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class PBRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<ExchangeRate> items;
    private final ItemViewHolder.OnItemClickListener onItemClickListener;

    public PBRecyclerAdapter(List<ExchangeRate> items, ItemViewHolder.OnItemClickListener listener) {
        this.items = items;
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.pb_item, parent, false);
            return new ItemViewHolder(item, onItemClickListener);
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
    }

    @Override
    public int getItemCount() {
        return items.size() ;
    }
    
    public void setNewData(List<ExchangeRate> list) {
        items = list;
        notifyDataSetChanged();
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
