package com.apps.sky.cryptoticker.StockPage.StockInfoTab;

import android.content.Context;
import android.content.SharedPreferences;
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
import java.util.Objects;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockInfoTab extends Fragment {

    private View rootView;
    private String price, change, rank, cap, avlsup, lstupd, high, low;
    TextView coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinLstUpdate, highVal, lowVal;
    String url, currency;
    public String cryptoID;
    MyGlobalsFunctions myGlobalsFunctions;
    SharedPreferences sharedPreferences;
    ValueLineChart cubicValueLineChart;
    ValueLineSeries series;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_info_tab, container, false);
        }
        sharedPreferences = getContext().getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);

        currency = sharedPreferences.getString(Constants.CURRENT_CURRENCY, "");
        if (currency.equals("")) currency = "INR";

        url = "https://api.coinmarketcap.com/v1/ticker/"+cryptoID+"/?convert=" + currency.toUpperCase();
        String iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/"+cryptoID+".png";
        String highLowUrl = "https://www.coingecko.com/en/chart/" + cryptoID + "/" + currency.toLowerCase() + ".json";

        myGlobalsFunctions = new MyGlobalsFunctions(rootView.getContext());

        cubicValueLineChart = rootView.findViewById(R.id.cubic_line_chart);
        series = new ValueLineSeries();
        series.setColor(getContext().getResources().getColor(R.color.colorAccentLight));

        new JSONTask().execute(url, iconUrl, highLowUrl);
        return rootView;
    }

    private void fillInfoFromJSON() {
        if (getView() != null) {
            coinPrice = getView().findViewById(R.id.coin_price);
            coinPrice.setText(myGlobalsFunctions.commaSeperateInteger2(price));
            coinAvailSupply = getView().findViewById(R.id.coin_avail_supply);
            coinAvailSupply.setText(myGlobalsFunctions.commaSeperateInteger(avlsup));
            coinCap = getView().findViewById(R.id.coin_cap);
            coinCap.setText(myGlobalsFunctions.commaSeperateInteger2(cap));
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
            highVal.setText(high);
            lowVal = getView().findViewById(R.id.low);
            lowVal.setText(low);
            cubicValueLineChart.addSeries(series);
        }
    }

    public void setVals(String finalJson, String highLowJson) throws JSONException {
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
        change = myGlobalsFunctions.commaSeperateInteger2(String.valueOf(changeNum)) + " (" + change + "%)";

        if (highLowJson!=null && !Objects.equals(highLowJson, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson);
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
                series.addPoint(new ValueLinePoint(myGlobalsFunctions.getEpochToNormalDateString(newRef.optJSONArray(i).optString(0).toString()), Float.parseFloat(newRef.optJSONArray(i).optString(1))));
            }
        }
    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String finalJson = myGlobalsFunctions.retieveStringFromFile(cryptoID,getString(R.string.crypto_info_dir));
                String highLowJson = "";
                if (finalJson != null) {
                    setVals(finalJson, highLowJson);
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
                    String highLowJson = myGlobalsFunctions.fetchJSONasString(params[2]);
                    myGlobalsFunctions.storeStringToFile(cryptoID, getString(R.string.crypto_info_dir), finalJson);
                    myGlobalsFunctions.convertImageURLtoBitmap(params[1], Boolean.TRUE);
                    setVals(finalJson, highLowJson);
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
