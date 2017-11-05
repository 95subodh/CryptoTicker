package com.apps.sky.cryptoticker.StockPage.StockPredictionTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.R;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

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

        ValueLineChart mCubicValueLineChart = comingSoonView.findViewById(R.id.cubiclinechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("Jan", 24000f));
        series.addPoint(new ValueLinePoint("Feb", 24200f));
        series.addPoint(new ValueLinePoint("Mar", 24500f));
        series.addPoint(new ValueLinePoint("Apr", 24900f));
        series.addPoint(new ValueLinePoint("Mai", 24350f));
        series.addPoint(new ValueLinePoint("Jun", 23800f));
        series.addPoint(new ValueLinePoint("Jul", 24020f));
        series.addPoint(new ValueLinePoint("Aug", 24700f));
        series.addPoint(new ValueLinePoint("Sep", 24710f));
        series.addPoint(new ValueLinePoint("Oct", 26000f));
        series.addPoint(new ValueLinePoint("Nov", 25500f));
        series.addPoint(new ValueLinePoint("Dec", 23000f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();

        return rootView;
    }
}