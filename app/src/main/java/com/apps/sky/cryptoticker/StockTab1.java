package com.apps.sky.cryptoticker;

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

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockTab1 extends Fragment {

    private View rootView;
    private static String name, price, change, rank, cap, avlsup, totsup, lstupd;
    private TextView coinName, coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinTotSupply, coinLstUpdate;
    private String url;
    public String crypto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_tab_1, container, false);
        }
        new JSONTask().execute(url);

        return rootView;
    }

    private void fillInfoFromJSON() {

        coinName = (TextView) getView().findViewById(R.id.coinName);
        coinName.setText(name);
        coinPrice = (TextView) getView().findViewById(R.id.coinPrice);
        coinPrice.setText(price);
        coinAvailSupply = (TextView) getView().findViewById(R.id.coinAvailSupply);
        coinAvailSupply.setText(avlsup);
        coinCap = (TextView) getView().findViewById(R.id.coinCap);
        coinCap.setText(cap);
        coinLstUpdate = (TextView) getView().findViewById(R.id.coinLstUpdate);
        coinLstUpdate.setText(lstupd);
        coinRank = (TextView) getView().findViewById(R.id.coinRank);
        coinRank.setText(rank);
        coinTotSupply = (TextView) getView().findViewById(R.id.coinTotSupply);
        coinTotSupply.setText(totsup);
        coinChange = (TextView) getView().findViewById(R.id.coinChange);
        coinChange.setText(change);
//        if(Integer.parseInt(change)<0){
//            coinChange.setTextColor(Color.RED);
//        }
//        else {
//            coinChange.setTextColor(Color.GREEN);
//        }
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
//            coinName = (TextView) findViewById(R.id.coinName);
//            coinName.setText(name);
            fillInfoFromJSON();
        }
    }
}
