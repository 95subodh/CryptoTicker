package com.apps.sky.cryptoticker.StockPage.StockPredictionTab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.sky.cryptoticker.GlobalFunctions.MyGlobalsFunctions;
import com.apps.sky.cryptoticker.R;

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
                myGlobalDemo.storeStringToFile("mysecondfile","text",storeDemoEditText.getText().toString());
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
}