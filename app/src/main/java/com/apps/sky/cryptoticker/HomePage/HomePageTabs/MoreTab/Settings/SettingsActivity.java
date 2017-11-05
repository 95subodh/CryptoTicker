package com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.apps.sky.cryptoticker.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        findViewById(R.id.change_currency_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_currency_view:
                Intent intent = new Intent(SettingsActivity.this, ChangeCurrencyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
