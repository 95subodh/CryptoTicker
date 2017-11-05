package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.content.Context;
import android.graphics.Bitmap;

import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsObject {
    private String title, publishedDate, link, author;
    private Bitmap btmp;
    private MyGlobalsFunctions myGlobalsFunctions;

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

    public void setPublishedDate(String originalDate) { this.publishedDate = myGlobalsFunctions.getWeekDayFormattedDate(originalDate); }

    public String getURL() {
        return link;
    }

    public void setURL(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return btmp;
    }

    public void setImage(String ImageUrl) { this.btmp = myGlobalsFunctions.convertImageURLtoBitmap(ImageUrl); }

    public void setContext(Context context) {
        myGlobalsFunctions = new MyGlobalsFunctions(context);
    }
}
