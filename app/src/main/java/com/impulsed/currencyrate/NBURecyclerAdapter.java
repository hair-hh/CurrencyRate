package com.impulsed.currencyrate;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class NBURecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<NBUPojo> items;
    HashMap<String, String> currencyMap;

    int selected = -1;


    public NBURecyclerAdapter(List<NBUPojo> items, HashMap<String, String> map) {
        this.items = items;
        this.currencyMap = map;
    }


    public void setNewData(List<NBUPojo> list) {
        items = list;
    }

    public void selectItem(int position) {
        selected = position;
        notifyDataSetChanged();
    }

    public void clearSelects() {
//        selects = new boolean[items.size()];
        selected = -1;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nbu_item, parent, false);
        return new ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NBUPojo data;
        DecimalFormat df = new DecimalFormat("##.##");
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        data = items.get(position);
        String cur = data.getCurrencyCodeL();
        String curr = currencyMap.get(cur);
        String unit = data.getUnits() + " " + cur;
        String amount = df.format(data.getAmount()) + " UAH";
        itemViewHolder.nbuCurrency.setText(curr);
        itemViewHolder.nbuUnits.setText(unit);
        itemViewHolder.nbuAmount.setText(amount);

        if (selected == position)
            itemViewHolder.itemView.setBackgroundColor(
                    itemViewHolder.itemView.getResources().getColor(R.color.orange_alpha));
        else
            itemViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public int getPosition(String currency) {
        for (int i = 0; i < items.size(); i++) {
            if (currency.equals(items.get(i).getCurrencyCodeL()))
                return i;
        }
        return -1;
    }

    public boolean checkIfCurrencyExist(String currency) {
        boolean check = false;
        for (NBUPojo p : items) {
            if (currency.equals(p.getCurrencyCodeL())) {
                check = true;
                break;
            }
        }
        return check;
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nbuCurrency, nbuUnits, nbuAmount;
        ConstraintLayout ct;


        public ItemViewHolder(View v) {
            super(v);

            ct = v.findViewById(R.id.nbuConstraint);
            ct.getRootView().setBackgroundColor(getAdapterPosition() % 2 == 0 ?
                    v.getResources().getColor(R.color.stripe_grey) :
                    v.getResources().getColor(R.color.transparent));
            nbuCurrency = v.findViewById(R.id.textCurrency);
            nbuUnits = v.findViewById(R.id.textUnits);
            nbuAmount = v.findViewById(R.id.textAmount);

        }


    }
}
