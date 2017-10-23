package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.graphics.Bitmap;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class WatchlistObject {
    private String title, currentPrice, change, cryptoID;
    private boolean color;
    private Bitmap btmp;
    private MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions();

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

    public void setCurrentPrice(String currentPrice) { this.currentPrice = currentPrice; }

    public boolean getChangeColor() { return color; }

    public void setChangeColor(boolean color) { this.color = color; }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public Bitmap getIcon() {
        return btmp;
    }

    public void setIcon(String ImageUrl) { this.btmp = myGlobalsFunctions.convertImageURLtoBitmap(ImageUrl); }
}
