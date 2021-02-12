package com.impulsed.currencyrate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class NBUFragment extends Fragment {


    RecyclerView nbuList;
    NBURecyclerAdapter adapter;
    LinearLayoutManager layoutManager;


    public NBUFragment() {
        // Required empty public constructor
    }


    public static NBUFragment newInstance() {
        return new NBUFragment();
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
        adapter = new NBURecyclerAdapter(new ArrayList<>(), getCurrencyMap());
        layoutManager =
                new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
        nbuList.setAdapter(adapter);
        nbuList.setLayoutManager(layoutManager);

        return v;
    }

    private HashMap<String, String> getCurrencyMap() {
        HashMap<String, String> map = new HashMap<>();
        String[] codes = getResources().getStringArray(R.array.currency_codes);
        String[] cur = getResources().getStringArray(R.array.currencies);
        for (int i = 0; i < codes.length; i++) {
            map.put(codes[i], cur[i]);
        }
        return map;
    }


}