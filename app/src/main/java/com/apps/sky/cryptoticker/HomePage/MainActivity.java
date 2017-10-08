package com.apps.sky.cryptoticker.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.HomePage.BottomNavigationBar.BottomNavigationViewHelper;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.util.ArrayList;

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
        relative = (RelativeLayout) findViewById(R.id.tab_content);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        RelativeLayout relative = (RelativeLayout) findViewById(R.id.tab_content);
                        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                        relative.removeAllViews();

                        switch (item.getItemId()) {
                            case R.id.action_watchlist:
                                FrameLayout watchlist_frame = (FrameLayout) inflater.inflate(R.layout.watchlist_tab, null, false);
                                relative.addView(watchlist_frame);
                                break;

                            case R.id.action_my_portfolio:
                                FrameLayout my_portfolio_frame = (FrameLayout) inflater.inflate(R.layout.my_portfolio_tab, null, false);
                                relative.addView(my_portfolio_frame);
                                break;

                            case R.id.action_trending:
                                FrameLayout trending_frame = (FrameLayout) inflater.inflate(R.layout.trending_tab, null, false);
                                relative.addView(trending_frame);
                                break;

                            case R.id.action_more:
                                FrameLayout more_frame = (FrameLayout) inflater.inflate(R.layout.more_tab, null, false);
                                relative.addView(more_frame);
                                break;

                        }
                        return true;
                    }
                });

        listView = (ListView) findViewById(R.id.list_view);

        items = new ArrayList<>();
        items.add("bitcoin");
        items.add("atc-coin");
        items.add("ripple");
        items.add("litecoin");
        items.add("ethereum");
        items.add("dash");
        items.add("bitconnect");
        items.add("lisk");
        items.add("tether");
        items.add("waves");
        items.add("bitshares");
        items.add("eos");
        items.add("metal");
        items.add("nexus");
        items.add("syscoin");

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
