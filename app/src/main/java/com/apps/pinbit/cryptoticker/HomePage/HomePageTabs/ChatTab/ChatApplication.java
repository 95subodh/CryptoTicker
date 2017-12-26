package com.apps.pinbit.cryptoticker.HomePage.HomePageTabs.ChatTab;

import android.app.Application;

import com.apps.pinbit.cryptoticker.Global.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by subyadav on 27/10/17.
 */

public class ChatApplication extends Application {

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}

