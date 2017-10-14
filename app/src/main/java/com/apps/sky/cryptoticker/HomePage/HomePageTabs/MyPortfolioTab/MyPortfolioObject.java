package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.graphics.Bitmap;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class MyPortfolioObject {
    private String title, currentPrice, myProfit, crypto;
    private Bitmap btmp;
    private MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCrypto() {
        return crypto;
    }

    public void setCrypto(String crypto) {
        this.crypto = crypto;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) { this.currentPrice = currentPrice; }

    public String getMyProfit() {
        return myProfit;
    }

    public void setMyProfit(String myProfit) {
        this.myProfit = myProfit;
    }

    public Bitmap getIcon() {
        return btmp;
    }

    public void setIcon(String ImageUrl) { this.btmp = myGlobalsFunctions.convertImageURLtoBitmap(ImageUrl); }

}
