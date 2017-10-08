package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.R;

public class WatchlistTab extends Fragment {


    public WatchlistTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.watchlist_tab, container, false);
    }
}