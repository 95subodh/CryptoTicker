package com.apps.sky.cryptoticker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class StockPageActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String url;
    private static String name, price, change, rank, cap, avlsup, totsup, lstupd;
    private TextView coinName, coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinTotSupply, coinLstUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

        Intent intent = getIntent();
        String crypto_name = intent.getExtras().getString("crypto");
        url = "https://api.coinmarketcap.com/v1/ticker/"+crypto_name+"/?convert=INR";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private void fillInfoFromJSON() {

        coinName = (TextView) findViewById(R.id.coinName);
        coinName.setText(name);
        coinPrice = (TextView) findViewById(R.id.coinPrice);
        coinPrice.setText(price);
        coinAvailSupply = (TextView) findViewById(R.id.coinAvailSupply);
        coinAvailSupply.setText(avlsup);
        coinCap = (TextView) findViewById(R.id.coinCap);
        coinCap.setText(cap);
        coinLstUpdate = (TextView) findViewById(R.id.coinLstUpdate);
        coinLstUpdate.setText(lstupd);
        coinRank = (TextView) findViewById(R.id.coinRank);
        coinRank.setText(rank);
        coinTotSupply = (TextView) findViewById(R.id.coinTotSupply);
        coinTotSupply.setText(totsup);
        coinChange = (TextView) findViewById(R.id.coinChange);
        coinChange.setText(change);
//        if(Integer.parseInt(change)<0){
//            coinChange.setTextColor(Color.RED);
//        }
//        else {
//            coinChange.setTextColor(Color.GREEN);
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    StockTab1 stockTab1 = new StockTab1();
                    new JSONTask().execute(url);
//                    fillInfoFromJSON();
//                    new JSONTask().execute(url);
                    return stockTab1;
                case 1:
                    StockTab2 stockTab2 = new StockTab2();
                    return stockTab2;
                case 2:
                    StockTab3 stockTab3 = new StockTab3();
//                    fillInfoFromJSON();
                    return stockTab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

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

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONArray jarr = new JSONArray(finalJson);

                JSONObject parentObject = jarr.getJSONObject(0);
                name = parentObject.getString("name");
                price = parentObject.getString("price_inr");
                change = parentObject.getString("percent_change_24h");
                rank = parentObject.getString("rank");
                cap = parentObject.getString("market_cap_inr");
                avlsup = parentObject.getString("available_supply");
                totsup = parentObject.getString("total_supply");
                lstupd = parentObject.getString("last_updated");
                return price;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            coinName = (TextView) findViewById(R.id.coinName);
//            coinName.setText(name);
            fillInfoFromJSON();
        }
    }
}
