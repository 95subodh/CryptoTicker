package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.pinbit.cryptoticker.Global.Constants;
import com.apps.pinbit.cryptoticker.Global.ConstantsCrypto;
import com.apps.pinbit.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.pinbit.cryptoticker.R;
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
    String url, exchangeRateURL, currency;
    double conversion = 1.0;
    MyGlobalsFunctions myGlobalsFunctions;
    public ArrayList<String> items;
    ArrayList<WatchlistObject> watchlistArray, watchlistArrayTemp;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKit;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("com.apps.pinbit.cryptoticker", Context.MODE_PRIVATE);
        myGlobalsFunctions = new MyGlobalsFunctions(getContext());

        items = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_watchlist_file), getString(R.string.crypto_watchlist_dir));
        watchlistArray = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            if (items == null || items.size() == 0) {
                rootView = inflater.inflate(R.layout.empty_view_layout, container, false);
                ((TextView) rootView.findViewById(R.id.empty_view_text)).setText("Your watchlist is empty");
            } else
                rootView = inflater.inflate(R.layout.watchlist_tab, container, false);
        }

        if (items != null && items.size() > 0) {
            spinKit = rootView.findViewById(R.id.spin_kit_watchlist);

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
        }
        return rootView;
    }

    @Override
    public void onRefresh() {
        loadView();
    }

    private void loadView() {
        swipeRefreshLayout.setRefreshing(true);
        if (items != null && items.size() > 0) {
            currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
            if (currency.equals("")) currency = Constants.DEFAULT_CURRENCY;
            exchangeRateURL = "https://free.currencyconverterapi.com/api/v4/convert?q=USD_" + currency + "&compact=y";
            spinKit.setVisibility(View.VISIBLE);
            watchlistArray = new ArrayList<>();
            adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
            recyclerView.setAdapter(adapter);
            if (myGlobalsFunctions.isNetworkConnected()) {
                count = 0;
                watchlistArrayTemp = new ArrayList<>();
                for (int i = 0; i < items.size(); ++i) {
                    String cryptoID = items.get(i);

                    WatchlistObject x = new WatchlistObject();
                    x.setCryptoID(cryptoID);
                    watchlistArrayTemp.add(x);
                }
                for (int i = 0; i < items.size(); ++i) {
                    String cryptoID = items.get(i);
                    url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
                    String imageUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/" + cryptoID + ".png";
                    String highLowUrl = "http://coincap.io/history/1day/" + ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[1];
                    new JSONTask().execute(url, imageUrl, highLowUrl, cryptoID);
                }
            }
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ((RelativeLayout) rootView.findViewById(R.id.main_view)).addView(inflater.inflate(R.layout.empty_view_layout, ((ViewGroup) getView().getParent()), false));
            ((TextView) rootView.findViewById(R.id.empty_view_text)).setText("Your watchlist is empty");
            rootView.refreshDrawableState();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setVals(String finalJson, String iconUrl, String highLowJson, String cryptoID) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        int cardPosition = 0;
        for (int i = 0; i < watchlistArrayTemp.size(); ++i) {
            if (watchlistArrayTemp.get(i).getCryptoID().equals(cryptoID)) {
                cardPosition = i;
                break;
            }
        }

        watchlistArrayTemp.get(cardPosition).setContext(getContext());

        JSONObject parentObject = jarr.getJSONObject(0);
        watchlistArrayTemp.get(cardPosition).setTitle(myGlobalsFunctions.nullCheck(parentObject.getString("name")));

        String price = parentObject.getString("price_" + currency.toLowerCase());
        watchlistArrayTemp.get(cardPosition).setCurrentPrice(myGlobalsFunctions.nullCheck(price));

        if (highLowJson != null && !Objects.equals(highLowJson, "")) {
            JSONObject highLowObj = new JSONObject(highLowJson);
            JSONArray newRef = highLowObj.optJSONArray("price");
            float min = Float.parseFloat(newRef.optJSONArray(0).optString(1)), max = Float.parseFloat(newRef.optJSONArray(0).optString(1));
            for (int i = 0; i < newRef.length(); i++) {
                Float temp = Float.parseFloat(newRef.optJSONArray(i).optString(1));
                if (temp > max) max = temp;
                if (temp < min) min = temp;
            }
            watchlistArrayTemp.get(cardPosition).setMinDayPrice(myGlobalsFunctions.nullCheck(Float.toString(Math.min(min * (float) conversion, Float.parseFloat(price)))));
            watchlistArrayTemp.get(cardPosition).setMaxDayPrice(myGlobalsFunctions.nullCheck(Float.toString(Math.max(max * (float) conversion, Float.parseFloat(price)))));
        }

        String change = myGlobalsFunctions.nullCheck(parentObject.getString("percent_change_24h"));
        if (change.equals("-")) {
            watchlistArrayTemp.get(cardPosition).setChange(change);
        } else {
            float changeNum = Float.parseFloat(price) - (Float.parseFloat(price) / (1 + ((float) 0.01 * Float.parseFloat(change))));
            watchlistArrayTemp.get(cardPosition).setChange(myGlobalsFunctions.floatFormatter(String.valueOf(changeNum), true, true, true) + " (" + change + "%)");
        }

        if (change.length() > 1) {
            if (change.charAt(0) == '-') watchlistArrayTemp.get(cardPosition).setChangeColor(false);
            else watchlistArrayTemp.get(cardPosition).setChangeColor(true);
        }

        watchlistArrayTemp.get(cardPosition).setCryptoID(myGlobalsFunctions.nullCheck(parentObject.getString("id")));
        watchlistArrayTemp.get(cardPosition).setIcon(iconUrl);
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                String highLowJson = myGlobalsFunctions.fetchJSONasString(params[2]);
                String exchangeRateJSON = myGlobalsFunctions.fetchJSONasString(exchangeRateURL);
                JSONObject jsonObject = new JSONObject(exchangeRateJSON);
                JSONObject currExRate = jsonObject.getJSONObject("USD_" + currency);
                conversion = currExRate.getDouble("val");
//                myGlobalsFunctions.storeStringToFile(params[3], getString(R.string.crypto_info_dir), finalJson);
                if (finalJson != null) {
                    setVals(finalJson, params[1], highLowJson, params[3]);
                }
                Thread.sleep(10);
                return finalJson;

            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            count++;
            if (count == items.size()) {
                watchlistArray.addAll(watchlistArrayTemp);
                adapter = new WatchlistRecyclerViewAdapter(watchlistArray, WatchlistTab.this);
                recyclerView.setAdapter(adapter);
                spinKit.setVisibility(View.INVISIBLE);
            }
        }
    }
}