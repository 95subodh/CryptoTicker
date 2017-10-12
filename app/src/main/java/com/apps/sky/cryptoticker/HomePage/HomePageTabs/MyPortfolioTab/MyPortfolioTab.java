package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MyPortfolioTab extends Fragment {

    View myPortfolioView, myCurrentPortfolioView;

    private View rootView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String url;
    public String crypto;
    final MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions(getContext());
    ArrayList<String> items;
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

        RelativeLayout current_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_current_portfolio_card, null, false);
        RelativeLayout current_portfolio_view = (RelativeLayout) rootView.findViewById(R.id.my_current_portfolio_view);
        current_portfolio_view.addView(current_portfolio_layout);
        myCurrentPortfolioView = (View) current_portfolio_view;

        RelativeLayout my_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_portfolio_card, null, false);
        RelativeLayout my_portfolio_view = (RelativeLayout) rootView.findViewById(R.id.my_portfolio_view);
        my_portfolio_view.addView(my_portfolio_layout);
        myPortfolioView = (View) my_portfolio_view;

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray);
        recyclerView.setAdapter(adapter);

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

        for (int i = 0; i < 5; ++i) {

            //--------retrieve values here instead of this line-------
            crypto = items.get(i);

            url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
            new JSONTask().execute(url);
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
                currency_details.setCrypto(parentObject.getString("id"));

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
