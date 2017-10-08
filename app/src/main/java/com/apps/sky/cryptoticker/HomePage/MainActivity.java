package com.apps.sky.cryptoticker.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.HomePage.BottomNavigationBar.BottomNavigationViewHelper;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.MoreTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab.MyPortfolioTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.TrendingTab.TrendingTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab.WatchlistTab;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items;
    BottomNavigationView bottomNavigationView;
    RelativeLayout relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Fragment fragment = new WatchlistTab();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        relative = (RelativeLayout) findViewById(R.id.tab_content);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment fragment;

                        switch (item.getItemId()) {
                            case R.id.action_watchlist:
                                fragment = new WatchlistTab();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                break;

                            case R.id.action_my_portfolio:
                                fragment = new MyPortfolioTab();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                break;

                            case R.id.action_trending:
                                fragment = new TrendingTab();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                break;

                            case R.id.action_more:
                                fragment = new MoreTab();
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                                break;

                        }
                        return true;
                    }
                });

        listView = (ListView) findViewById(R.id.list_view);

        items = new ArrayList<>();
        String[] otherList = new String[] {"atc-coin","bitcoin","bitconnect","bitshares","dash","eos","ethereum","golem-network-tokens",
                "lisk","litecoin","metal","nexus","omisego","ripple","rupee","syscoin","tether","the-champcoin","waves"};
        items.addAll(Arrays.asList(otherList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String text = listView.getItemAtPosition(i).toString();
                Intent intent = new Intent(MainActivity.this, StockPageActivity.class);
                intent.putExtra("crypto", "" + text.toLowerCase());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();
                for (String temp : items) {
                    if (temp.toLowerCase().contains(newText.toLowerCase())) {
                        templist.add(temp);
                    }
                }
                System.out.println(newText);

                if (newText.length() > 0) {
                    listView.setVisibility(View.VISIBLE);
                }
                else {
                    listView.setVisibility(View.GONE);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        android.R.layout.simple_list_item_1, templist);
                listView.setAdapter(adapter);

                return true;
            }
        });

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                bottomNavigationView.setVisibility(View.INVISIBLE);
                relative.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                bottomNavigationView.setVisibility(View.VISIBLE);
                relative.setVisibility(View.VISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
