package com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.ConstantsCrypto;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.HomePage.MainActivity;
import com.apps.sky.cryptoticker.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddToMyPortfolioFormActivity extends AppCompatActivity {

    private String cryptoName, cryptoID;
    private MyGlobalsFunctions myGlobalFunctions;

    private RecyclerView recyclerView;
    private TradeRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    boolean onlyDetails;
    Button addTrade, submit;
    boolean coinPresent = false;

    ArrayList<TradeObject> tradeArray = new ArrayList<>();
    CryptoTradeObject cryptoTradeObject = new CryptoTradeObject();
    ArrayList<CryptoTradeObject> cryptoTradeObjectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_to_my_portfolio_form);

        Intent intent = getIntent();
        cryptoID = intent.getExtras().getString("cryptoID");
        cryptoName = ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[0];
        onlyDetails = intent.getExtras().getBoolean("only_details");
        myGlobalFunctions = new MyGlobalsFunctions(this);

        TextView title = findViewById(R.id.trade_details_heading);
        String tradeName = cryptoName + " Trade Details";
        title.setText(tradeName);
        addTrade = findViewById(R.id.add_trade_button);
        submit = findViewById(R.id.submit_button);

        addTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBlankTradeCard();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCurrencyToMyPortfolio();
                Intent intent = new Intent(AddToMyPortfolioFormActivity.this, MainActivity.class);
                intent.putExtra("tab", "my_portfolio");
                startActivity(intent);
            }
        });
        submit.setEnabled(false);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getCurrencyTradeDetails();
        if (!onlyDetails) addBlankTradeCard();

        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

    private void addBlankTradeCard() {
        addTrade.setEnabled(false);
        submit.setEnabled(false);
        TradeObject obj = new TradeObject();
        Integer tradeNum = tradeArray.size() + 1;
        obj.setTradeNumber("Trade " + tradeNum.toString());
        obj.setCryptoID(cryptoID);
        tradeArray.add(obj);
        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

    void deleteItem(int position) {
        tradeArray.remove(position);
        boolean done = false;
        for (int i = 1; i <= tradeArray.size(); ++i) {
            tradeArray.get(i - 1).setTradeNumber("Trade " + ((Integer) i).toString());
            if (!done && (tradeArray.get(tradeArray.size() - 1).getQuantity().trim().isEmpty() ||
                    tradeArray.get(tradeArray.size() - 1).getCost().trim().isEmpty())) {
                addTrade.setEnabled(false);
                submit.setEnabled(false);
                done = true;
            }
            else {
                addTrade.setEnabled(true);
                submit.setEnabled(true);
            }
        }
        if (tradeArray.size() == 0) {
            addTrade.setEnabled(true);
            submit.setEnabled(false);
        }
//        adapter.notifyItemRemoved(position);
//        adapter.notifyItemRangeChanged(position, tradeArray.size() - position);
        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

    private void addCurrencyToMyPortfolio() {
        cryptoTradeObject.setCryptoID(cryptoID);
        cryptoTradeObject.setTrades(tradeArray);
        if (!coinPresent) cryptoTradeObjectArrayList.add(cryptoTradeObject);
        else {
            for (int i = 0; i < cryptoTradeObjectArrayList.size(); ++i) {
                if (cryptoTradeObjectArrayList.get(i).getCryptoID().equals(cryptoID)) {
                    cryptoTradeObjectArrayList.get(i).setTrades(tradeArray);
                }
            }
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
        String json = gson.toJson(cryptoTradeObjectArrayList, type);
        myGlobalFunctions.storeStringToFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir), json);
    }

    private void getCurrencyTradeDetails() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();

        String json = myGlobalFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

        try {
            if (json != null) {
                cryptoTradeObjectArrayList = gson.fromJson(json, type);
                if (cryptoTradeObjectArrayList.size() > 0) {
                    for (CryptoTradeObject item : cryptoTradeObjectArrayList) {
                        if (item.getCryptoID().equals(cryptoID)) {
                            cryptoTradeObject = item;
                            coinPresent = true;
                        }
                    }
                    tradeArray = cryptoTradeObject.getTrades();
                    adapter = new TradeRecyclerViewAdapter(tradeArray);
                    recyclerView.setAdapter(adapter);
                }
            }
        }
        catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("error", "error in parsing json");
        }
    }
}
