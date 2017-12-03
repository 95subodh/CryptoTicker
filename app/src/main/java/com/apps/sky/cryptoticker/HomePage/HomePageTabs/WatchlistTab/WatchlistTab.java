package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class WatchlistTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String url, currency;
    MyGlobalsFunctions myGlobalsFunctions;
    public ArrayList<String> items;
    ArrayList<WatchlistObject> watchlistArray;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKit;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);
        myGlobalsFunctions = new MyGlobalsFunctions(getContext());
        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        if (currency.equals("")) currency = Constants.DEFAULT_CURRENCY;

        items = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_watchlist_file), getString(R.string.crypto_watchlist_dir));
        watchlistArray = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.watchlist_tab, container, false);
        }
        spinKit = rootView.findViewById(R.id.spin_kit_watchlist);
        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadView();
            }
        });

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onRefresh() {
        loadView();
    }

    private void loadView() {

        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        swipeRefreshLayout.setRefreshing(true);
        spinKit.setVisibility(View.VISIBLE);
        watchlistArray = new ArrayList<>();
        adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
        recyclerView.setAdapter(adapter);
        if (myGlobalsFunctions.isNetworkConnected()) {
            count = 0;
            for (int i = 0; i < items.size(); ++i) {
                String cryptoID = items.get(i);
                url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
                String imageUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/"+cryptoID+".png";
                String highLowUrl = "https://www.coingecko.com/en/price_charts/" + cryptoID + "/" + currency.toLowerCase() + "/24_hours.json";
                new JSONTask().execute(url, imageUrl, highLowUrl, cryptoID);
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setVals(String finalJson, String iconUrl, String highLowJson) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        WatchlistObject currency_details = new WatchlistObject();
        currency_details.setContext(getContext());

        JSONObject parentObject = jarr.getJSONObject(0);
        currency_details.setTitle(myGlobalsFunctions.nullCheck(parentObject.getString("name")));

        String price = parentObject.getString("price_" + currency.toLowerCase());
        currency_details.setCurrentPrice(myGlobalsFunctions.nullCheck(price));

        if (highLowJson!=null && !Objects.equals(highLowJson, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson);
            JSONArray newRef = highLowObj.optJSONArray("stats");
            float min = Float.parseFloat( newRef.optJSONArray(0).optString(1) ), max = Float.parseFloat( newRef.optJSONArray(0).optString(1) );
            for (int i = 0; i < newRef.length(); i++) {
                Float temp = Float.parseFloat( newRef.optJSONArray(i).optString(1) );
                if (temp > max) max = temp;
                if (temp < min) min = temp;
            }
            currency_details.setMinDayPrice(myGlobalsFunctions.nullCheck(Float.toString(Math.min(min, Float.parseFloat(price)))));
            currency_details.setMaxDayPrice(myGlobalsFunctions.nullCheck(Float.toString(Math.max(max, Float.parseFloat(price)))));
        }

        String change = myGlobalsFunctions.nullCheck( parentObject.getString("percent_change_24h") );
        if (change.equals("-")) {
            currency_details.setChange(change);
        }
        else {
            float changeNum = Float.parseFloat(price) - (Float.parseFloat(price) / (1 + ((float) 0.01 * Float.parseFloat(change))));
            currency_details.setChange(myGlobalsFunctions.commaSeperateIntegerMinimal(String.valueOf(changeNum), true) + " (" + change + "%)");
        }

        if (change.length()>1) {
            if (change.charAt(0) == '-') currency_details.setChangeColor(false);
            else currency_details.setChangeColor(true);
        }

        currency_details.setCryptoID( myGlobalsFunctions.nullCheck(parentObject.getString("id")) );
        currency_details.setIcon(iconUrl);
        watchlistArray.add(currency_details);
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
//                myGlobalsFunctions.storeStringToFile(params[3], getString(R.string.crypto_info_dir), finalJson);
                if (finalJson!=null) {
                    setVals(finalJson, params[1], highLowJson);
                }
                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            count++;
            if (count==items.size()) {
                adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
                recyclerView.setAdapter(adapter);
                spinKit.setVisibility(View.INVISIBLE);
            }
        }
    }
}