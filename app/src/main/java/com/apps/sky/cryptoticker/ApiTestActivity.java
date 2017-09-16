package com.apps.sky.cryptoticker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ApiTestActivity extends AppCompatActivity {
    private TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_test);

        Intent intent = getIntent();
        String crypto_name = intent.getExtras().getString("crypto");
        String url = "https://api.coinmarketcap.com/v1/ticker/"+crypto_name+"/?convert=INR";

        t1 = (TextView) findViewById(R.id.textViewName);
        new JSONTask().execute(url);
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
                String name = parentObject.getString("name");
                String price = parentObject.getString("price_inr");

                return name+" - "+price;

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
            t1.setText(result);
        }
    }
}
