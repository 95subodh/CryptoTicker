package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.pinbit.cryptoticker.Global.Constants;
import com.apps.pinbit.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.CryptoTradeObject;
import com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.TradeObject;
import com.apps.pinbit.cryptoticker.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MyPortfolioTab extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View myPortfolioView, myCurrentPortfolioView;

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Gson gson;
    Type type;

    String url, exchangeRateURL, currency, prevCurrency, currCurrency;
    Boolean isCurrencyChanged = Boolean.FALSE;
    double conversion = 1.0;
    float totalCost = 0, totalPrice = 0;
    MyGlobalsFunctions myGlobalsFunctions;
    ArrayList<CryptoTradeObject> myPortfolioItems;
    CryptoTradeObject curItem = new CryptoTradeObject();
    ArrayList<MyPortfolioObject> myPortfolioArray, myPortfolioArrayTemp;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    SpinKitView spinKit;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("com.apps.pinbit.cryptoticker", Context.MODE_PRIVATE);
        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        if (currency.equals("")) currency = Constants.DEFAULT_CURRENCY;

        myGlobalsFunctions = new MyGlobalsFunctions(getContext());
        myPortfolioItems = new ArrayList<>();

        gson = new Gson();
        type = new TypeToken<ArrayList<CryptoTradeObject>>() {
        }.getType();
        String json = myGlobalsFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

        try {
            if (json != null) myPortfolioItems = gson.fromJson(json, type);
        } catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("error", "error in parsing portfolio json");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            if (myPortfolioItems == null || myPortfolioItems.size() == 0) {
                rootView = inflater.inflate(R.layout.empty_view_layout, container, false);
                ((TextView) rootView.findViewById(R.id.empty_view_text)).setText("Your portfolio is empty");
            } else {
                rootView = inflater.inflate(R.layout.my_portfolio_tab, container, false);
                RelativeLayout current_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_current_portfolio_card, container, false);
                RelativeLayout current_portfolio_view = rootView.findViewById(R.id.my_current_portfolio_view);
                current_portfolio_view.addView(current_portfolio_layout);
                myCurrentPortfolioView = current_portfolio_view;

                RelativeLayout my_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_portfolio_card, container, false);
                RelativeLayout my_portfolio_view = rootView.findViewById(R.id.my_portfolio_view);
                my_portfolio_view.addView(my_portfolio_layout);
                myPortfolioView = my_portfolio_view;
            }
        }

        if (myPortfolioItems != null && myPortfolioItems.size() > 0) {
            spinKit = rootView.findViewById(R.id.spin_kit_portfolio);
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
        if (myPortfolioItems != null && myPortfolioItems.size() > 0) {
            spinKit.setVisibility(View.VISIBLE);
            totalCost = 0;
            totalPrice = 0;
            count = 0;
            myPortfolioArray = new ArrayList<>();
            adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray, MyPortfolioTab.this);
            recyclerView.setAdapter(adapter);

            if (myGlobalsFunctions.isNetworkConnected()) {

                prevCurrency = sharedPreferences.getString(Constants.PREV_PORTFOLIO_CURRENCY, "");
                if (prevCurrency.equals("")) {
                    prevCurrency = Constants.DEFAULT_CURRENCY;
                }

                currCurrency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
                if (currCurrency.equals("")) currCurrency = Constants.DEFAULT_CURRENCY;

                if (!prevCurrency.equals(currCurrency)) isCurrencyChanged = Boolean.TRUE;

                exchangeRateURL = "http://api.fixer.io/latest?base=USD";

                if (isCurrencyChanged) {
                    myPortfolioItems = new ArrayList<>();

                    String json = myGlobalsFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

                    try {
                        if (json != null) myPortfolioItems = gson.fromJson(json, type);
                        new minorJSONTask().execute().get();
                        Thread.sleep(2000);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                myPortfolioArrayTemp = new ArrayList<>();
                for (int i = 0; i < myPortfolioItems.size(); ++i) {
                    String cryptoID = myPortfolioItems.get(i).getCryptoID();

                    MyPortfolioObject x = new MyPortfolioObject();
                    x.setCryptoID(cryptoID);
                    myPortfolioArrayTemp.add(x);
                }
                for (int i = 0; i < myPortfolioItems.size(); ++i) {
                    curItem = myPortfolioItems.get(i);
                    String cryptoID = curItem.getCryptoID();
                    url = "https://api.coinmarketcap.com/v1/ticker/" + cryptoID + "/?convert=" + currency.toUpperCase();
                    String iconUrl = "https://files.coinmarketcap.com/static/img/coins/32x32/" + cryptoID + ".png";
                    float quantity = 0, cost = 0;
                    for (TradeObject item : curItem.getTrades()) {
                        float q = Float.parseFloat(item.getQuantity());
                        cost += (Float.parseFloat(item.getCost()) * q);
                        quantity += Float.parseFloat(item.getQuantity());
                    }
                    new JSONTask().execute(url, iconUrl, String.valueOf(cost), String.valueOf(quantity), cryptoID);
                }
            }
            setCurrentPortfolioValues();
        } else {
            LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ((LinearLayout) rootView.findViewById(R.id.main_view)).removeAllViews();
            ((LinearLayout) rootView.findViewById(R.id.main_view)).addView(inflater.inflate(R.layout.empty_view_layout, ((ViewGroup) getView().getParent()), false));
            ((TextView) rootView.findViewById(R.id.empty_view_text)).setText("Your portfolio is empty");
            rootView.refreshDrawableState();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    void setCurrentPortfolioValues() {
        float totalProfit, priceDif;
        priceDif = totalPrice - totalCost;
        totalProfit = 100 * ((totalPrice - totalCost) / totalCost);
        totalProfit = (float) (Math.round(totalProfit * 100.0) / 100.0);
        TextView currentPortfolioValue = rootView.findViewById(R.id.current_portfolio_value);
        TextView totalCostValue = rootView.findViewById(R.id.total_cost);
        TextView totalProfitValue = rootView.findViewById(R.id.total_profit);
        TextView totalProfitPerValue = rootView.findViewById(R.id.total_profit_percentage);
        currentPortfolioValue.setText(myGlobalsFunctions.floatFormatter(String.valueOf(totalPrice), true, true, false));
        totalCostValue.setText(myGlobalsFunctions.floatFormatter(String.valueOf(totalCost), true, true, true));
        totalProfitValue.setText(myGlobalsFunctions.floatFormatter(String.valueOf(priceDif), true, true, true));
        String tp = String.valueOf(totalProfit) + "%";
        totalProfitPerValue.setText(tp);

        if (getContext() != null) {
            if (priceDif >= 0)
                totalProfitValue.setTextColor(getContext().getResources().getColor(R.color.valuePositive));
            else
                totalProfitValue.setTextColor(getContext().getResources().getColor(R.color.valueNegative));
            if (totalProfit >= 0)
                totalProfitPerValue.setTextColor(getContext().getResources().getColor(R.color.valuePositive));
            else
                totalProfitPerValue.setTextColor(getContext().getResources().getColor(R.color.valueNegative));
        }
        myCurrentPortfolioView.refreshDrawableState();
    }

    String calcMyProfitPercentage(String costStr, String quantityStr, String curPrice) {
        float quantity = Float.parseFloat(quantityStr), cost = Float.parseFloat(costStr), profitPer, coinPrice = Float.parseFloat(curPrice);
        profitPer = 100 * ((coinPrice * quantity - cost) / cost);
        totalCost += cost;
        totalPrice += (coinPrice * quantity);
        return String.valueOf(Math.round(profitPer * 100.0) / 100.0);
    }

    public class minorJSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String exchangeRateJSON = myGlobalsFunctions.fetchJSONasString(exchangeRateURL);
                JSONObject jsonObject = new JSONObject(exchangeRateJSON);
                JSONObject currExRate = jsonObject.getJSONObject("rates");
                conversion = 1.0;
                if (!currCurrency.equals("USD") && !prevCurrency.equals("USD"))
                    conversion = currExRate.getDouble(currCurrency.toUpperCase()) / currExRate.getDouble(prevCurrency.toUpperCase());
                else if (!currCurrency.equals("USD") && prevCurrency.equals("USD"))
                    conversion = currExRate.getDouble(currCurrency.toUpperCase());
                else if (currCurrency.equals("USD") && !prevCurrency.equals("USD")) {
                    conversion = currExRate.getDouble(prevCurrency.toUpperCase());
                    conversion = 1.0 / conversion;
                }
                return exchangeRateJSON;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            for (int i = 0; i < myPortfolioItems.size(); ++i) {
                curItem = myPortfolioItems.get(i);
                float cost;
                for (TradeObject item : curItem.getTrades()) {
                    cost = Float.parseFloat(item.getCost());
                    item.setCost(String.valueOf(cost * conversion));
                }
            }

            String json = gson.toJson(myPortfolioItems, type);
            myGlobalsFunctions.storeStringToFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir), json);
            sharedPreferences.edit().putString(Constants.PREV_PORTFOLIO_CURRENCY, currCurrency).apply();
            isCurrencyChanged = Boolean.FALSE;
            currency = currCurrency;
            loadView();
        }
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
                if (finalJson != null) {
                    JSONArray jarr = new JSONArray(finalJson);

                    int cardPosition = 0;
                    for (int i = 0; i < myPortfolioArrayTemp.size(); ++i) {
                        if (myPortfolioArrayTemp.get(i).getCryptoID().equals(params[4])) {
                            cardPosition = i;
                            break;
                        }
                    }

                    JSONObject parentObject = jarr.getJSONObject(0);
                    myPortfolioArrayTemp.get(cardPosition).setContext(getContext());
                    myPortfolioArrayTemp.get(cardPosition).setTitle(parentObject.getString("name"));
                    myPortfolioArrayTemp.get(cardPosition).setIcon(params[1]);
                    myPortfolioArrayTemp.get(cardPosition).setCryptoID(parentObject.getString("id"));

                    String price = parentObject.getString("price_" + currency.toLowerCase());

                    String profitPer = calcMyProfitPercentage(params[2], params[3], price) + "%";
                    myPortfolioArrayTemp.get(cardPosition).setCurrentValue(Float.toString(Float.parseFloat(params[3]) * Float.parseFloat(price)));
                    myPortfolioArrayTemp.get(cardPosition).setMyProfit(profitPer);
                    if (profitPer.charAt(0) == '-')
                        myPortfolioArrayTemp.get(cardPosition).setChangeColor(false);
                    else myPortfolioArrayTemp.get(cardPosition).setChangeColor(true);
                }
                return finalJson;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            count++;
            if (count == myPortfolioItems.size()) {
                myPortfolioArray.addAll(myPortfolioArrayTemp);
                adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray, MyPortfolioTab.this);
                recyclerView.setAdapter(adapter);
                setCurrentPortfolioValues();
                spinKit.setVisibility(View.INVISIBLE);
            }

        }
    }
}
