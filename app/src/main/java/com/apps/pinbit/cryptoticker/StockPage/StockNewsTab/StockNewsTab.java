package com.apps.pinbit.cryptoticker.StockPage.StockNewsTab;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.pinbit.cryptoticker.Global.MyGlobalsFunctions;
import com.apps.pinbit.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.min;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockNewsTab extends Fragment {

    private View rootView;

    public String cryptoID;
    String key = "23b11fdf774042e6bd138b5448af5403";
    String source = "sources=google-news";
    String language = "en";
    String status, url;
    ArrayList<NewsObject> news = new ArrayList<>();
    private ArrayList<Drawable> mNewsIcons = new ArrayList<>();

    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    MyGlobalsFunctions myGlobalsFunctions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_news_tab, container, false);
        }

        url = "http://beta.newsapi.org/v2/everything?q=" + cryptoID + "&apiKey=" + key + "&language=" + language;
        myGlobalsFunctions = new MyGlobalsFunctions(getContext());

        if (myGlobalsFunctions.isNetworkConnected()) {
            new JSONTask().execute(url);
        }

        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_1));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_2));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_3));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_4));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_5));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_6));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_7));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_8));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_9));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_10));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_11));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_12));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_13));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_14));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_15));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_16));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_17));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_18));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_19));
        mNewsIcons.add(rootView.getResources().getDrawable(R.drawable.news_image_20));

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    public void setVals(String finalJson) throws JSONException {
        JSONObject parentObject = new JSONObject(finalJson);
        status = parentObject.getString("status");
        JSONArray articles = parentObject.getJSONArray("articles");

        //*********      this contains 10 news articles       ***********//
        for (int i = 0; i < min(articles.length(), 10); i++) {

            JSONObject obj = articles.getJSONObject(i);
            NewsObject currentNews = new NewsObject();
            currentNews.setContext(getContext());

            currentNews.setTitle(obj.getString("title"));
            currentNews.setPublishedDate(obj.getString("publishedAt"));
            currentNews.setAuthor(obj.getJSONObject("source").getString("name"));
            currentNews.setURL(obj.getString("url"));

            int hash = 7;
            String title = obj.getString("title") + obj.getString("url");
            for (int j = 0, len = title.length(); j < len; j++) {
                hash = title.codePointAt(j) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mNewsIcons.size());
            currentNews.setImage(mNewsIcons.get(index));
//            currentNews.setImage(obj.getString("urlToImage"));

            news.add(i, currentNews);
        }
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (myGlobalsFunctions.isNetworkConnected()) {
                    String finalJson = myGlobalsFunctions.fetchJSONasString(params[0]);
                    setVals(finalJson);
                    return finalJson;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            adapter = new NewsRecyclerViewAdapter(news);
            recyclerView.setAdapter(adapter);
        }
    }
}
