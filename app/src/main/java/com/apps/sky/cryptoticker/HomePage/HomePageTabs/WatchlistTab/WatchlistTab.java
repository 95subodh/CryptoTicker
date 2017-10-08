package com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WatchlistTab extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String url;
    public String crypto;
    MyGlobalsFunctions myGlobalsFunctions;
    ArrayList<String> items;
    ArrayList<WatchlistObject> watchlistArray = new ArrayList<WatchlistObject>();

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WatchlistRecyclerViewAdapter(watchlistArray);
        recyclerView.setAdapter(adapter);
        myGlobalsFunctions = new MyGlobalsFunctions(getContext());

        items = new ArrayList<>();
        items.add("bitcoin");
        items.add("atc-coin");
        items.add("ripple");
        items.add("litecoin");
        items.add("ethereum");
        items.add("dash");
        items.add("bitconnect");
        items.add("lisk");
        items.add("tether");
        items.add("waves");
        items.add("bitshares");
        items.add("eos");
        items.add("metal");
        items.add("nexus");
        items.add("syscoin");

        for (int i = 0; i < 10; ++i) {

            //--------retrieve values here instead of this line-------
            crypto = items.get(i);

            url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
            new JSONTask().execute(url);
        }

        return rootView;
    }

    public void setVals(String finalJson) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        JSONObject parentObject = jarr.getJSONObject(0);
        WatchlistObject currency_details = new WatchlistObject();
        currency_details.setTitle(parentObject.getString("name"));
        currency_details.setCurrentPrice("$" + parentObject.getString("price_inr"));
        currency_details.setChange(parentObject.getString("percent_change_24h"));
        watchlistArray.add(currency_details);
    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String finalJson = myGlobalsFunctions.retieveStringFromFile(crypto,getString(R.string.crypto_info_dir));
                if (finalJson != null)
                    setVals(finalJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                myGlobalsFunctions.storeStringToFile(crypto,getString(R.string.crypto_info_dir),finalJson);
                setVals(finalJson);
                return finalJson;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter = new WatchlistRecyclerViewAdapter(watchlistArray);
            recyclerView.setAdapter(adapter);
        }
    }
}