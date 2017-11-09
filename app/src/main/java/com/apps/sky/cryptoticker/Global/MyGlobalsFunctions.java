package com.apps.sky.cryptoticker.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import com.apps.sky.cryptoticker.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by subodhyadav on 08/10/17.
 */

public class MyGlobalsFunctions {
    private Context mContext;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // constructor
    public MyGlobalsFunctions(Context context){
        this.mContext = context;
    }

    public String getCurrencySymbol() {
        SharedPreferences sharedPreferences;
        sharedPreferences = mContext.getSharedPreferences("com.apps.sky.cryptoticker", Context.MODE_PRIVATE);
        String currency = sharedPreferences.getString(Constants.PREFERENCE_CURRENCY, "");
        switch (currency) {
            case "USD":
                return "$";
            case "INR":
                return "₹";
            case "JPY":
                return "￥";
            case "EUR":
                return "€";
            default:
                return "₹";
        }
    }

    public String commaSeperateInteger2(String num, Boolean... symbolCheck){
        if (num == null || "null".equals(num) || "-".equals(num)) return "-";
        Double x = Double.valueOf(num);

        String curr = "";
        if (symbolCheck.length>0 && symbolCheck[0]) {
            curr = getCurrencySymbol();
        }

        if (x >= 1000000000 || x <= -1000000000) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            x /= 1000000000.0;
            x =  Double.valueOf(newFormat.format(x));
            return curr + NumberFormat.getNumberInstance(Locale.US).format(x) + "B";
        }
        else if (x >= 1000000 || x <= -1000000) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            x /= 1000000.0;
            x =  Double.valueOf(newFormat.format(x));
            return curr + NumberFormat.getNumberInstance(Locale.US).format(x) + "M";
        }
        else if (x>=1000 || x<=-1000) {
            DecimalFormat newFormat = new DecimalFormat("#.#");
            x =  Double.valueOf(newFormat.format(x));
        }
        else if (x>=1 || x<=-1) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            x =  Double.valueOf(newFormat.format(x));
        }
        return curr + NumberFormat.getNumberInstance(Locale.US).format(x);
    }

    public String commaSeperateInteger(String num, Boolean... symbolCheck){
        if (num == null || "null".equals(num) || "-".equals(num)) return "-";
        Double x = Double.valueOf(num);
        if (x>=1000 || x<=-1000) {
            DecimalFormat newFormat = new DecimalFormat("#.#");
            x =  Double.valueOf(newFormat.format(x));
        }
        else if (x>=1 || x<=-1) {
            DecimalFormat newFormat = new DecimalFormat("#.##");
            x =  Double.valueOf(newFormat.format(x));
        }
        String curr = "";
        if (symbolCheck.length>0 && symbolCheck[0]) {
            curr = getCurrencySymbol();
        }
        return curr + NumberFormat.getNumberInstance(Locale.US).format(x);
    }

    public String getEpochToNormalDateString (String date) {
        if (date.contains(".")) {
            String date2 = new java.text.SimpleDateFormat("MM/dd").format(new java.util.Date ((long)(Double.valueOf(date)*1)));
            return date2;
        }
        else {
            String date2 = new java.text.SimpleDateFormat("MM/dd HH:mm:ss").format(new java.util.Date (Long.valueOf(date)*1000));
            return date2;
        }
    }

    private String convertDateToCalendarDate (String date) {
        Calendar calendar = Calendar.getInstance();
        try { calendar.setTime(formatter.parse(date)); }
        catch (Exception e) { e.printStackTrace(); }
        return calendar.getTime().toString();
    }

    public String getWeekDayFormattedDate(String originalDate) {
        return convertDateToCalendarDate(originalDate).substring(0, 11);
    }

    public String getTimeFormattedDate(String originalDate) {
        return convertDateToCalendarDate(originalDate).substring(4, 10) + ", " + convertDateToCalendarDate(originalDate).substring(11, 16);
    }

    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap convertImageURLtoBitmap(String ImageUrl, Boolean... store) {
        try {
            if (ImageUrl.contains("https://files")) {
                String temp = retieveStringFromFile(ImageUrl, mContext.getString(R.string.crypto_coin_icon_dir));
                if (temp!=null) {
                    return stringToBitMap(temp);
                }
            }
            URL url = new URL(ImageUrl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap icon = BitmapFactory.decodeStream(in);
            if (store.length>0) {
                storeStringToFile(ImageUrl, mContext.getString(R.string.crypto_coin_icon_dir), bitMapToString(icon));
            }
            return icon;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnectedOrConnecting());
    }

    public void storeStringToFile(String fileName, String fileDirectory, String outputString) {
        File myDir = mContext.getFilesDir();

        try {
            File secondFile = new File(myDir + "/"+ fileDirectory +"/", fileName);
            if (secondFile.getParentFile().mkdirs()) {
                secondFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(secondFile);

            fos.write(outputString.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String retieveStringFromFile(String fileName, String fileDirectory) {
        File myDir = mContext.getFilesDir();
        try {
            File secondInputFile = new File(myDir + "/"+ fileDirectory +"/", fileName);
            if (secondInputFile.exists()) {
                InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
                BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                r.close();
                secondInputStream.close();
                return total.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteMyFile(String fileName) {
        mContext.deleteFile(fileName);
    }

    public String fetchJSONasString(String uri) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
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

    public void storeListToFile(String fileName, String fileDirectory, ArrayList<String> arrayList) {
        StringBuilder csvList = new StringBuilder();
        for(String s : arrayList){
            csvList.append(s);
            csvList.append(",");
        }

        storeStringToFile(fileName, fileDirectory, csvList.toString());
    }

    public ArrayList<String> retrieveListFromFile(String fileName, String fileDirectory) {
        String csvList = retieveStringFromFile(fileName, fileDirectory);
        String[] items = {};
        if (csvList!=null) {
            items = csvList.split(",");
        }
        ArrayList<String> list = new ArrayList<>();
        for (String i : items) {
            if (i.length()>0) {
                list.add(i);
            }
        }

        return list;
    }
}
