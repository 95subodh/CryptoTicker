package com.apps.sky.cryptoticker.StockPage.StockInfoTab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockInfoTab extends Fragment implements View.OnClickListener {

    private View rootView;
    private String price, change, rank, cap, avlsup, lstupd, high, low;
    TextView coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinLstUpdate, highVal, lowVal, text24, text7, text14, text30, text60, text90, textAll;
    String url, iconUrl, highLowUrl24, highLowUrl7, highLowUrl14, highLowUrl30, highLowUrl60, highLowUrl90, highLowUrlAll, currency;
    public String cryptoID;
    MyGlobalsFunctions myGlobalsFunctions;
    SharedPreferences sharedPreferences;
    ValueLineChart cubicValueLineChart;
    ValueLineSeries series24, series7, series14, series30, series60, series90, seriesAll;
    ArrayList<TextView> texts;
    ArrayList<Integer> views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_info_tab, container, false);
        }

        rootView.findViewById(R.id.h_24).setOnClickListener(this);
        rootView.findViewById(R.id.d_7).setOnClickListener(this);
        rootView.findViewById(R.id.d_14).setOnClickListener(this);
        rootView.findViewById(R.id.d_30).setOnClickListener(this);
        rootView.findViewById(R.id.d_60).setOnClickListener(this);
        rootView.findViewById(R.id.d_90).setOnClickListener(this);
        rootView.findViewById(R.id.alltime).setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);

        currency = sharedPreferences.getString(Constants.CURRENT_CURRENCY, "");
        if (currency.equals("")) currency = "INR";

        url = "https://api.coinmarketcap.com/v1/ticker/"+cryptoID+"/?convert=" + currency.toUpperCase();
        iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/"+cryptoID+".png";
        highLowUrl24 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/24_hours.json";
        highLowUrl7 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/7_days.json";
        highLowUrl14 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/14_days.json";
        highLowUrl30 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/30_days.json";
        highLowUrl60 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/60_days.json";
        highLowUrl90 = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/90_days.json";
        highLowUrlAll = "https://www.coingecko.com/en/chart/" + cryptoID + "/" + currency.toLowerCase() + ".json";

        myGlobalsFunctions = new MyGlobalsFunctions(rootView.getContext());

        cubicValueLineChart = rootView.findViewById(R.id.cubic_line_chart);
        cubicValueLineChart.setUseDynamicScaling(true);
        text24 = rootView.findViewById(R.id.h_24_textview);
        text24.setTypeface(null, Typeface.BOLD);
        text24.setTextSize(16);
        text7 = rootView.findViewById(R.id.d_7_textview);
        text14 = rootView.findViewById(R.id.d_14_textview);
        text30 = rootView.findViewById(R.id.d_30_textview);
        text60 = rootView.findViewById(R.id.d_60_textview);
        text90 = rootView.findViewById(R.id.d_90_textview);
        textAll = rootView.findViewById(R.id.d_alltime_textview);

        texts = new ArrayList<>();
        texts.add(text24); texts.add(text7); texts.add(text14); texts.add(text30); texts.add(text60); texts.add(text90); texts.add(textAll);

        views = new ArrayList<>();
        views.add(R.id.h_24); views.add(R.id.d_7); views.add(R.id.d_14); views.add(R.id.d_30); views.add(R.id.d_60); views.add(R.id.d_90); views.add(R.id.alltime);

        series24 = new ValueLineSeries();
        series24.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        series7 = new ValueLineSeries();
        series7.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        series14 = new ValueLineSeries();
        series14.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        series30 = new ValueLineSeries();
        series30.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        series60 = new ValueLineSeries();
        series60.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        series90 = new ValueLineSeries();
        series90.setColor(getContext().getResources().getColor(R.color.colorAccentLight));
        seriesAll = new ValueLineSeries();
        seriesAll.setColor(getContext().getResources().getColor(R.color.colorAccentLight));

        new JSONTask().execute(url, iconUrl, highLowUrl24, highLowUrl7, highLowUrl14, highLowUrl30, highLowUrl60, highLowUrl90, highLowUrlAll);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        for (int i = 0; i < 7; ++i) {
            if (view.getId() == views.get(i)) {
                texts.get(i).setTypeface(null, Typeface.BOLD);
                texts.get(i).setTextSize(16);
            }
            else {
                texts.get(i).setTypeface(null, Typeface.NORMAL);
                texts.get(i).setTextSize(12);
            }
        }

        switch (view.getId()) {
            case (R.id.h_24):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series24);
                break;

            case (R.id.d_7):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series7);
                break;

            case (R.id.d_14):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series14);
                break;

            case (R.id.d_30):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series30);
                break;

            case (R.id.d_60):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series60);
                break;

            case (R.id.d_90):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(series90);
                break;

            case (R.id.alltime):
                cubicValueLineChart.clearChart();
                cubicValueLineChart.addSeries(seriesAll);
                break;
        }
    }

    private void fillInfoFromJSON() {
        if (getView() != null) {
            coinPrice = getView().findViewById(R.id.coin_price);
            coinPrice.setText(myGlobalsFunctions.commaSeperateInteger2(price, true));
            coinAvailSupply = getView().findViewById(R.id.coin_avail_supply);
            coinAvailSupply.setText(myGlobalsFunctions.commaSeperateInteger(avlsup));
            coinCap = getView().findViewById(R.id.coin_cap);
            coinCap.setText  (myGlobalsFunctions.commaSeperateInteger2(cap, true));
            coinRank = getView().findViewById(R.id.coin_rank);
            coinRank.setText(rank);
            coinChange = getView().findViewById(R.id.coin_change);
            coinChange.setText(change);
            if (change.charAt(0) == '-') {
                coinChange.setTextColor(getResources().getColor(R.color.valueNegative));
            } else {
                coinChange.setTextColor(getResources().getColor(R.color.valuePositive));
            }
            coinLstUpdate = getView().findViewById(R.id.coin_lst_update);
            coinLstUpdate.setText(myGlobalsFunctions.getEpochToNormalDateString(lstupd));
            highVal = getView().findViewById(R.id.high);
            highVal.setText(myGlobalsFunctions.commaSeperateInteger(high, true));
            lowVal = getView().findViewById(R.id.low);
            lowVal.setText(myGlobalsFunctions.commaSeperateInteger(low, true));
            cubicValueLineChart.addSeries(series24);
        }
    }

    public void fillGraphValues(String highLowJson24, String highLowJson7, String highLowJson14, String highLowJson30,
                                String highLowJson60, String highLowJson90, String highLowJsonAll) throws JSONException{
        if (highLowJson24!=null && !Objects.equals(highLowJson24, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson24);
            JSONArray newRef = highLowObj.optJSONArray("stats");
            float min = Float.parseFloat( newRef.optJSONArray(0).optString(1) ), max = Float.parseFloat( newRef.optJSONArray(0).optString(1) );
            for (int i = 0; i < newRef.length(); i++) {
                Float temp = Float.parseFloat( newRef.optJSONArray(i).optString(1) );
                if (temp > max) max = temp;
                if (temp < min) min = temp;
            }
            high = Float.toString(max);
            low = Float.toString(min);

            for (int i = 0; i < newRef.length(); ++i) {
                series24.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJson7!=null && !Objects.equals(highLowJson7, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson7);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                series7.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJson14!=null && !Objects.equals(highLowJson14, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson14);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                series14.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJson30!=null && !Objects.equals(highLowJson30, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson30);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                series30.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJson60!=null && !Objects.equals(highLowJson60, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson60);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                series60.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJson90!=null && !Objects.equals(highLowJson90, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson90);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                series90.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
        if (highLowJsonAll!=null && !Objects.equals(highLowJsonAll, "")) {
            JSONObject highLowObj = new JSONObject(highLowJsonAll);
            JSONArray newRef = highLowObj.optJSONArray("stats");

            for (int i = 0; i < newRef.length(); ++i) {
                seriesAll.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
    }

    public void setVals(String finalJson, String highLowJson24, String highLowJson7, String highLowJson14, String highLowJson30,
                        String highLowJson60, String highLowJson90, String highLowJsonAll) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        JSONObject parentObject = jarr.getJSONObject(0);
        price = parentObject.getString("price_" + currency.toLowerCase());
        change = parentObject.getString("percent_change_24h");
        if (change.equals("null")) change = parentObject.getString("percent_change_1h");
        if (change.equals("null")) change = parentObject.getString("percent_change_7d");
        if (change.equals("null")) change = "0.0";
        rank = parentObject.getString("rank");
        cap = parentObject.getString("market_cap_" + currency.toLowerCase());
        avlsup = parentObject.getString("available_supply");
        lstupd = parentObject.getString("last_updated");

        float changeNum = Float.parseFloat(price) - (Float.parseFloat(price) / (1 + ((float) 0.01 * Float.parseFloat(change))));
        change = myGlobalsFunctions.commaSeperateInteger2(String.valueOf(changeNum), true) + " (" + change + "%)";
        fillGraphValues(highLowJson24, highLowJson7, highLowJson14, highLowJson30, highLowJson60, highLowJson90, highLowJsonAll);
    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String finalJson = myGlobalsFunctions.retieveStringFromFile(cryptoID,getString(R.string.crypto_info_dir));
                String highLowJson24 = "";
                String highLowJson7 = "";
                String highLowJson14 = "";
                String highLowJson30 = "";
                String highLowJson60 = "";
                String highLowJson90 = "";
                String highLowJsonAll = "";
                if (finalJson != null) {
                    setVals(finalJson, highLowJson24, highLowJson7, highLowJson14, highLowJson30, highLowJson60, highLowJson90, highLowJsonAll);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (myGlobalsFunctions.isNetworkConnected()) {
                    String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                    String highLowJson24 = myGlobalsFunctions.fetchJSONasString(params[2]);
                    String highLowJson7 = myGlobalsFunctions.fetchJSONasString(params[3]);
                    String highLowJson14 = myGlobalsFunctions.fetchJSONasString(params[4]);
                    String highLowJson30 = myGlobalsFunctions.fetchJSONasString(params[5]);
                    String highLowJson60 = myGlobalsFunctions.fetchJSONasString(params[6]);
                    String highLowJson90 = myGlobalsFunctions.fetchJSONasString(params[7]);
                    String highLowJsonAll = myGlobalsFunctions.fetchJSONasString(params[8]);
                    myGlobalsFunctions.storeStringToFile(cryptoID, getString(R.string.crypto_info_dir), finalJson);
                    myGlobalsFunctions.convertImageURLtoBitmap(params[1], Boolean.TRUE);
                    setVals(finalJson, highLowJson24, highLowJson7, highLowJson14, highLowJson30, highLowJson60, highLowJson90, highLowJsonAll);
                    return price;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            fillInfoFromJSON();
        }
    }
}
