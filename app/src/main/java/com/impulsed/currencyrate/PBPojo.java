package com.impulsed.currencyrate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PBPojo {

    @SerializedName("exchangeRate")
    @Expose
    private final List<ExchangeRate> exchangeRate = null;


    public List<ExchangeRate> getExchangeRate() {
        return exchangeRate;
    }


}
