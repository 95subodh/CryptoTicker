package com.apps.sky.cryptoticker.HomePage.HomePageTabs.ChatTab;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.sky.cryptoticker.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatTab extends Fragment {

    final String MESSAGES_ENDPOINT = "http://pusher-chat-demo.herokuapp.com";

    private View rootView;
    EditText messageInput;
    Button sendButton;
    String username = "anonymous";

    public ChatTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.chat_tab, container, false);
        }

        // get our input field by its ID
        messageInput = (EditText) rootView.findViewById(R.id.message_input);


        // get our button by its ID
        sendButton = (Button) rootView.findViewById(R.id.send_button);

        // set its click listener
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMessage();
            }
        });
        return rootView;
    }

    private void postMessage() {
        String text = messageInput.getText().toString();

        // return if the text is blank
        if (text.equals("")) {
            return;
        }

        RequestParams params = new RequestParams();

        // set our JSON object
        params.put("text", text);
        params.put("name", username);
        params.put("time", new Date().getTime());

        // create our HTTP client
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(MESSAGES_ENDPOINT + "/messages", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageInput.setText("");
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
            }
        });
    }

}
