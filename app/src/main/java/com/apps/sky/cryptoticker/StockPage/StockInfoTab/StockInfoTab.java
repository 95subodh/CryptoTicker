package com.apps.sky.cryptoticker.StockPage.StockInfoTab;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockInfoTab extends Fragment {

    private View rootView;
    private static String name, price, change, rank, cap, avlsup, totsup, lstupd;
    private TextView coinName, coinPrice, coinChange, coinRank, coinCap, coinAvailSupply, coinTotSupply, coinLstUpdate;
    private String url;
    public String crypto;
    MyGlobalsFunctions mgf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_info_tab, container, false);
        }

        url = "https://api.coinmarketcap.com/v1/ticker/"+crypto+"/?convert=INR";
        mgf = new MyGlobalsFunctions(getContext());
        new JSONTask().execute(url);

        return rootView;
    }

    private String commaSeperateInteger(String num){
        return NumberFormat.getNumberInstance(Locale.US).format(Float.valueOf(num));
    }

    private void fillInfoFromJSON() {

        coinName = getView().findViewById(R.id.coin_name);
        coinName.setText(name);
        coinPrice = getView().findViewById(R.id.coin_price);
        coinPrice.setText(commaSeperateInteger(price));
        coinAvailSupply = getView().findViewById(R.id.coin_avail_supply);
        coinAvailSupply.setText(commaSeperateInteger(avlsup));
        coinCap = getView().findViewById(R.id.coin_cap);
        coinCap.setText(commaSeperateInteger(cap));
        coinRank = getView().findViewById(R.id.coin_rank);
        coinRank.setText(rank);
        coinTotSupply = getView().findViewById(R.id.coin_tot_supply);
        coinTotSupply.setText(commaSeperateInteger(totsup));
        coinChange = getView().findViewById(R.id.coin_change);
        coinChange.setText(change);
        if (change.charAt(0) == '-') {
            coinChange.setTextColor(Color.RED);
        }
        else {
            coinChange.setTextColor(Color.parseColor("#ff99cc00"));
        }

        coinLstUpdate = getView().findViewById(R.id.coin_lst_update);
        coinLstUpdate.setText(mgf.getTimeFormattedDate(mgf.getEpochToNormalDateString(lstupd)));
    }

    public void setVals(String finalJson) throws JSONException {
        JSONArray jarr = new JSONArray(finalJson);

        JSONObject parentObject = jarr.getJSONObject(0);
        name = parentObject.getString("name");
        price = parentObject.getString("price_inr");
        change = parentObject.getString("percent_change_24h");
        rank = parentObject.getString("rank");
        cap = parentObject.getString("market_cap_inr");
        avlsup = parentObject.getString("available_supply");
        totsup = parentObject.getString("total_supply");
        lstupd = parentObject.getString("last_updated");
    }

    public class JSONTask extends AsyncTask<String,String, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                String finalJson = mgf.retieveStringFromFile(crypto,getString(R.string.crypto_info_dir));
                if (finalJson != null)
                    setVals(finalJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
//                final MyGlobalsFunctions myGlobalsFunctions = new MyGlobalsFunctions(getContext());
                String finalJson = mgf.fetchJSONasString(params[0]);
                mgf.storeStringToFile(crypto,getString(R.string.crypto_info_dir),finalJson);
                setVals(finalJson);
                return price;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
            fillInfoFromJSON();
        }
    }
}
