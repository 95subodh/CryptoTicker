package com.apps.sky.cryptoticker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockTab2 extends Fragment {

    private String url;
    public String crypto;
    private String key = "23b11fdf774042e6bd138b5448af5403";
    private String source = "sources=google-news";
    private String language = "en";
    private String status;
    private String title, link, linkToImage, description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.stock_tab_2, container, false);
        url = "http://beta.newsapi.org/v2/everything?q=" + crypto + "&apiKey=" + key + "&language=" + language;
        new JSONTask().execute(url);
        return rootView;
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
                for (int i=0 ; /*i<articles.length()*/ i<1; i++){
                    JSONObject obj = articles.getJSONObject(i);

                    title = obj.getString("title");
                    link = obj.getString("url");
                    description = obj.getString("description");
                    linkToImage = obj.getString("urlToImage");
                }
                    return finalJson;
//                return name+" - "+price;

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
            TextView t1,t2,t3,t4;
            t1 = getView().findViewById(R.id.newsUrl);
            t2 = getView().findViewById(R.id.newsImageUrl);
            t3 = getView().findViewById(R.id.newsTitle);
            t4 = getView().findViewById(R.id.newsDescription);
            t1.setText(link);
            t2.setText(linkToImage);
            t3.setText(title);
            t4.setText(description);
        }
    }
}
