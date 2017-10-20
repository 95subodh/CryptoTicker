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
import com.apps.sky.cryptoticker.HomePage.MainActivity;
import com.apps.sky.cryptoticker.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AddToMyPortfolioFormActivity extends AppCompatActivity {

    private String cryptoName, crypto;
    private MyGlobalsFunctions myGlobalsFunctions;

    private RecyclerView recyclerView;
    private TradeRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean onlyDetails;
    private Button addTrade, submit;

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
        myGlobalsFunctions = new MyGlobalsFunctions(this);

        TextView title = (TextView) findViewById(R.id.trade_details_heading);
        String tradeName = crypto + " Trade Details";
        title.setText(tradeName);
        addTrade = (Button) findViewById(R.id.add_trade_button);
        submit = (Button) findViewById(R.id.submit_button);

        addTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tradeArray.size() > 0)
                    addExistingTradeToList();
                addBlankTradeCard();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExistingTradeToList();
                addCurrencyToMyPortfolio();
                Intent intent = new Intent(AddToMyPortfolioFormActivity.this, MainActivity.class);
                intent.putExtra("tab", "my_portfolio");
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        getCurrencyTradeDetails();
        if (!onlyDetails) addBlankTradeCard();

        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (tradeArray.size() > 0) {
//            submit.setEnabled(true);
//            addTrade.setEnabled(true);
//        }
//        else {
//            submit.setEnabled(false);
//            addTrade.setEnabled(false);
//        }
//        return super.onTouchEvent(event);
//    }

    private void addExistingTradeToList() {
        tradeArray.get(tradeArray.size() - 1).setCost("100");
        tradeArray.get(tradeArray.size() - 1).setQuantity("5");
    }

    private void addBlankTradeCard() {
        TradeObject obj = new TradeObject();
        Integer tradeNum = tradeArray.size() + 1;
        obj.setTradeNumber("Trade " + tradeNum.toString());
        tradeArray.add(obj);
        adapter = new TradeRecyclerViewAdapter(tradeArray);
        recyclerView.setAdapter(adapter);
    }

    private void addCurrencyToMyPortfolio() {
        cryptoTradeObject.setCrypto(crypto);
        cryptoTradeObject.setTrades(tradeArray);
        cryptoTradeObjectArrayList.add(cryptoTradeObject);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();
        String json = gson.toJson(cryptoTradeObjectArrayList, type);
        myGlobalsFunctions.storeStringToFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir), json);
//        Gson gson1 = new GsonBuilder().create();
//        JsonArray myPortfolioItems = gson1.toJsonTree(cryptoTradeObjectArrayList).getAsJsonArray();
//        ArrayList<String> myPortfolioItems = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));
//        myPortfolioItems.add(json);
//        myGlobalsFunctions.storeListToFile( getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir), json);

    }

    private void getCurrencyTradeDetails() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CryptoTradeObject>>() {}.getType();

        String json = myGlobalsFunctions.retieveStringFromFile(getString(R.string.crypto_my_portfolio_file), getString(R.string.crypto_my_portfolio_dir));

        try {
            if (json != null) {
                cryptoTradeObjectArrayList = gson.fromJson(json, type);
                cryptoTradeObject = cryptoTradeObjectArrayList.get(0);    /////// This cryptotradeobject contains retrieved values from internal storage
            }
        }
        catch (IllegalStateException | JsonSyntaxException exception) {
            Log.d("error", "error in parsing json");
        }
    }
}
