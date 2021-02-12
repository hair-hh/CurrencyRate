package com.impulsed.currencyrate;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExchangeRate implements Parcelable {

    public static final Creator<ExchangeRate> CREATOR = new Creator<ExchangeRate>() {
        @Override
        public ExchangeRate createFromParcel(Parcel source) {
            return new ExchangeRate(source);
        }

        @Override
        public ExchangeRate[] newArray(int size) {
            return new ExchangeRate[size];
        }
    };
    @SerializedName("baseCurrency")
    @Expose
    private final String baseCurrency;
    @SerializedName("currency")
    @Expose
    private final String currency;
    @SerializedName("saleRateNB")
    @Expose
    private final Double saleRateNB;
    @SerializedName("purchaseRateNB")
    @Expose
    private final Double purchaseRateNB;
    @SerializedName("saleRate")
    @Expose
    private final Double saleRate;
    @SerializedName("purchaseRate")
    @Expose
    private final Double purchaseRate;

    protected ExchangeRate(Parcel in) {
        this.baseCurrency = in.readString();
        this.currency = in.readString();
        this.saleRateNB = (Double) in.readValue(Double.class.getClassLoader());
        this.purchaseRateNB = (Double) in.readValue(Double.class.getClassLoader());
        this.saleRate = (Double) in.readValue(Double.class.getClassLoader());
        this.purchaseRate = (Double) in.readValue(Double.class.getClassLoader());
    }

    public String getCurrency() {
        return currency;
    }

    public Double getSaleRate() {
        return saleRate;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.baseCurrency);
        dest.writeString(this.currency);
        dest.writeValue(this.saleRateNB);
        dest.writeValue(this.purchaseRateNB);
        dest.writeValue(this.saleRate);
        dest.writeValue(this.purchaseRate);
    }
}
