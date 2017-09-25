package com.apps.sky.cryptoticker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockTab2 extends Fragment {

    private View rootView;
    private Bitmap btmp;

    public  String crypto;
    private String key = "23b11fdf774042e6bd138b5448af5403";
    private String source = "sources=google-news";
    private String language = "en";
    private String status, title, link, linkToImage, publishedDate, url;
    ArrayList<Object> news = new ArrayList<Object>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);

        rootView = inflater.inflate(R.layout.stock_tab_2, container, false);
        url = "http://beta.newsapi.org/v2/everything?q=" + crypto + "&apiKey=" + key + "&language=" + language;
        new JSONTask().execute(url);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_stock_news_tab, menu);
    }

    public String convertDate(String originalDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(originalDate));
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return calendar.getTime().toString().substring(0, 11);
    }

    public Bitmap getbmpfromURL(String surl){
        try {
            URL url = new URL(surl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            return  mIcon;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public class NewsObject {
        private String title, publishedDate, link;
        private Bitmap btmp;

        NewsObject(String title1, String publishedDate1, String link1, Bitmap btmp1) {
            title = title1;
            publishedDate = publishedDate1;
            link = link1;
            btmp = btmp1;
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
                JSONObject parentObject = new JSONObject(finalJson);
                status = parentObject.getString("status");
                JSONArray articles = parentObject.getJSONArray("articles");

                //*********      this contains 10 news articles       ***********//
                for (int i=0 ; i<articles.length(); i++){

                    JSONObject obj = articles.getJSONObject(i);
                    JSONObject src = obj.getJSONObject("source");

                    title = obj.getString("title");
                    publishedDate = obj.getString("publishedAt");
                    linkToImage = obj.getString("urlToImage");
                    btmp = getbmpfromURL(linkToImage);
                    link = src.getString("name");

                    Object currentNews = new NewsObject(title, link, publishedDate, btmp);
                    news.add(i, currentNews);
                }

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
            TextView t1,t3,t4;
            ImageView t2;
            t1 = getView().findViewById(R.id.newsUrl);
            t2 = getView().findViewById(R.id.newsImageUrl);
            t3 = getView().findViewById(R.id.newsTitle);
            t4 = getView().findViewById(R.id.newsDate);
            t1.setText(link);
            t2.setImageBitmap(btmp);
            t3.setText(title);
            t4.setText(convertDate(publishedDate));

//            adapter = new RecyclerAdapter(news);
//            recyclerView.setAdapter(adapter);
        }
    }
}
