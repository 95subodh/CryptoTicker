package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.CryptoTradeObject;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.TradeObject;
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

    String url, cryptoID, currency;
    float totalCost = 0, totalPrice = 0;
    MyGlobalsFunctions myGlobalsFunctions;
    ArrayList<CryptoTradeObject> myPortfolioItems;
    CryptoTradeObject curItem = new CryptoTradeObject();
    ArrayList<MyPortfolioObject> myPortfolioArray = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(Constants.CURRENT_CURRENCY, "");
        if (currency.equals("")) currency = "INR";

        myGlobalsFunctions = new MyGlobalsFunctions(getContext());
        myPortfolioItems = new ArrayList<>();

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
        String json = myGlobalsFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

        try {
            if (json != null) myPortfolioItems = gson.fromJson(json, type);
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("error", "error in parsing portfolio json");
        }

        if (myGlobalsFunctions.isNetworkConnected()) {
            for (int i = 0; i < myPortfolioItems.size(); ++i) {
                curItem = myPortfolioItems.get(i);
                cryptoID = curItem.getCryptoID();
                url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
                String iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/" + cryptoID + ".png";
                float quantity = 0, cost = 0;
                for (TradeObject item : curItem.getTrades()) {
                    float q = Float.parseFloat(item.getQuantity());
                    cost += (Float.parseFloat(item.getCost()) * q);
                    quantity += Float.parseFloat(item.getQuantity());
                }
                new JSONTask().execute(url, iconUrl, String.valueOf(cost), String.valueOf(quantity));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.my_portfolio_tab, container, false);
            RelativeLayout current_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_current_portfolio_card, null, false);
            RelativeLayout current_portfolio_view = rootView.findViewById(R.id.my_current_portfolio_view);
            current_portfolio_view.addView(current_portfolio_layout);
            myCurrentPortfolioView = current_portfolio_view;

            RelativeLayout my_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_portfolio_card, null, false);
            RelativeLayout my_portfolio_view = rootView.findViewById(R.id.my_portfolio_view);
            my_portfolio_view.addView(my_portfolio_layout);
            myPortfolioView = my_portfolio_view;
        }

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        String currencyNew = sharedPreferences.getString(Constants.CURRENT_CURRENCY, "");
        if (!currency.equals(currencyNew)) {
            currency = currencyNew;
            totalCost = 0; totalPrice = 0;
            myPortfolioArray = new ArrayList<>();
            if (myGlobalsFunctions.isNetworkConnected()) {
                for (int i = 0; i < myPortfolioItems.size(); ++i) {
                    curItem = myPortfolioItems.get(i);
                    cryptoID = curItem.getCryptoID();
                    url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
                    String iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/" + cryptoID + ".png";
                    float quantity = 0, cost = 0;
                    for (TradeObject item : curItem.getTrades()) {
                        float q = Float.parseFloat(item.getQuantity());
                        cost += (Float.parseFloat(item.getCost()) * q);
                        quantity += Float.parseFloat(item.getQuantity());
                    }
                    new JSONTask().execute(url, iconUrl, String.valueOf(cost), String.valueOf(quantity));
                }
            }
        }
        adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray,MyPortfolioTab.this);
        recyclerView.setAdapter(adapter);
        setCurrentPortfolioValues();

        return rootView;
    }

    void setCurrentPortfolioValues() {
        float totalProfit, priceDif;
        priceDif = totalPrice - totalCost;
        totalProfit = 100 * ((totalPrice - totalCost)/totalCost);
        totalProfit = (float) (Math.round(totalProfit * 100.0) / 100.0);
        TextView currentPortfolioValue = rootView.findViewById(R.id.current_portfolio_value);
        TextView totalCostValue = rootView.findViewById(R.id.total_cost);
        TextView totalProfitValue = rootView.findViewById(R.id.total_profit);
        TextView totalProfitPerValue = rootView.findViewById(R.id.total_profit_percentage);
        currentPortfolioValue.setText(myGlobalsFunctions.commaSeperateInteger(String.valueOf(totalPrice), true));
        totalCostValue.setText(myGlobalsFunctions.commaSeperateInteger(String.valueOf(totalCost), true));
        totalProfitValue.setText(myGlobalsFunctions.commaSeperateInteger(String.valueOf(priceDif), true));
        String tp = String.valueOf(totalProfit) + "%";
        totalProfitPerValue.setText(tp);

        if (getContext() != null) {
            if (priceDif >= 0)
                totalProfitValue.setTextColor(getContext().getResources().getColor(R.color.valuePositive));
            else totalProfitValue.setTextColor(getContext().getResources().getColor(R.color.valueNegative));
            if (totalProfit >= 0)
                totalProfitPerValue.setTextColor(getContext().getResources().getColor(R.color.valuePositive));
            else totalProfitPerValue.setTextColor(getContext().getResources().getColor(R.color.valueNegative));
        }
        myCurrentPortfolioView.refreshDrawableState();
    }

    String calcMyProfitPercentage(String costStr, String quantityStr, String curPrice) {
        float quantity = Float.parseFloat(quantityStr), cost = Float.parseFloat(costStr), profitPer, coinPrice = Float.parseFloat(curPrice);
        profitPer = 100 * ((coinPrice * quantity - cost)/cost);
        totalCost += cost;
        totalPrice += (coinPrice * quantity);
        return String.valueOf(Math.round(profitPer * 100.0) / 100.0);
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
                currency_details.setContext(getContext());
                currency_details.setTitle(parentObject.getString("name"));
                currency_details.setCurrentPrice(parentObject.getString("price_" + currency.toLowerCase()));
                currency_details.setIcon(params[1]);
                currency_details.setCryptoID(parentObject.getString("id"));

                String profitPer = calcMyProfitPercentage(params[2], params[3], parentObject.getString("price_" + currency.toLowerCase())) + "%";
                currency_details.setMyProfit(profitPer);
                if (profitPer.charAt(0) == '-') currency_details.setChangeColor(false);
                else currency_details.setChangeColor(true);
                myPortfolioArray.add(currency_details);
                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            setCurrentPortfolioValues();
            adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray,MyPortfolioTab.this);
            recyclerView.setAdapter(adapter);
        }
    }
}
