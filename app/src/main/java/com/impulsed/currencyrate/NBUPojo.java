package com.impulsed.currencyrate;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NBUPojo implements Parcelable {

    public static final Parcelable.Creator<NBUPojo> CREATOR = new Parcelable.Creator<NBUPojo>() {
        @Override
        public NBUPojo createFromParcel(Parcel source) {
            return new NBUPojo(source);
        }

        @Override
        public NBUPojo[] newArray(int size) {
            return new NBUPojo[size];
        }
    };
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("TimeSign")
    @Expose
    private String timeSign;
    @SerializedName("CurrencyCode")
    @Expose
    private String currencyCode;
    @SerializedName("CurrencyCodeL")
    @Expose
    private String currencyCodeL;
    @SerializedName("Units")
    @Expose
    private Integer units;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    protected NBUPojo(Parcel in) {
        this.startDate = in.readString();
        this.timeSign = in.readString();
        this.currencyCode = in.readString();
        this.currencyCodeL = in.readString();
        this.units = (Integer) in.readValue(Integer.class.getClassLoader());
        this.amount = (Double) in.readValue(Double.class.getClassLoader());
    }
    public NBUPojo() {
    }

    public String getCurrencyCodeL() {
        return currencyCodeL;
    }

    public void setCurrencyCodeL(String currencyCodeL) {
        this.currencyCodeL = currencyCodeL;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startDate);
        dest.writeString(this.timeSign);
        dest.writeString(this.currencyCode);
        dest.writeString(this.currencyCodeL);
        dest.writeValue(this.units);
        dest.writeValue(this.amount);
    }
}
