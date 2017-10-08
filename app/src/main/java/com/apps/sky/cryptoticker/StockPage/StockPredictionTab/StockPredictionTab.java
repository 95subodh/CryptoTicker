package com.apps.sky.cryptoticker.StockPage.StockPredictionTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by subodhyadav on 16/09/17.
 */

public class StockPredictionTab extends Fragment {
    Button storeDemoButton, storeDemoRetireveButton;
    TextView storeDemoTextView, storeDemoRetrieveTextView;
    EditText storeDemoEditText;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.stock_prediction_tab, container, false);
        }

        storeDemoButton = rootView.findViewById(R.id.storageDemobutton);
        storeDemoTextView = rootView.findViewById(R.id.storageDemotextView);
        storeDemoEditText = rootView.findViewById(R.id.storageDemoeditText);
        final MyGlobalsFunctions myGlobalDemo = new MyGlobalsFunctions(getContext());
        storeDemoButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myGlobalDemo.storeStringFromFile("mysecondfile","text",storeDemoEditText.getText().toString());
            }
        });
        storeDemoRetireveButton = rootView.findViewById(R.id.storeDemoRetrieveButton);
        storeDemoRetrieveTextView = rootView.findViewById(R.id.storageDemoRetrieveTextView);
        storeDemoRetireveButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String fetchedString = myGlobalDemo.retieveStringFromFile("mysecondfile","text");
                storeDemoRetrieveTextView.setText(fetchedString);
            }
        });

        return rootView;
    }

    private void storeMyFilePlease(String outputString) {
        String filename = "mysecondfile";
        File myDir = getContext().getFilesDir();

        try {
            File secondFile = new File(myDir + "/text2/", filename);
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

    private void retieveMyFilePlease() {
        String filename = "mysecondfile";
        File myDir = getContext().getFilesDir();
        try {
            File secondInputFile = new File(myDir + "/text2/", filename);
            InputStream secondInputStream = new BufferedInputStream(new FileInputStream(secondInputFile));
            BufferedReader r = new BufferedReader(new InputStreamReader(secondInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            secondInputStream.close();
            storeDemoRetrieveTextView.setText(total);
            Log.d("File", "File contents: " + total);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMyFile(String fileName){
        getContext().deleteFile(fileName);
    }
}