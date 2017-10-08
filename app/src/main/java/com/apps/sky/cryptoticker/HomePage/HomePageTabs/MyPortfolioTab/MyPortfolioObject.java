package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class MyPortfolioObject {
    private String title, currentPrice, myProfit;
    private Bitmap btmp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setIcon(String ImageUrl) {
        try {
            URL url = new URL(ImageUrl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            this.btmp = mIcon;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            this.btmp = null;
        }
    }
}
