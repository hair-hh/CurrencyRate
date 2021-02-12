package com.impulsed.currencyrate;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PBRecyclerAdapter.ItemViewHolder.OnItemClickListener {

    private static final String STATE_PB = "itemsPB";
    private static final String STATE_NBU = "itemsNBU";
    private static final String STATE_CHECK = "check";
    private static final String STATE_DATE_PB = "datePB";
    private static final String STATE_DATE_NBU = "dateNBU";


    Calendar date = Calendar.getInstance();
    String dateString;
    String pbDate, nbuDate;
    TextView dateTextPB, dateTextNBU;
    PBFragment pbFragment;
    NBUFragment nbuFragment;
    FragmentManager fm;
    List<NBUPojo> itemsNBU;
    List<ExchangeRate> itemsPB;
    boolean state = false;

    DatePickerDialog.OnDateSetListener pbd = (view, year, monthOfYear, dayOfMonth) -> {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTimePB();
    };
    DatePickerDialog.OnDateSetListener nbu = (view, year, monthOfYear, dayOfMonth) -> {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateTimeNBU();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            state = savedInstanceState.getBoolean(STATE_CHECK);

            pbDate = savedInstanceState.getString(STATE_DATE_PB);
            nbuDate = savedInstanceState.getString(STATE_DATE_NBU);

            Gson gson = new Gson();

            itemsPB = new ArrayList<>();
            itemsNBU = new ArrayList<>();

            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            Set<String> pb = new HashSet<>(preferences.getStringSet(STATE_PB, null));
            if (pb.size() > 0) {
                for (String s : pb) {
                    itemsPB.add(gson.fromJson(s, ExchangeRate.class));
                }
            }
            Set<String> nbu = new HashSet<>(preferences.getStringSet(STATE_NBU, null));
            if (nbu.size()>0){
                for (String s: nbu){
                    itemsNBU.add(gson.fromJson(s, NBUPojo.class));
                }
            }
        }
        setContentView(R.layout.activity_main);
        dateTextPB = findViewById(R.id.datePBText);
        dateTextNBU = findViewById(R.id.dateNBUText);
        dateTextPB.setText(pbDate);
        dateTextNBU.setText(nbuDate);
        fm = getSupportFragmentManager();
        setPBFragment();
        setNBUFragment();
        if (!state){
            setInitialDateTimeNBU();
            setInitialDateTimePB();
        } else {
            pbFragment.getAdapter().setNewData(itemsPB);
            pbFragment.getAdapter().notifyDataSetChanged();
            nbuFragment.adapter.setNewData(itemsNBU);
            nbuFragment.adapter.notifyDataSetChanged();
        }
        ImageView pickerIm = findViewById(R.id.datePickerIconPB);
        pickerIm.setOnClickListener(this::setDateFormat);
        ImageView picker = findViewById(R.id.datePickerIcon);
        picker.setOnClickListener(this::setDateFormat);
    }

    public void setDateFormat(View v) {
        if (v.getId() == R.id.datePickerIconPB) {
            new DatePickerDialog(this, pbd, date.get(Calendar.YEAR)
                    , date.get(Calendar.MONTH)
                    , date.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v.getId() == R.id.datePickerIcon) {
            new DatePickerDialog(this, nbu, date.get(Calendar.YEAR)
                    , date.get(Calendar.MONTH)
                    , date.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private void setInitialDateTimePB() {
        String dayZero = date.get(Calendar.DAY_OF_MONTH) >= 10 ? "" : "0";
        String monthZero = date.get(Calendar.MONTH) + 1 >= 10 ? "" : "0";
        String dot = ".";
        dateString = dayZero +
                date.get(Calendar.DAY_OF_MONTH) +
                dot +
                monthZero +
                (date.get(Calendar.MONTH) + 1) +
                dot +
                date.get(Calendar.YEAR);
        SpannableString content = new SpannableString(dateString);
        content.setSpan(new UnderlineSpan(), 0, dateString.length(), 0);
        dateTextPB.setText(content);
        getItemsPB(dateString);

    }

    private void setInitialDateTimeNBU() {
        String dayZero = date.get(Calendar.DAY_OF_MONTH) >= 10 ? "" : "0";
        String monthZero = date.get(Calendar.MONTH) + 1 >= 10 ? "" : "0";
        String dot = ".";
        dateString = dayZero +
                date.get(Calendar.DAY_OF_MONTH) +
                dot +
                monthZero +
                (date.get(Calendar.MONTH) + 1) +
                dot +
                date.get(Calendar.YEAR);
        SpannableString content = new SpannableString(dateString);
        content.setSpan(new UnderlineSpan(), 0, dateString.length(), 0);
        dateTextNBU.setText(content);
        getItemsNBU(dateString);
    }

    private void getItemsPB(String dateString) {
        App.getPbApi().getData(1, dateString).enqueue(new Callback<PBPojo>() {
            @Override
            public void onResponse(@NonNull Call<PBPojo> call, @NonNull Response<PBPojo> response) {
                if (response.isSuccessful()) {
                    itemsPB = new ArrayList<>();
                    List<ExchangeRate> list;
                    assert response.body() != null;
                    list = response.body().getExchangeRate();

                    for (ExchangeRate rate : list) {
                        if (rate.getPurchaseRate() != null
                                && rate.getSaleRate() != null
                                && rate.getCurrency() != null)
                            itemsPB.add(rate);

                    }
                    Collections.reverse(itemsPB);
                    pbFragment.getAdapter().setNewData(itemsPB);
                    pbFragment.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PBPojo> call, @NonNull Throwable t) {

            }
        });
    }

    private void getItemsNBU(String dateString) {
        App.getNbuApi().getData(1, dateString).enqueue(new Callback<List<NBUPojo>>() {
            @Override
            public void onResponse(@NonNull Call<List<NBUPojo>> call, @NonNull Response<List<NBUPojo>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    itemsNBU = response.body();
                    nbuFragment.adapter.setNewData(itemsNBU);
                    nbuFragment.adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NBUPojo>> call, @NonNull Throwable t) {

            }
        });
    }

    private void setNBUFragment() {
        nbuFragment = NBUFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.nbuFrame, nbuFragment)
                .commit();
    }

    private void setPBFragment() {
        pbFragment = PBFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.pbFrame, pbFragment)
                .setReorderingAllowed(true)
                .commit();
        pbFragment.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        String currency = itemsPB.get(position).getCurrency();
        if (currency.equals("PLZ"))
            currency = "PLN";
        if (nbuFragment.adapter.checkIfCurrencyExist(currency)) {
            nbuFragment.adapter.clearSelects();
            int pos = nbuFragment.adapter.getPosition(currency);
            nbuFragment.nbuList.smoothScrollToPosition(pos);
            nbuFragment.adapter.selectItem(pos);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_CHECK, true);
        outState.putString(STATE_DATE_PB, dateTextPB.getText().toString());
        outState.putString(STATE_DATE_NBU, dateTextNBU.getText().toString());
        Gson gson = new Gson();
        Set<String> pb = new HashSet<>();
        Set<String> nbu = new HashSet<>();
        for (ExchangeRate r : itemsPB) {
            pb.add(gson.toJson(r));
        }
        for (NBUPojo n : itemsNBU) {
            nbu.add(gson.toJson(n));
        }
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(STATE_PB, pb);
        editor.putStringSet(STATE_NBU, nbu);
        editor.apply();
    }
}