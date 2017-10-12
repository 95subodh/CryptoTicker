package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsObject {
    private String title, publishedDate, link, author;
    private Bitmap btmp;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String originalDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(originalDate));
        }
        catch (Exception e) {
            System.out.println(e);
        }
        this.publishedDate = calendar.getTime().toString().substring(0, 11);
    }

    public String getURL() {
        return link;
    }

    public void setURL(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return btmp;
    }

    public void setImage(String ImageUrl) {
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
