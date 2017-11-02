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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.apps.sky.cryptoticker.Global.ConstantsCrypto;
import com.apps.sky.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.AddToMyPortfolioForm.AddToMyPortfolioFormActivity;
import com.apps.sky.cryptoticker.HomePage.MainActivity;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockInfoTab.StockInfoTab;
import com.apps.sky.cryptoticker.StockPage.StockNewsTab.StockNewsTab;
import com.apps.sky.cryptoticker.StockPage.StockPredictionTab.StockPredictionTab;

import java.util.ArrayList;

public class StockPageActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private String cryptoID, cryptoName;
    private Boolean isFabOpen = false;
    private MyGlobalsFunctions myGlobalsFunctions;
    private FloatingActionButton add, fab1, fab2;
    private View fab1_view, fab2_view;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_page);

        Intent intent = getIntent();
        if (intent.getExtras() != null) cryptoID = intent.getExtras().getString("cryptoID");
        if (cryptoID != null) cryptoName = ConstantsCrypto.cryptoMap.get(cryptoID.replace("-", "_"))[0];

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView toolbarTextView = findViewById(R.id.toolbar_title);
        toolbarTextView.setText(cryptoName.toUpperCase());
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab1_view = findViewById(R.id.fab1_view);
        fab2_view = findViewById(R.id.fab2_view);

        add = findViewById(R.id.add_currency);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        myGlobalsFunctions = new MyGlobalsFunctions(getApplicationContext());
      
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
            }
        });

        fab1_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> watchlistitems = myGlobalsFunctions.retrieveListFromFile(getString(R.string.crypto_watchlist_file), getString(R.string.crypto_watchlist_dir));
                if(watchlistitems==null || !watchlistitems.contains(cryptoID)){
                    watchlistitems.add(cryptoID);
                    myGlobalsFunctions.storeListToFile( getString(R.string.crypto_watchlist_file), getString(R.string.crypto_watchlist_dir), watchlistitems);
                }
                Intent intent = new Intent(StockPageActivity.this, MainActivity.class);
                intent.putExtra("tab", "watchlist");
                startActivity(intent);
            }
        });

        fab2_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockPageActivity.this, AddToMyPortfolioFormActivity.class);
                intent.putExtra("cryptoID", "" + cryptoID.toLowerCase());
                intent.putExtra("only_details", false);
                startActivity(intent);
            }
        });

    }

    public void animateFAB(){
        if(isFabOpen){
            add.startAnimation(rotate_backward);
            fab1_view.startAnimation(fab_close);
            fab2_view.startAnimation(fab_close);
            fab1_view.setClickable(false);
            fab2_view.setClickable(false);
            isFabOpen = false;

        } else {
            add.startAnimation(rotate_forward);
            fab1_view.startAnimation(fab_open);
            fab2_view.startAnimation(fab_open);
            fab1_view.setClickable(true);
            fab2_view.setClickable(true);
            isFabOpen = true;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    StockInfoTab stockInfoTab = new StockInfoTab();
                    stockInfoTab.cryptoID = cryptoID;
                    System.out.println(cryptoID);
                    return stockInfoTab;
                case 1:
                    StockNewsTab stockNewsTab = new StockNewsTab();
                    stockNewsTab.cryptoID = cryptoID;
                    return stockNewsTab;
                case 2:
                    return new StockPredictionTab();
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
