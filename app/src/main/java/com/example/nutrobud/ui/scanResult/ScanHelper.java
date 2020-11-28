package com.example.nutrobud.ui.scanResult;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.nutrobud.ui.home.Stats;

import java.util.HashMap;
import java.util.Map;

public class ScanHelper implements Parcelable {
    private int id;
    private int caloriesTrackedQty;
    private int carbohydrate;
    private int fat;
    private int protein;
    private int sodium;
    private int sugar;
    private int fiber;
    private String[] detectedAllergens;

    public ScanHelper() {
    }

    public ScanHelper(int id, int caloriesTrackedQty, int carbohydrate, int fat, int protein, int sodium, String[] detectedAllergens, int sugar, int fiber) {
        this.id = id;
        this.caloriesTrackedQty = caloriesTrackedQty;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
        this.sodium = sodium;
        this.detectedAllergens = detectedAllergens;
        this.sugar = sugar;
        this.fiber = fiber;
    }

    protected ScanHelper(Parcel in) {
        id = in.readInt();
        caloriesTrackedQty = in.readInt();
        carbohydrate = in.readInt();
        fat = in.readInt();
        protein = in.readInt();
        sodium = in.readInt();
        detectedAllergens = in.createStringArray();
        fiber = in.readInt();
        sugar = in.readInt();
    }

    public static final Creator<ScanHelper> CREATOR = new Creator<ScanHelper>() {
        @Override
        public ScanHelper createFromParcel(Parcel in) {
            return new ScanHelper(in);
        }

        @Override
        public ScanHelper[] newArray(int size) {
            return new ScanHelper[size];
        }
    };

    public int getCaloriesTrackedQty() {
        return caloriesTrackedQty;
    }

    public void setCaloriesTrackedQty(int caloriesTrackedQty) {
        this.caloriesTrackedQty = caloriesTrackedQty;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getDetectedAllergens() {
        return detectedAllergens;
    }

    public void setDetectedAllergens(String[] detectedAllergens) {
        this.detectedAllergens = detectedAllergens;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(caloriesTrackedQty);
        parcel.writeInt(carbohydrate);
        parcel.writeInt(fat);
        parcel.writeInt(protein);
        parcel.writeInt(sodium);
        parcel.writeStringArray(detectedAllergens);
        parcel.writeInt(fiber);
        parcel.writeInt(sugar);
    }
}
