package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ankitaverma on 06/10/17.
 */

public class WatchlistObject {
    private String title, currentPrice, change;
    boolean color;
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
