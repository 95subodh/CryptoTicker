package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings.SettingsActivity;
import com.apps.sky.cryptoticker.R;


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
        rootView.findViewById(R.id.more_tab_item_contact_us).setOnClickListener(this);
        rootView.findViewById(R.id.more_tab_item_ticker).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_tab_item_settings:
                Intent intent = new Intent(rootView.getContext(), SettingsActivity.class);
                rootView.getContext().startActivity(intent);
                break;

            case R.id.more_tab_item_about:
                break;

            case R.id.more_tab_item_contact_us:
                break;

            case R.id.more_tab_item_ticker:
                break;
        }
    }
}
