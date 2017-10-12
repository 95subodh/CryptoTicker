package com.apps.sky.cryptoticker.StockPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockInfoTab.StockInfoTab;
import com.apps.sky.cryptoticker.StockPage.StockNewsTab.StockNewsTab;
import com.apps.sky.cryptoticker.StockPage.StockPredictionTab.StockPredictionTab;

public class StockPageActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String crypto_name;

//    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
//    SharedPreferences.Editor editor = pref.edit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

        Intent intent = getIntent();
        crypto_name = intent.getExtras().getString("crypto");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_currency);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Dialog dialog = new Dialog(StockPageActivity.this);
//                dialog.setContentView(R.layout.stock_add_dialog_view);
//                dialog.setCancelable(true);
//                RadioButton rd1 = (RadioButton) dialog.findViewById(R.id.watchlist_radio_btn);
//                RadioButton rd2 = (RadioButton) dialog.findViewById(R.id.my_portfolio_radio_btn);
//                Button btn = (Button) dialog.findViewById(R.id.add_currency_ok_btn);
//                btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Snackbar.make(view, "Currency added to your portfolio :)", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
//                });
//                dialog.show();

//                //--------add values here -------
//                editor.putString("currency_name", crypto_name);
            }
        });

    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Add this currency to")
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // The 'which' argument contains the index position
//                        // of the selected item
//                    }
//                });
//        return builder.create();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    StockInfoTab stockInfoTab = new StockInfoTab();
                    stockInfoTab.crypto = crypto_name;
                    return stockInfoTab;
                case 1:
                    StockNewsTab stockNewsTab = new StockNewsTab();
                    stockNewsTab.crypto = crypto_name;
                    return stockNewsTab;
                case 2:
                    StockPredictionTab stockPredictionTab = new StockPredictionTab();
                    return stockPredictionTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() { return 3; }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.stock_tab1_title);
                case 1:
                    return getString(R.string.stock_tab2_title);
                case 2:
                    return getString(R.string.stock_tab3_title);
            }
            return null;
        }
    }
}
