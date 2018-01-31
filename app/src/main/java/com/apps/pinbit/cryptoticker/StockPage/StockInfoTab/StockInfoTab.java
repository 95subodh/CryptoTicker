package com.apps.pinbit.cryptoticker.StockPage.StockInfoTab;

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

import com.apps.pinbit.cryptoticker.Global.Constants;
import com.apps.pinbit.cryptoticker.Global.ConstantsCrypto;
import com.apps.pinbit.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.pinbit.cryptoticker.R;
import com.github.ybq.android.spinkit.SpinKitView;

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
    TextView coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinLstUpdate, highVal, lowVal, text24, text7, text30, text90, text180, textAll;
    String url, exchangeRateURL, iconUrl, highLowUrl24, highLowUrl7, highLowUrl30, highLowUrl90, highLowUrl180, highLowUrlAll, currency;
    public String cryptoID;
    double conversion = 1.0;
    MyGlobalsFunctions myGlobalsFunctions;
    SharedPreferences sharedPreferences;
    ValueLineChart cubicValueLineChart;
    ValueLineSeries series24, series7, series30, series90, series180, seriesAll;
    ArrayList<TextView> texts;
    ArrayList<Integer> views;
    String currentSeries;
    SpinKitView spinKit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_info_tab, container, false);
        }

        rootView.findViewById(R.id.h_24).setOnClickListener(this);
        rootView.findViewById(R.id.d_7).setOnClickListener(this);
        rootView.findViewById(R.id.d_30).setOnClickListener(this);
        rootView.findViewById(R.id.d_90).setOnClickListener(this);
        rootView.findViewById(R.id.d_180).setOnClickListener(this);
        rootView.findViewById(R.id.alltime).setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("com.apps.pinbit.cryptoticker", Context.MODE_PRIVATE);

        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        if (currency.equals("")) currency = Constants.DEFAULT_CURRENCY;

        exchangeRateURL = "http://api.fixer.io/latest?base=USD";

        url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
        iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/" + cryptoID + ".png";
        highLowUrl24 = "http://coincap.io/history/1day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
        highLowUrl7 = "http://coincap.io/history/7day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
        highLowUrl30 = "http://coincap.io/history/30day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
        highLowUrl90 = "http://coincap.io/history/90day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
        highLowUrl180 = "http://coincap.io/history/180day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
        highLowUrlAll = "http://coincap.io/history/365day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];

        myGlobalsFunctions = new MyGlobalsFunctions(rootView.getContext());
        spinKit = rootView.findViewById(R.id.spin_kit_stock_info);

        cubicValueLineChart = rootView.findViewById(R.id.cubic_line_chart);
        cubicValueLineChart.setUseDynamicScaling(true);
        text24 = rootView.findViewById(R.id.h_24_textview);
        text24.setTypeface(null, Typeface.BOLD);
        text24.setTextSize(16);
        text7 = rootView.findViewById(R.id.d_7_textview);
        text30 = rootView.findViewById(R.id.d_30_textview);
        text90 = rootView.findViewById(R.id.d_90_textview);
        text180 = rootView.findViewById(R.id.d_180_textview);
        textAll = rootView.findViewById(R.id.d_alltime_textview);

        texts = new ArrayList<>();
        texts.add(text24);
        texts.add(text7);
        texts.add(text30);
        texts.add(text90);
        texts.add(text180);
        texts.add(textAll);

        views = new ArrayList<>();
        views.add(R.id.h_24);
        views.add(R.id.d_7);
        views.add(R.id.d_30);
        views.add(R.id.d_90);
        views.add(R.id.d_180);
        views.add(R.id.alltime);

        series24 = new ValueLineSeries();
        series24.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        series7 = new ValueLineSeries();
        series7.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        series30 = new ValueLineSeries();
        series30.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        series90 = new ValueLineSeries();
        series90.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        series180 = new ValueLineSeries();
        series180.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        seriesAll = new ValueLineSeries();
        seriesAll.setColor(getContext().getResources().getColor(R.color.colorPrimary));
        if (myGlobalsFunctions.isNetworkConnected()) {
            new JSONTask().execute(url, iconUrl);
            spinKit.setVisibility(View.VISIBLE);
            new JSONTaskHighLow().execute(highLowUrl24);
            currentSeries = "1";
        }
        return rootView;
    }

    @Override
    public void onClick(View view) {

        for (int i = 0; i < 6; ++i) {
            if (view.getId() == views.get(i)) {
                texts.get(i).setTypeface(null, Typeface.BOLD);
                texts.get(i).setTextSize(16);
            } else {
                texts.get(i).setTypeface(null, Typeface.NORMAL);
                texts.get(i).setTextSize(12);
            }
        }

        cubicValueLineChart.clearChart();
        spinKit.setVisibility(View.VISIBLE);

        switch (view.getId()) {
            case (R.id.h_24):
                currentSeries = "1";
                if (series24.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrl24, "1");
                else
                    cubicValueLineChart.addSeries(series24);
                break;

            case (R.id.d_7):
                currentSeries = "2";
                if (series7.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrl7, "2");
                else
                    cubicValueLineChart.addSeries(series7);
                break;

            case (R.id.d_30):
                currentSeries = "3";
                if (series30.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrl30, "3");
                else
                    cubicValueLineChart.addSeries(series30);
                break;

            case (R.id.d_90):
                currentSeries = "4";
                if (series90.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrl90, "4");
                else
                    cubicValueLineChart.addSeries(series90);
                break;

            case (R.id.d_180):
                currentSeries = "5";
                if (series180.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrl180, "5");
                else
                    cubicValueLineChart.addSeries(series180);
                break;

            case (R.id.alltime):
                currentSeries = "6";
                if (seriesAll.getSeries().size() == 0)
                    new JSONTaskGraph().execute(highLowUrlAll, "6");
                else
                    cubicValueLineChart.addSeries(seriesAll);
                break;
        }
        spinKit.setVisibility(View.INVISIBLE);
    }

    private void fillInfoFromJSONHighLow() {
        if (getView() != null) {
            highVal = getView().findViewById(R.id.high);
            highVal.setText(myGlobalsFunctions.floatFormatter(high, true, true, true));
            lowVal = getView().findViewById(R.id.low);
            lowVal.setText(myGlobalsFunctions.floatFormatter(low, true, true, true));
            cubicValueLineChart.addSeries(series24);
        }
    }

    private void fillInfoFromJSON() {
        if (getView() != null) {
            coinPrice = getView().findViewById(R.id.coin_price);
            coinPrice.setText(myGlobalsFunctions.floatFormatter(price, true, true, false));
            coinAvailSupply = getView().findViewById(R.id.coin_avail_supply);
            coinAvailSupply.setText(myGlobalsFunctions.floatFormatter(avlsup, false, true, false));
            coinCap = getView().findViewById(R.id.coin_cap);
            coinCap.setText(myGlobalsFunctions.floatFormatter(cap, true, true, true));
            coinRank = getView().findViewById(R.id.coin_rank);
            coinRank.setText(rank);
            coinChange = getView().findViewById(R.id.coin_change);
            coinChange.setText(change);
            if (change.length() > 1 && change.charAt(1) == '-') {
                coinChange.setTextColor(getResources().getColor(R.color.valueNegative));
            } else {
                coinChange.setTextColor(getResources().getColor(R.color.valuePositive));
            }
            coinLstUpdate = getView().findViewById(R.id.coin_lst_update);
            coinLstUpdate.setText(myGlobalsFunctions.getEpochToNormalDateAndTimeString(lstupd));
        }
    }

    public void setValsGraph(String highLowJson) throws JSONException {

        if (highLowJson != null && !Objects.equals(highLowJson, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson);
            JSONArray newRef = highLowObj.optJSONArray("price");

            switch (currentSeries) {
                case "1":
                    for (int i = 0; i < newRef.length(); ++i) {
                        series24.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalTimeString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(series24);
                    break;

                case "2":
                    for (int i = 0; i < newRef.length(); ++i) {
                        series7.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(series7);
                    break;

                case "3":
                    for (int i = 0; i < newRef.length(); ++i) {
                        series30.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(series30);
                    break;

                case "4":
                    for (int i = 0; i < newRef.length(); ++i) {
                        series90.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(series90);
                    break;

                case "5":
                    for (int i = 0; i < newRef.length(); ++i) {
                        series180.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(series180);
                    break;

                case "6":
                    for (int i = 0; i < newRef.length(); ++i) {
                        seriesAll.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalYearString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
                    }
                    cubicValueLineChart.clearChart();
                    cubicValueLineChart.addSeries(seriesAll);
                    break;
            }
        }
    }

    public void setValsHighLow(String highLowJson24) throws JSONException {
        if (highLowJson24 != null && !Objects.equals(highLowJson24, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson24);
            JSONArray newRef = highLowObj.optJSONArray("price");
            float min = Float.parseFloat(newRef.optJSONArray(0).optString(1));
            float max = Float.parseFloat(newRef.optJSONArray(0).optString(1));
            for (int i = 0; i < newRef.length(); i++) {
                Float temp = Float.parseFloat(newRef.optJSONArray(i).optString(1));
                if (temp > max) max = temp;
                if (temp < min) min = temp;
            }
            high = Float.toString(max * (float) conversion);
            low = Float.toString(min * (float) conversion);
            for (int i = 0; i < newRef.length(); ++i) {
                series24.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalTimeString(newRef.optJSONArray(i).optString(0)), Float.parseFloat(newRef.optJSONArray(i).optString(1)) * (float) conversion));
            }
        }
    }

    public void setVals(String finalJson) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        JSONObject parentObject = jarr.getJSONObject(0);
        price = myGlobalsFunctions.nullCheck(parentObject.getString("price_" + currency.toLowerCase()));
        change = myGlobalsFunctions.nullCheck(parentObject.getString("percent_change_24h"));
        rank = myGlobalsFunctions.nullCheck(parentObject.getString("rank"));
        cap = myGlobalsFunctions.nullCheck(parentObject.getString("market_cap_" + currency.toLowerCase()));
        avlsup = myGlobalsFunctions.nullCheck(parentObject.getString("available_supply"));
        lstupd = myGlobalsFunctions.nullCheck(parentObject.getString("last_updated"));

        if (!"-".equals(change) && !"-".equals(price)) {
            float changeNum = Float.parseFloat(price) - (Float.parseFloat(price) / (1 + ((float) 0.01 * Float.parseFloat(change))));
            change = myGlobalsFunctions.floatFormatter(String.valueOf(changeNum), true, true, true) + " (" + change + "%)";
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String finalJson = myGlobalsFunctions.retieveStringFromFile(cryptoID, getString(R.string.crypto_info_dir));
                if (finalJson != null) {
                    setVals(finalJson);
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
                    myGlobalsFunctions.convertImageURLtoBitmap(params[1], Boolean.TRUE);
                    if (finalJson != null)
                        setVals(finalJson);
                    String exchangeRateJSON = myGlobalsFunctions.fetchJSONasString(exchangeRateURL);
                    JSONObject jsonObject = new JSONObject(exchangeRateJSON);
                    JSONObject currExRate = jsonObject.getJSONObject("rates");
                    conversion = 1.0;
                    if (!currency.equals("USD"))
                        conversion = currExRate.getDouble(currency.toUpperCase());
                    return finalJson;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            if (result != null)
                fillInfoFromJSON();
        }
    }

    public class JSONTaskHighLow extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (myGlobalsFunctions.isNetworkConnected()) {
                    String highLowJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                    if (highLowJson != null) {
                        setValsHighLow(highLowJson);
                    }
                    String exchangeRateJSON = myGlobalsFunctions.fetchJSONasString(exchangeRateURL);
                    JSONObject jsonObject = new JSONObject(exchangeRateJSON);
                    JSONObject currExRate = jsonObject.getJSONObject("rates");
                    conversion = 1.0;
                    if (!currency.equals("USD"))
                        conversion = currExRate.getDouble(currency.toUpperCase());
                    return highLowJson;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            if (result != null) {
                fillInfoFromJSONHighLow();
                spinKit.setVisibility(View.INVISIBLE);
            }
        }
    }

    public class JSONTaskGraph extends AsyncTask<String, String, String> {
        String num;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (myGlobalsFunctions.isNetworkConnected()) {
                    String highLowJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                    num = params[1];
                    return highLowJson;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            try {
                if (result != null && num.equals(currentSeries)) {
                    setValsGraph(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spinKit.setVisibility(View.INVISIBLE);
        }
    }
}
