package com.impulsed.currencyrate;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class PBFragment extends Fragment {

    private static final String TAG = "PB_FRAGMENT";

    private static final String LIST_TAG = "list";
    private static final String ITEMS_TAG = "items";

    RecyclerView pbList;
    PBRecyclerAdapter.ItemViewHolder.OnItemClickListener listener;
    private PBRecyclerAdapter adapter;

    public PBFragment() {
        // Required empty public constructor
    }

    public static PBFragment newInstance() {
        return new PBFragment();
    }

    public void setOnItemClickListener(PBRecyclerAdapter.ItemViewHolder
                                               .OnItemClickListener listener) {
        this.listener = listener;
        Log.d(TAG, "setClickListener");
    }

    public PBRecyclerAdapter getAdapter() {
        Log.d(TAG, "getAdapter");
        return adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_p_b, container, false);
        pbList = v.findViewById(R.id.pbList);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(Objects.requireNonNull(getActivity()).getBaseContext());
        adapter = new PBRecyclerAdapter(new ArrayList<>(), listener);
        pbList.setAdapter(adapter);
        pbList.setLayoutManager(layoutManager);

        if (savedInstanceState!=null){
            Log.d(TAG, "restoreInstanceState");
            adapter.setNewData(savedInstanceState.getParcelableArrayList(ITEMS_TAG));
            Objects.requireNonNull(pbList.getLayoutManager())
                    .onRestoreInstanceState(savedInstanceState.getParcelable(LIST_TAG));
        }
        Log.d(TAG, "onCreateView");
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Parcelable listState = Objects.requireNonNull(pbList.getLayoutManager())
                .onSaveInstanceState();
        outState.putParcelable(LIST_TAG, listState);
        outState.putParcelableArrayList(ITEMS_TAG, new ArrayList<>(getAdapter().items));
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
}