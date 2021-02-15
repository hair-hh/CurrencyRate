package com.impulsed.currencyrate;

import android.provider.BaseColumns;

public final class RatesContract {

    private RatesContract(){}

    public static class RatesPBEntry implements BaseColumns{
        public static final String TABLE_NAME_PB = "pbEntry";
        public static final String COLUMN_NAME_CURRENCY = "currency";
        public static final String COLUMN_NAME_SALE = "saleRate";
        public static final String COLUMN_NAME_PURCHASE = "purchaseRate";
        public static final String COLUMN_NAME_DATE_PB = "datePB";
    }

    public static class RatesNBUEntry implements BaseColumns{
        public static final String TABLE_NAME_NBU = "nbuEntry";
        public static final String COLUMN_NAME_CURRENCY_CODE = "currencyCodeL";
        public static final String COLUMN_NAME_UNITS = "units";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_DATE_NBU = "dateNBU";
    }

}
