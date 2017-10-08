package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockNewsTab extends Fragment {

    private View rootView;

    public  String crypto;
    private String key = "23b11fdf774042e6bd138b5448af5403";
    private String source = "sources=google-news";
    private String language = "en";
    private String status, url;
    ArrayList<NewsObject> news = new ArrayList<NewsObject>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    final MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_news_tab, container, false);
        }

        url = "http://beta.newsapi.org/v2/everything?q=" + crypto + "&apiKey=" + key + "&language=" + language;
        new JSONTask().execute(url);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((NewsRecyclerViewAdapter) adapter).setOnItemClickListener(new NewsRecyclerViewAdapter
//                .MyClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//                System.out.println(" Clicked on Item " + position);
//            }
//        });
//    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                JSONObject parentObject = new JSONObject(finalJson);
                status = parentObject.getString("status");
                JSONArray articles = parentObject.getJSONArray("articles");

                //*********      this contains 10 news articles       ***********//
                for (int i=0 ; i<articles.length(); i++){

                    JSONObject obj = articles.getJSONObject(i);
                    NewsObject currentNews = new NewsObject();

                    currentNews.setTitle(obj.getString("title"));
                    currentNews.setPublishedDate(obj.getString("publishedAt"));
                    currentNews.setURL(obj.getJSONObject("source").getString("name"));
//                    currentNews.setImage(obj.getString("urlToImage"));

                    news.add(i, currentNews);
                }
                return finalJson;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter = new NewsRecyclerViewAdapter(news);
            recyclerView.setAdapter(adapter);
        }
    }
}
