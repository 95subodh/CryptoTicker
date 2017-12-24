package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ChangeCurrencyActivity extends AppCompatActivity {

    String currency;
    ListView listView;
    ArrayList<HashMap<String, Object>> currencies;
    MyGlobalsFunctions myGlobalsFunctions;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_currency);
        sharedPreferences = this.getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);

        myGlobalsFunctions = new MyGlobalsFunctions(this);
        final TextView currentCurrencyTextView = findViewById(R.id.default_currency_value);
        Button setCurrencyBtn = findViewById(R.id.set_currency_btn);

        currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        if (currency.equals("")) currency = Constants.DEFAULT_CURRENCY;
        currentCurrencyTextView.setText(currency);

        currencies = new ArrayList<>();
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("currency", "INR");
        currencies.add(map1);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("currency", "USD");
        currencies.add(map2);
        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("currency", "EUR");
        currencies.add(map3);
        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("currency", "JPY");
        currencies.add(map4);

        for (HashMap<String, Object> m : currencies) {
            if (m.containsValue(currency)) {
                m.put("checked", true);
            } else {
                m.put("checked", false);
            }
        }
        listView = findViewById(R.id.choose_currency_listview);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final SimpleAdapter adapter = new SimpleAdapter(this, currencies, R.layout.radio_btn_listview_item, new String[]{"currency", "checked"}, new int[]{R.id.currency_text, R.id.choose_btn});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                RadioButton rb = v.findViewById(R.id.choose_btn);
                if (!rb.isChecked()) {
                    for (HashMap<String, Object> curr : currencies)
                        curr.put("checked", false);

                    currencies.get(arg2).put("checked", true);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        setCurrencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HashMap<String, Object> curr : currencies) {
                    Boolean check = (Boolean) curr.get("checked");
                    if (check) {
                        String temp = (String) curr.get("currency");
                        sharedPreferences.edit().putString(Constants.PREFERENCE_CURRENCY, temp).apply();
                        currency = temp;
                        currentCurrencyTextView.setText(temp);
                        break;
                    }
                }
            }
        });

    }
}

