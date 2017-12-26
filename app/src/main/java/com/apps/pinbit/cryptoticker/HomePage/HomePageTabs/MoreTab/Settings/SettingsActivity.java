package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.MoreTab.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.apps.pinbit.cryptoticker.Global.Constants;
import com.apps.pinbit.cryptoticker.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.change_currency_view).setOnClickListener(this);
        findViewById(R.id.change_username_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_currency_view:
                Intent intent = new Intent(SettingsActivity.this, ChangeCurrencyActivity.class);
                startActivity(intent);
                break;

            case R.id.change_username_view:
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.change_username_dialogue, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = promptsView.findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String username = userInput.getText().toString();
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);
                                        pref.edit().putString(Constants.PREFERENCE_USERNAME, username).apply();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                break;
        }
    }
}
