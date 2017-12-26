package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.content.Context;
import android.graphics.Bitmap;

import com.apps.pinbit.cryptoticker.Global.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class WatchlistObject {
    private String title, currentPrice, change, cryptoID, minDayPrice, maxDayPrice;
    private boolean color;
    private Bitmap btmp;
    private MyGlobalsFunctions myGlobalsFunctions;

    public WatchlistObject() {
        this.title = "-";
        this.currentPrice = "-";
        this.change = "-";
        this.cryptoID = "-";
        this.minDayPrice = "-";
        this.maxDayPrice = "-";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCryptoID() {
        return cryptoID;
    }

    public void setCryptoID(String cryptoID) {
        this.cryptoID = cryptoID;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public boolean getChangeColor() {
        return color;
    }

    public void setChangeColor(boolean color) {
        this.color = color;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getMinDayPrice() {
        return minDayPrice;
    }

    public void setMinDayPrice(String minDayPrice) {
        this.minDayPrice = minDayPrice;
    }

    public String getMaxDayPrice() {
        return maxDayPrice;
    }

    public void setMaxDayPrice(String maxDayPrice) {
        this.maxDayPrice = maxDayPrice;
    }

    public Bitmap getIcon() {
        return btmp;
    }

    public void setIcon(String ImageUrl) {
        this.btmp = myGlobalsFunctions.convertImageURLtoBitmap(ImageUrl);
    }

    public void setContext(Context context) {
        myGlobalsFunctions = new MyGlobalsFunctions(context);
    }
}
