package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MoreTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MoreTab.About.AboutActivity;
import com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings.SettingsActivity;
import com.apps.pinbit.cryptoticker.R;


public class MoreTab extends Fragment implements View.OnClickListener {

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.more_tab, container, false);
        }
        rootView.findViewById(R.id.more_tab_item_settings).setOnClickListener(this);
        rootView.findViewById(R.id.more_tab_item_about).setOnClickListener(this);
//        rootView.findViewById(R.id.more_tab_item_contact_us).setOnClickListener(this);
        rootView.findViewById(R.id.more_tab_item_share_app).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_tab_item_settings:
                Intent settingIntent = new Intent(rootView.getContext(), SettingsActivity.class);
                rootView.getContext().startActivity(settingIntent);
                break;

            case R.id.more_tab_item_about:
                Intent aboutIntent = new Intent(rootView.getContext(), AboutActivity.class);
                rootView.getContext().startActivity(aboutIntent);
                break;

//            case R.id.more_tab_item_contact_us:
//                break;

            case R.id.more_tab_item_share_app:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "This is a cool app for watching and making crytocurrent portfolios. https://github.com/95subodh/CryptoTicker";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
    }
}
