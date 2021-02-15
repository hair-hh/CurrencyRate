package com.impulsed.currencyrate;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class NBUFragment extends Fragment {

    private static final String LIST_TAG = "list";
    private static final String ITEMS_TAG = "items";

    RecyclerView nbuList;
    LinearLayoutManager layoutManager;
    private NBURecyclerAdapter adapter;
    public NBUFragment() {
        // Required empty public constructor
    }

    public static NBUFragment newInstance() {
        return new NBUFragment();
    }

    public NBURecyclerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_n_b_u, container, false);

        nbuList = v.findViewById(R.id.nbuList);
        layoutManager =
                new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
        adapter = new NBURecyclerAdapter(new ArrayList<>(), getCurrencyMap());
        nbuList.setAdapter(adapter);
        nbuList.setLayoutManager(layoutManager);
        if (savedInstanceState != null) {
            adapter.setNewData(savedInstanceState.getParcelableArrayList(ITEMS_TAG));
            Objects.requireNonNull(nbuList.getLayoutManager())
                    .onRestoreInstanceState(savedInstanceState.getParcelable(LIST_TAG));
        }
        return v;
    }

    public HashMap<String, String> getCurrencyMap() {
        HashMap<String, String> map = new HashMap<>();
        String[] codes = getResources().getStringArray(R.array.currency_codes);
        String[] cur = getResources().getStringArray(R.array.currencies);
        for (int i = 0; i < codes.length; i++) {
            map.put(codes[i], cur[i]);
        }
        return map;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable stateList = Objects.requireNonNull(nbuList.getLayoutManager())
                .onSaveInstanceState();
        outState.putParcelable(LIST_TAG, stateList);
        outState.putParcelableArrayList(ITEMS_TAG, new ArrayList<>(adapter.items));
        super.onSaveInstanceState(outState);
    }
}