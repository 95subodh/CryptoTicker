package com.apps.sky.cryptoticker.HomePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.apps.sky.cryptoticker.Global.Constants;
import com.apps.sky.cryptoticker.Global.ConstantsCrypto;
import com.apps.sky.cryptoticker.HomePage.BottomNavigationBar.BottomNavigationViewHelper;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.ChatTab.ChatTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MoreTab.MoreTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.MyPortfolioTab.MyPortfolioTab;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.ViewPagerAdapter;
import com.apps.sky.cryptoticker.HomePage.HomePageTabs.WatchlistTab.WatchlistTab;
import com.apps.sky.cryptoticker.R;
import com.apps.sky.cryptoticker.StockPage.StockPageActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items;
    BottomNavigationView bottomNavigationView;
    RelativeLayout relative;
    boolean onCreate = false;
    View mainView;
    SearchView searchView;
    MenuItem searchItem;

    ViewPager viewPager;
    MenuItem prevMenuItem;

    WatchlistTab watchlistTab;
    MyPortfolioTab myPortfolioTab;
    ChatTab chatTab;
    MoreTab moreTab;

    HashMap<String, String> searchMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onCreate = true;
        Intent intent = getIntent();

        relative = findViewById(R.id.tab_content);
        listView = findViewById(R.id.list_view);
        mainView = findViewById(R.id.main_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        String tab = "";
        if (intent.hasExtra("tab")) {
            try {
                tab = intent.getExtras().getString("tab");
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        viewPager = findViewById(R.id.viewpager1);
        viewPager.setOffscreenPageLimit(5);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.action_watchlist:
                            viewPager.setCurrentItem(0);
                            break;

                        case R.id.action_my_portfolio:
                            viewPager.setCurrentItem(1);
                            break;

                        case R.id.action_chat:
                            viewPager.setCurrentItem(2);
                            break;

                        case R.id.action_more:
                            viewPager.setCurrentItem(3);
                            break;
                    }
                    return false;
                }
            });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager();
        assignTab(tab);

        items = new ArrayList<>();
        items.addAll(Arrays.asList(Constants.cryptoIDList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = searchMap.get(listView.getItemAtPosition(i).toString());
                String text = temp.replaceAll("_", "-");
                Intent intent = new Intent(MainActivity.this, StockPageActivity.class);
                intent.putExtra("cryptoID", "" + text.toLowerCase());
                startActivity(intent);
            }
        });
    }

    private void setupViewPager()
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        watchlistTab=new WatchlistTab();
        myPortfolioTab=new MyPortfolioTab();
        chatTab=new ChatTab();
        moreTab=new MoreTab();
        adapter.addFragment(watchlistTab);
        adapter.addFragment(myPortfolioTab);
        adapter.addFragment(chatTab);
        adapter.addFragment(moreTab);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onCreate) onCreate = false;
        else {
            if (searchView.isAttachedToWindow()) {
                searchView.onActionViewCollapsed();
                searchView.setQuery("", false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("no", null).show();
    }

    private void assignTab(String tab) {
        switch (tab) {
            case "watchlist" :
                viewPager.setCurrentItem(0);
                bottomNavigationView.setSelectedItemId(R.id.action_watchlist);
                break;

            case "my_portfolio" :
                viewPager.setCurrentItem(1);
                bottomNavigationView.setSelectedItemId(R.id.action_my_portfolio);
                break;

            case "chat" :
                viewPager.setCurrentItem(2);
                bottomNavigationView.setSelectedItemId(R.id.action_chat);
                break;

            case "more" :
                viewPager.setCurrentItem(3);
                bottomNavigationView.setSelectedItemId(R.id.action_more);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        searchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    searchItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> templist = new ArrayList<>();
                System.out.println(newText);

                if (newText.length() > 0) {
                    for (String iter : items) {
                        String temp = ConstantsCrypto.cryptoMap.get(iter)[0] + " (" + ConstantsCrypto.cryptoMap.get(iter)[1] + ")";
                        searchMap.put(temp, iter);

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
