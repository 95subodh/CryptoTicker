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
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.ChatTab.ChatTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.MoreTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab.MyPortfolioTab;
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
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        relative = (RelativeLayout) findViewById(R.id.tab_content);
        listView = (ListView) findViewById(R.id.list_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        String tab = "";
        if (intent.hasExtra("tab"))
            tab = intent.getExtras().getString("tab");
        assignTab(tab);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_watchlist:
                                fragment = new WatchlistTab();
                                break;

                            case R.id.action_my_portfolio:
                                fragment = new MyPortfolioTab();
                                break;

                            case R.id.action_chat:
                                fragment = new ChatTab();
                                break;

//                            case R.id.action_trending:
//                                fragment = new TrendingTab();
//                                break;

                            case R.id.action_more:
                                fragment = new MoreTab();
                                break;

                            default:
                                fragment = new Fragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                        return true;
                    }
                });

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

    private void assignTab(String tab) {
        switch (tab) {
            case "watchlist" :
                fragment = new WatchlistTab();
                break;

            case "my_portfolio" :
                fragment = new MyPortfolioTab();
                break;

            case "chat" :
                fragment = new ChatTab();
                break;

            case "more" :
                fragment = new MoreTab();
                break;

            default:
                fragment = new WatchlistTab();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();
                System.out.println(newText);

                if (newText.length() > 0) {
                    for (String temp : items) {
                        if (temp.toLowerCase().contains(newText.toLowerCase())) {
                            templist.add(temp);
                        }
                    }
                    listView.setVisibility(View.VISIBLE);
                }
                else {
                    listView.setVisibility(View.INVISIBLE);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                        R.layout.search_list_view_item, templist);
                listView.setAdapter(adapter);

                return true;
            }
        });

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                bottomNavigationView.setClickable(false);
                relative.setClickable(false);

            }
            @Override
            public void onViewDetachedFromWindow(View view) {
                bottomNavigationView.setClickable(true);
                relative.setClickable(true);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
