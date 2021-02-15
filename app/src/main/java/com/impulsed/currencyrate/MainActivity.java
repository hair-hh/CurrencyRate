package com.impulsed.currencyrate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements PBRecyclerAdapter.ItemViewHolder.OnItemClickListener {

    private static final String STATE_CHECK = "check";
    private static final String STATE_DATE_PB = "datePB";
    private static final String STATE_DATE_NBU = "dateNBU";
    private static final String STATE_DATE_PB_STR = "datePBString";
    private static final String STATE_DATE_NBU_STR = "dateNBUString";
    private static final String STATE_PB_FRAG = "pbFragment";
    private static final String STATE_NBU_FRAG = "nbuFragment";

    DatePickerDialog picker;
    String dateStringPB;
    String dateStringNBU;
    String pbDate, nbuDate;
    TextView dateTextPB, dateTextNBU;
    PBFragment pbFragment;
    NBUFragment nbuFragment;
    boolean state = false;
    ProgressBar pbProgress, nbuProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateTextPB = findViewById(R.id.datePBText);
        dateTextNBU = findViewById(R.id.dateNBUText);
        dateTextPB.setText(pbDate);
        dateTextNBU.setText(nbuDate);
        dateTextPB.setOnClickListener(this::setDateFormat);
        dateTextNBU.setOnClickListener(this::setDateFormat);
        pbProgress = findViewById(R.id.progressPB);
        nbuProgress = findViewById(R.id.progressNBU);
        pbProgress.setVisibility(View.VISIBLE);
        nbuProgress.setVisibility(View.VISIBLE);
        ImageView pickerIm = findViewById(R.id.datePickerIconPB);
        pickerIm.setOnClickListener(this::setDateFormat);
        ImageView pickerIcon = findViewById(R.id.datePickerIcon);
        pickerIcon.setOnClickListener(this::setDateFormat);
        setCurrentDate();
        setPBFragment();
        setNBUFragment();

        if (savedInstanceState == null) {
            NetworkManager manager = new NetworkManager(this);
            manager.getListPB(pbDate, pbProgress);
            manager.getListNBU(nbuDate, nbuProgress);
        } else {
            pbProgress.setVisibility(View.INVISIBLE);
            nbuProgress.setVisibility(View.INVISIBLE);
            state = savedInstanceState.getBoolean(STATE_CHECK);
            pbDate = savedInstanceState.getString(STATE_DATE_PB);
            nbuDate = savedInstanceState.getString(STATE_DATE_NBU);
            dateTextPB.setText(pbDate);
            dateTextNBU.setText(nbuDate);
            dateStringNBU = savedInstanceState.getString(STATE_DATE_NBU_STR);
            dateStringPB = savedInstanceState.getString(STATE_DATE_PB_STR);
            pbFragment = (PBFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, STATE_PB_FRAG);
            pbFragment.setOnItemClickListener(this);
            nbuFragment = (NBUFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, STATE_NBU_FRAG);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pbFrame, pbFragment)
                    .replace(R.id.nbuFrame, nbuFragment)
                    .commitNow();
        }


    }

    private void setCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mont = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        pbDate = setInitialDateTime(day, mont, year);
        nbuDate = setInitialDateTime(day, mont, year);
        dateTextPB.setText(pbDate);
        dateTextNBU.setText(nbuDate);
    }

    public void setDateFormat(View v) {
        final Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        if (v.getId() == R.id.datePickerIconPB
                || v.getId() == R.id.datePBText) {
            pbProgress.setVisibility(View.VISIBLE);

            picker = new DatePickerDialog(MainActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        pbDate = setInitialDateTime(dayOfMonth, month1, year1);
                        dateTextPB.setText(pbDate);
                        setNewData(STATE_PB_FRAG);
                    }, year, month, day);
            picker.show();
        } else if (v.getId() == R.id.datePickerIcon
                || v.getId() == R.id.dateNBUText) {
            nbuProgress.setVisibility(View.VISIBLE);

            picker = new DatePickerDialog(MainActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        nbuDate = setInitialDateTime(dayOfMonth, month1, year1);
                        dateTextNBU.setText(nbuDate);
                        setNewData(STATE_NBU_FRAG);
                    }, year, month, day);
            picker.show();
        }
    }

    private void setNewData(String frag) {
        SaveSQLiteData sqLiteData = new SaveSQLiteData(this);
        NetworkManager manager = new NetworkManager(this);
        switch (frag) {
            case STATE_PB_FRAG:
                if (sqLiteData.checkExistPB(pbDate)) {
                    pbFragment.getAdapter().setNewData(sqLiteData.getPB(pbDate));
                    pbProgress.setVisibility(View.INVISIBLE);
                } else {
                    manager.getListPB(pbDate, pbProgress);
                }
                break;
            case STATE_NBU_FRAG:
                if (sqLiteData.checkExistNBU(nbuDate)) {
                    nbuFragment.getAdapter().setNewData(sqLiteData.getNBU(nbuDate));
                    pbProgress.setVisibility(View.INVISIBLE);
                } else
                    manager.getListNBU(nbuDate, nbuProgress);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + frag);
        }
    }

    private String setInitialDateTime(int day, int month, int year) {
        String res;
        String dayZero = day >= 10 ? "" : "0";
        String monthZero = month + 1 >= 10 ? "" : "0";
        String dot = ".";
        res = dayZero + day + dot + monthZero + (month + 1) + dot + year;
        return res;
    }


    public void setNBUFragment() {
        if (nbuFragment == null)
            nbuFragment = NBUFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.nbuFrame, this.nbuFragment)
                .setReorderingAllowed(true)
                .commitNow();
    }

    public void setPBFragment() {
        if (pbFragment == null) {
            pbFragment = PBFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.pbFrame, this.pbFragment)
                .setReorderingAllowed(true)
                .commitNow();
        pbFragment.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        String currency = pbFragment.getAdapter().getItem(position).getCurrency();
        if (currency.equals("PLZ"))
            currency = "PLN";
        if (nbuFragment.getAdapter().checkIfCurrencyExist(currency)) {
            nbuFragment.getAdapter().clearSelects();
            int pos = nbuFragment.getAdapter().getPosition(currency);
            nbuFragment.nbuList.smoothScrollToPosition(pos);
            nbuFragment.getAdapter().selectItem(pos);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(STATE_CHECK, true);
        outState.putString(STATE_DATE_PB, dateTextPB.getText().toString());
        outState.putString(STATE_DATE_NBU, dateTextNBU.getText().toString());
        outState.putString(STATE_DATE_PB_STR, dateStringPB);
        outState.putString(STATE_DATE_NBU_STR, dateStringNBU);
        getSupportFragmentManager().putFragment(outState, STATE_PB_FRAG, pbFragment);
        getSupportFragmentManager().putFragment(outState, STATE_NBU_FRAG, nbuFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        state = savedInstanceState.getBoolean(STATE_CHECK);
        dateTextPB.setText(savedInstanceState.getString(STATE_DATE_PB));
        pbFragment = (PBFragment) getSupportFragmentManager()
                .getFragment(savedInstanceState, STATE_PB_FRAG);
        pbFragment.setOnItemClickListener(this);
        nbuFragment = (NBUFragment) getSupportFragmentManager()
                .getFragment(savedInstanceState, STATE_NBU_FRAG);
        super.onRestoreInstanceState(savedInstanceState);
    }
}