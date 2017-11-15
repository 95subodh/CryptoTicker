package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.content.Context;
import android.graphics.Bitmap;

import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class MyPortfolioObject {
    private String title, currentValue, myProfit, cryptoID;
    private boolean color;
    private Bitmap btmp;
    private MyGlobalsFunctions myGlobalsFunctions;

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

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) { this.currentValue = currentValue; }

    public String getMyProfit() {
        return myProfit;
    }

    public void setMyProfit(String myProfit) {
        this.myProfit = myProfit;
    }

    public boolean getChangeColor() { return color; }

    public void setChangeColor(boolean color) { this.color = color; }

    public Bitmap getIcon() {
        return btmp;
    }

    public void setIcon(String ImageUrl) { this.btmp = myGlobalsFunctions.convertImageURLtoBitmap(ImageUrl); }

    public void setContext(Context context) {
        myGlobalsFunctions = new MyGlobalsFunctions(context);
    }
}
