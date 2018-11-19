package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MoreTab.About;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.apps.pinbit.cryptoticker.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by subyadav on 24/12/17.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0.3");
        View aboutPage = new AboutPage(getApplicationContext())
                .isRTL(false)
                .setImage(R.drawable.pinbit)
                .setDescription("MADE with LOVE <3")
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("pinbitcorp@outlook.com")
                .addPlayStore("com.apps.pinbit.cryptoticker")
                .create();
        setContentView(aboutPage);
    }
}
