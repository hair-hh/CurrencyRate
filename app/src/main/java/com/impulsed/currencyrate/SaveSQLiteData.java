package com.impulsed.currencyrate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SaveSQLiteData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rates_entries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES_PB =
            "CREATE TABLE " + RatesContract.RatesPBEntry.TABLE_NAME_PB + " (" +
                    RatesContract.RatesPBEntry.COLUMN_NAME_CURRENCY + " TEXT PRIMARY KEY," +
                    RatesContract.RatesPBEntry.COLUMN_NAME_PURCHASE + " REAL," +
                    RatesContract.RatesPBEntry.COLUMN_NAME_SALE + " REAL," +
                    RatesContract.RatesPBEntry.COLUMN_NAME_DATE_PB + " TEXT)";
    private static final String SQL_CREATE_TABLE_NBU =
            "CREATE TABLE " + RatesContract.RatesNBUEntry.TABLE_NAME_NBU + " (" +
                    RatesContract.RatesNBUEntry.COLUMN_NAME_CURRENCY_CODE + " TEXT PRIMARY KEY," +
                    RatesContract.RatesNBUEntry.COLUMN_NAME_UNITS + " INTEGER," +
                    RatesContract.RatesNBUEntry.COLUMN_NAME_AMOUNT + " REAL," +
                    RatesContract.RatesNBUEntry.COLUMN_NAME_DATE_NBU + " TEXT)";

    private static final String SQL_DELETE_PB =
            "DROP TABLE IF EXISTS " + RatesContract.RatesPBEntry.TABLE_NAME_PB;

    private static final String SQL_DELETE_NBU =
            "DROP TABLE IF EXISTS " + RatesContract.RatesNBUEntry.TABLE_NAME_NBU;

    public SaveSQLiteData(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_PB);
        db.execSQL(SQL_CREATE_TABLE_NBU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PB);
        db.execSQL(SQL_DELETE_NBU);
        onCreate(db);
    }

    public void setupPB(List<ExchangeRate> items, String date) {
        SQLiteDatabase db = getWritableDatabase();
        if (!checkExistPB(date)) {
            for (ExchangeRate r : items) {
                ContentValues cv = new ContentValues();
                cv.put(RatesContract.RatesPBEntry.COLUMN_NAME_CURRENCY, r.getCurrency());
                cv.put(RatesContract.RatesPBEntry.COLUMN_NAME_SALE, r.getSaleRate());
                cv.put(RatesContract.RatesPBEntry.COLUMN_NAME_PURCHASE, r.getPurchaseRate());
                cv.put(RatesContract.RatesPBEntry.COLUMN_NAME_DATE_PB, date);
                db.insert(RatesContract.RatesPBEntry.TABLE_NAME_PB, null, cv);
            }
        }
    }

    public void setupNBU(List<NBUPojo> items, String date) {
        SQLiteDatabase db = getWritableDatabase();
        if (!checkExistNBU(date)) {
            for (NBUPojo n : items) {
                ContentValues cv = new ContentValues();
                cv.put(RatesContract.RatesNBUEntry.COLUMN_NAME_CURRENCY_CODE, n.getCurrencyCodeL());
                cv.put(RatesContract.RatesNBUEntry.COLUMN_NAME_UNITS, n.getUnits());
                cv.put(RatesContract.RatesNBUEntry.COLUMN_NAME_AMOUNT, n.getAmount());
                cv.put(RatesContract.RatesNBUEntry.COLUMN_NAME_DATE_NBU, date);
                db.insert(RatesContract.RatesNBUEntry.TABLE_NAME_NBU, null, cv);
            }
        }
    }

    public List<ExchangeRate> getPB(String date) {
        List<ExchangeRate> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                RatesContract.RatesPBEntry.COLUMN_NAME_CURRENCY,
                RatesContract.RatesPBEntry.COLUMN_NAME_SALE,
                RatesContract.RatesPBEntry.COLUMN_NAME_PURCHASE,
                RatesContract.RatesPBEntry.COLUMN_NAME_DATE_PB
        };

        String selection = RatesContract.RatesPBEntry.COLUMN_NAME_DATE_PB + " =?";
        String[] selectionArgs = {date};
        String sortOrder = RatesContract.RatesPBEntry.COLUMN_NAME_CURRENCY + " DESC";

        Cursor cursor = db.query(
                RatesContract.RatesPBEntry.TABLE_NAME_PB,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            ExchangeRate r = new ExchangeRate();
            r.setCurrency(cursor.getString(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesPBEntry.COLUMN_NAME_CURRENCY)));
            r.setPurchaseRate(cursor.getDouble(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesPBEntry.COLUMN_NAME_PURCHASE)));
            r.setSaleRate(cursor.getDouble(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesPBEntry.COLUMN_NAME_SALE)));
            list.add(r);
        }
        cursor.close();
        return list;
    }

    public List<NBUPojo> getNBU(String date) {
        List<NBUPojo> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                RatesContract.RatesNBUEntry.COLUMN_NAME_CURRENCY_CODE,
                RatesContract.RatesNBUEntry.COLUMN_NAME_UNITS,
                RatesContract.RatesNBUEntry.COLUMN_NAME_AMOUNT,
                RatesContract.RatesNBUEntry.COLUMN_NAME_DATE_NBU
        };

        String selection = RatesContract.RatesNBUEntry.COLUMN_NAME_DATE_NBU + " =?";
        String[] selectionArgs = {date};
        String sortOrder = RatesContract.RatesNBUEntry.COLUMN_NAME_CURRENCY_CODE + " DESC";

        Cursor cursor = db.query(
                RatesContract.RatesNBUEntry.TABLE_NAME_NBU,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            NBUPojo r = new NBUPojo();
            r.setCurrencyCodeL(cursor.getString(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesNBUEntry.COLUMN_NAME_CURRENCY_CODE)));
            r.setUnits(cursor.getInt(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesNBUEntry.COLUMN_NAME_UNITS)));
            r.setAmount(cursor.getDouble(
                    cursor.getColumnIndexOrThrow(RatesContract.RatesNBUEntry.COLUMN_NAME_AMOUNT)));
            list.add(r);
        }
        cursor.close();
        return list;
    }

    public boolean checkExistPB(String date) {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, RatesContract.RatesPBEntry.TABLE_NAME_PB
                , RatesContract.RatesPBEntry.COLUMN_NAME_DATE_PB + " =?"
                , new String[]{date}) > 0;
    }

    public boolean checkExistNBU(String date) {
        SQLiteDatabase db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, RatesContract.RatesNBUEntry.TABLE_NAME_NBU
                , RatesContract.RatesNBUEntry.COLUMN_NAME_DATE_NBU + " =?"
                , new String[]{date}) > 0;
    }
}
