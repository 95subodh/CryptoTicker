package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class WatchlistTab extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String url;
    public String cryptoID;
    MyGlobalsFunctions myGlobalsFunctions;
    public ArrayList<String> items;
    ArrayList<WatchlistObject> watchlistArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.watchlist_tab, container, false);
        }

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        myGlobalsFunctions = new MyGlobalsFunctions(getContext());

        items = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_watchlist_file), getString(R.string.crypto_watchlist_dir));
        watchlistArray = new ArrayList<>();
        if (myGlobalsFunctions.isNetworkConnected()) {
            for (int i = 0; i < items.size(); ++i) {
                cryptoID = items.get(i);
                url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=INR";
                String imageUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/"+cryptoID+".png";
                String highLowUrl = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/usd/24_hours.json";
                new JSONTask().execute(url, imageUrl, highLowUrl);
            }
        }

        adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public void setVals(String finalJson, String imageUrl, String highLowJson) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);
        WatchlistObject currency_details = new WatchlistObject();
        if (highLowJson!=null && !Objects.equals(highLowJson, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson);
            JSONArray newRef = highLowObj.optJSONArray("stats");
            float min = Float.parseFloat( newRef.optJSONArray(0).optString(1) ), max = Float.parseFloat( newRef.optJSONArray(0).optString(1) );
            for (int i = 0; i < newRef.length(); i++) {
                Float x = Float.parseFloat( newRef.optJSONArray(i).optString(1) );
                if (x > max) max = x;
                if (x < min) min = x;
            }
            currency_details.setMinDayPrice(Float.toString(min));
            currency_details.setMaxDayPrice(Float.toString(max));
        }
        JSONObject parentObject = jarr.getJSONObject(0);
        currency_details.setTitle(parentObject.getString("name"));
        currency_details.setCurrentPrice(parentObject.getString("price_inr"));
        String change = parentObject.getString("percent_change_24h");
        currency_details.setChange(change);
        currency_details.setCryptoID(parentObject.getString("id"));
        currency_details.setIcon(imageUrl);
        watchlistArray.add(currency_details);
        if (change.charAt(0) == '-') currency_details.setChangeColor(false);
        else currency_details.setChangeColor(true);
    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                String highLowJson = myGlobalsFunctions.fetchJSONasString(params[2]);
//                myGlobalsFunctions.storeStringToFile(cryptoID,getString(R.string.crypto_info_dir),finalJson);
                setVals(finalJson, params[1], highLowJson);
                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
            recyclerView.setAdapter(adapter);
        }
    }
}