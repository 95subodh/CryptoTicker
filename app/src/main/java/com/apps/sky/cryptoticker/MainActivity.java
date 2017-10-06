package com.apps.sky.cryptoticker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> items;
    BottomNavigationView bottomNavigationView;
    View myPortfolioView, myCurrentPortfolioView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String url;
    public String crypto;
    ArrayList<MyPortfolioObject> myPortfolioArray = new ArrayList<MyPortfolioObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        LayoutInflater inflater = LayoutInflater.from(this);

        RelativeLayout current_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_current_portfolio_card, null, false);
        RelativeLayout current_portfolio_view = (RelativeLayout) findViewById(R.id.my_current_portfolio_view);
        current_portfolio_view.addView(current_portfolio_layout);
        myCurrentPortfolioView = (View) current_portfolio_view;

        RelativeLayout my_portfolio_layout = (RelativeLayout) inflater.inflate(R.layout.my_portfolio_card, null, false);
        RelativeLayout my_portfolio_view = (RelativeLayout) findViewById(R.id.my_portfolio_view);
        my_portfolio_view.addView(my_portfolio_layout);
        myPortfolioView = (View) my_portfolio_view;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray);
        recyclerView.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_my_portfolio:
                                Toast.makeText(MainActivity.this, "My Portfolio Clicked", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_trending:
                                Toast.makeText(MainActivity.this, "Trending Clicked", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_chat:
                                Toast.makeText(MainActivity.this, "Chat Clicked", Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.action_settings:
                                Toast.makeText(MainActivity.this, "Settings Clicked", Toast.LENGTH_SHORT).show();
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

        for (int i = 0; i < 5; ++i) {
            crypto = items.get(i);
            url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
            new MainActivity.JSONTask().execute(url);
        }

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
                MyPortfolioObject currency_details = new MyPortfolioObject();
                currency_details.setTitle(parentObject.getString("name"));
                currency_details.setCurrentPrice("$" + parentObject.getString("price_inr"));
                currency_details.setMyProfit("34.08%");

                myPortfolioArray.add(currency_details);
                return finalJson;

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

            adapter = new MyPortfolioRecyclerViewAdapter(myPortfolioArray);
            recyclerView.setAdapter(adapter);
        }
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
                myCurrentPortfolioView.setVisibility(View.INVISIBLE);
                myPortfolioView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                bottomNavigationView.setVisibility(View.VISIBLE);
                myCurrentPortfolioView.setVisibility(View.VISIBLE);
                myPortfolioView.setVisibility(View.VISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}
