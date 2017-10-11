package com.apps.sky.cryptoticker.HomePage.HomePageTabs.ChatTab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatTab extends Fragment {

    private View rootView;

    public ChatTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            return inflater.inflate(R.layout.chat_tab, container, false);
        }
        return rootView;
    }

}
