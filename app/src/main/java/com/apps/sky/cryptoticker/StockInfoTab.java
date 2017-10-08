package com.apps.sky.cryptoticker;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockInfoTab extends Fragment {

    private View rootView;
    private static String name, price, change, rank, cap, avlsup, totsup, lstupd;
    private TextView coinName, coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinTotSupply, coinLstUpdate;
    private String url;
    public String crypto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_tab_1, container, false);
        }

        url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
        new JSONTask().execute(url);

        return rootView;
    }

    private String commaSeperateInteger(String num){
        return NumberFormat.getNumberInstance(Locale.US).format(Float.valueOf(num));
    }

    private void fillInfoFromJSON() {

        coinName = getView().findViewById(R.id.coinName);
        coinName.setText(name);
        coinPrice = getView().findViewById(R.id.coinPrice);
        coinPrice.setText(commaSeperateInteger(price));
        coinAvailSupply = getView().findViewById(R.id.coinAvailSupply);
        coinAvailSupply.setText(commaSeperateInteger(avlsup));
        coinCap = getView().findViewById(R.id.coinCap);
        coinCap.setText(commaSeperateInteger(cap));
        coinRank = getView().findViewById(R.id.coinRank);
        coinRank.setText(rank);
        coinTotSupply = getView().findViewById(R.id.coinTotSupply);
        coinTotSupply.setText(commaSeperateInteger(totsup));
        coinChange = getView().findViewById(R.id.coinChange);
        coinChange.setText(change);
        if (change.charAt(0) == '-') {
            coinChange.setTextColor(Color.RED);
        }
        else {
            coinChange.setTextColor(Color.parseColor("#ff99cc00"));
        }

        coinLstUpdate = getView().findViewById(R.id.coinLstUpdate);
        String lastUpdTime = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (Integer.valueOf(lstupd)*1000));
//        Date lastUpdTime = new Date(Long.parseLong(lstupd));
        coinLstUpdate.setText(lastUpdTime);

    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONArray jarr = new JSONArray(finalJson);

                JSONObject parentObject = jarr.getJSONObject(0);
                name = parentObject.getString("name");
                price = parentObject.getString("price_inr");
                change = parentObject.getString("percent_change_24h");
                rank = parentObject.getString("rank");
                cap = parentObject.getString("market_cap_inr");
                avlsup = parentObject.getString("available_supply");
                totsup = parentObject.getString("total_supply");
                lstupd = parentObject.getString("last_updated");
                return price;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
