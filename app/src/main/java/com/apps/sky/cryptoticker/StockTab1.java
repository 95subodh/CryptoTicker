package com.apps.sky.cryptoticker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockTab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stock_tab_1, container, false);

        return rootView;
    }
}
