package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.CryptoTradeObject;
import com.apps.sky.cryptoticker.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyPortfolioTab extends Fragment {

    View myPortfolioView, myCurrentPortfolioView;

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    String url, cryptoID;
    MyGlobalsFunctions myGlobalsFunctions;
    ArrayList<CryptoTradeObject> myPortfolioItems;
    CryptoTradeObject cur_item = new CryptoTradeObject();
    ArrayList<MyPortfolioObject> myPortfolioArray = new ArrayList<MyPortfolioObject>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.my_portfolio_tab, container, false);
        }

        myGlobalsFunctions = new MyGlobalsFunctions(rootView.getContext());

        RelativeLayout current_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_current_portfolio_card, null, false);
        RelativeLayout current_portfolio_view = rootView.findViewById(R.id.my_current_portfolio_view);
        current_portfolio_view.addView(current_portfolio_layout);
        myCurrentPortfolioView = current_portfolio_view;

        RelativeLayout my_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_portfolio_card, null, false);
        RelativeLayout my_portfolio_view = rootView.findViewById(R.id.my_portfolio_view);
        my_portfolio_view.addView(my_portfolio_layout);
        myPortfolioView = my_portfolio_view;

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray);
        recyclerView.setAdapter(adapter);

        myPortfolioItems = new ArrayList<CryptoTradeObject>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
        String json = myGlobalsFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

        try {
            if (json != null) myPortfolioItems = gson.fromJson(json, type);
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("error", "error in parsing json");
        }

        for (int i = 0; i < myPortfolioItems.size(); ++i) {
            cur_item = myPortfolioItems.get(i);
            cryptoID = cur_item.getCryptoID();
            url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=INR";
            String imageUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/"+cryptoID+".png";
            new JSONTask().execute(url, imageUrl);
        }
        return rootView;
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
                JSONArray jarr = new JSONArray(finalJson);

                JSONObject parentObject = jarr.getJSONObject(0);
                MyPortfolioObject currency_details = new MyPortfolioObject();
                currency_details.setTitle(parentObject.getString("name"));
                currency_details.setCurrentPrice(parentObject.getString("price_inr"));
                currency_details.setMyProfit("34.08%");
                currency_details.setIcon(params[1]);
                currency_details.setCryptoID(parentObject.getString("id"));

                myPortfolioArray.add(currency_details);
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

            adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray);
            recyclerView.setAdapter(adapter);
        }
    }
}
