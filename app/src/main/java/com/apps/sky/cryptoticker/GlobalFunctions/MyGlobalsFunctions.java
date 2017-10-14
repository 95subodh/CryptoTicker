package com.apps.sky.cryptoticker.GlobalFunctions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by subodhyadav on 08/10/17.
 */

public class MyGlobalsFunctions {
    Context mContext;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // constructor
    public MyGlobalsFunctions(Context context){
        this.mContext = context;
    }

    public MyGlobalsFunctions() {}

    public String getUserName(){
        return "test";
    }

    public String commaSeperateInteger(String num){
        return NumberFormat.getNumberInstance(Locale.US).format(Float.valueOf(num));
    }

    public String getEpochToNormalDateString (String date) {
        Log.d("EpochDate: ", (new java.util.Date (Integer.valueOf(date)*1000)).toString());
        return (new java.util.Date (Integer.valueOf(date)*1000)).toString();
    }

    private String convertDateToCalendarDate (String date) {
        Calendar calendar = Calendar.getInstance();
        try { calendar.setTime(formatter.parse(date)); }
        catch (Exception e) { System.out.println(e); }
        Log.d("ConvertedDate: ", calendar.getTime().toString());
        return calendar.getTime().toString();
    }

    public String getWeekDayFormattedDate(String originalDate) {
        return convertDateToCalendarDate(originalDate).substring(0, 11);
    }

    public String getTimeFormattedDate(String originalDate) {
        Log.d("TimeFormattedDate: ", convertDateToCalendarDate(originalDate).substring(4, 10) + ", " + convertDateToCalendarDate(originalDate).substring(11, 16));
        return convertDateToCalendarDate(originalDate).substring(4, 10) + ", " + convertDateToCalendarDate(originalDate).substring(11, 16);
    }

    public Bitmap convertImageURLtoBitmap(String ImageUrl) {
        try {
            URL url = new URL(ImageUrl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            return mIcon;
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
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
            InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
            BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            secondInputStream.close();
//            Log.d("File", "File contents: " + total);
            return total.toString();
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
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            String finalJson = buffer.toString();
            return finalJson;

        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        for(int i=0; i < items.length; i++){
            list.add(items[i]);
        }

        return list;
    }
}
