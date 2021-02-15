package com.impulsed.currencyrate;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkManager {

    private final SaveSQLiteData sqLiteData;
    private final Context mContext;
    private List<ExchangeRate> pbList;
    private List<NBUPojo> nbuList;


    public NetworkManager(Context context) {
        sqLiteData = new SaveSQLiteData(context);
        this.mContext = context;
    }

    public void getListPB(String date, ProgressBar progressBar) {
        App.getPbApi().getData(1, date).enqueue(new Callback<PBPojo>() {
            @Override
            public void onResponse(@NotNull Call<PBPojo> call, @NotNull Response<PBPojo> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    pbList = new ArrayList<>();
                    assert response.body() != null;
                    List<ExchangeRate> list = response.body().getExchangeRate();
                    for (ExchangeRate rate : list) {
                        if (rate.getPurchaseRate() != null
                                && rate.getSaleRate() != null
                                && rate.getCurrency() != null)
                            pbList.add(rate);
                    }
                    Collections.reverse(pbList);
                    sqLiteData.setupPB(pbList, date);
                    ((MainActivity) mContext).pbFragment.getAdapter().setNewData(pbList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<PBPojo> call, @NotNull Throwable t) {

            }
        });
    }

    public void getListNBU(String date, ProgressBar progressBar) {
        App.getNbuApi().getData(1, date).enqueue(new Callback<List<NBUPojo>>() {
            @Override
            public void onResponse(@NotNull Call<List<NBUPojo>> call, @NotNull Response<List<NBUPojo>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    assert response.body() != null;
                    nbuList = response.body();
                    sqLiteData.setupNBU(nbuList, date);
                    ((MainActivity) mContext).nbuFragment.getAdapter().setNewData(nbuList);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<NBUPojo>> call, @NotNull Throwable t) {

            }
        });
    }
}
