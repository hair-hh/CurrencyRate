package com.impulsed.currencyrate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;


public class PBFragment extends Fragment {

    RecyclerView pbList;
    private PBRecyclerAdapter adapter;
    PBRecyclerAdapter.ItemViewHolder.OnItemClickListener listener;

    public PBFragment() {
        // Required empty public constructor
    }

    public static PBFragment newInstance() {

        return new PBFragment();
    }

    public void setOnItemClickListener(PBRecyclerAdapter.ItemViewHolder.OnItemClickListener listener){
        this.listener = listener;
    }

    public PBRecyclerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_p_b, container, false);
        pbList = v.findViewById(R.id.pbList);
        adapter = new PBRecyclerAdapter(new ArrayList<>(), listener);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
        pbList.setAdapter(adapter);
        pbList.setLayoutManager(layoutManager);
        return v;
    }


}