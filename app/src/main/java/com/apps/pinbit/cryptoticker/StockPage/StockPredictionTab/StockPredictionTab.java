package com.apps.pinbit.cryptoticker.StockPage.StockPredictionTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apps.pinbit.cryptoticker.R;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockPredictionTab extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_prediction_tab, container, false);
        }

        RelativeLayout mainView = rootView.findViewById(R.id.main_view);
        RelativeLayout comingSoonView = (RelativeLayout) inflater.inflate(R.layout.coming_soon_view, container, false);
        mainView.addView(comingSoonView);

        return rootView;
    }
}