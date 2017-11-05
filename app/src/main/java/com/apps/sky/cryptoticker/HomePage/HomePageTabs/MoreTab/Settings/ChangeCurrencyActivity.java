package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;

public class ChangeCurrencyActivity extends AppCompatActivity {

    String currency;
    ListView listView;
    ArrayList<String> currencies = new ArrayList<>();
    MyGlobalsFunctions myGlobalsFunctions;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_currency);
        sharedPreferences = this.getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);

        currencies.add("INR");
        currencies.add("USD");

        myGlobalsFunctions = new MyGlobalsFunctions(this);
        TextView currentCurrencyTextView = findViewById(R.id.default_currency_value);
        currency = sharedPreferences.getString(Constants.CURRENT_CURRENCY, new String());
        if (currency.equals("")) currency = "INR";
        currentCurrencyTextView.setText(currency);

        listView = findViewById(R.id.choose_currency_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ChangeCurrencyActivity.this,
                R.layout.search_list_view_item, currencies);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = listView.getItemAtPosition(i).toString();
                sharedPreferences.edit().putString(Constants.CURRENT_CURRENCY, temp).apply();
            }
        });
    }


}
