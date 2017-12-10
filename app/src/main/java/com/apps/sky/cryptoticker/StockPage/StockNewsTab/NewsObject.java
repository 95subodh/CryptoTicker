package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;

/**
 * Created by ankitaverma on 26/09/17.
 */

public class NewsObject {
    private String title, publishedDate, link, author;
    private Drawable img;
    private MyGlobalsFunctions myGlobalsFunctions;

    String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getPublishedDate() {
        return publishedDate;
    }

    void setPublishedDate(String originalDate) { this.publishedDate = myGlobalsFunctions.getWeekDayFormattedDate(originalDate); }

    String getURL() {
        return link;
    }

    void setURL(String link) { this.link = link; }

    Drawable getImage() {
        return img;
    }

    void setImage(Drawable image) { this.img = image; }

    public void setContext(Context context) { myGlobalsFunctions = new MyGlobalsFunctions(context);}
}
