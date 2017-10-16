package com.apps.sky.cryptoticker.StockPage.AddToMyPortfolioForm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.CryptoTradeObject;
import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddToMyPortfolioFormActivity extends AppCompatActivity {

    private String cryptoName, crypto;
    private MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean onlyDetails;

    ArrayList<TradeObject> tradeArray = new ArrayList<TradeObject>();
    CryptoTradeObject cryptoTradeObject = new CryptoTradeObject();
    ArrayList<CryptoTradeObject> cryptoTradeObjectArrayList = new ArrayList<CryptoTradeObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_to_my_portfolio_form);
        Log.d("Fabs", "Currency added to your portfolio :)");

        Intent intent = getIntent();
        crypto = intent.getExtras().getString("crypto");
        onlyDetails = intent.getExtras().getBoolean("only_details");

        TextView title = (TextView) findViewById(R.id.trade_details_heading);
        String tradeName = crypto + " Trade Details";
        title.setText(tradeName);
        Button addTrade = (Button) findViewById(R.id.add_trade_button);
        Button submit = (Button) findViewById(R.id.submit_button);

        addTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTradeCard();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTradeCard();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getCurrencyTradeDetails();
        if (!onlyDetails) addTradeCard();

        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

    private void addTradeCard() {
        TradeObject obj = new TradeObject();
        Integer tradeNum = tradeArray.size() + 1;
        obj.setTradeNumber("Trade " + tradeNum.toString());
        tradeArray.add(obj);
    }

    private void addCurrencyToMyPortfolio() {
        cryptoTradeObject.setCrypto(crypto);
        cryptoTradeObject.setTrades(tradeArray);
        cryptoTradeObjectArrayList.add(cryptoTradeObject);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
        String json = gson.toJson(cryptoTradeObjectArrayList, type);

        ArrayList<String> myPortfolioItems = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));
        myPortfolioItems.add(json);
        myGlobalsFunctions.storeListToFile( getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir), myPortfolioItems);

    }

    private void getCurrencyTradeDetails() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();

        String json = gson.toJson(myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir)), type);

        ArrayList<CryptoTradeObject> fromJson = gson.fromJson(json, type);

        for (CryptoTradeObject trades : fromJson) {
            if (trades.getCrypto() == crypto) {
                tradeArray = trades.getTrades();
                System.out.println(trades);
                break;
            }
        }
    }
}
