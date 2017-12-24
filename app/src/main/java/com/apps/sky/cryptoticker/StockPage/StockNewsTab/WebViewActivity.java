package com.apps.sky.cryptoticker.StockPage.StockNewsTab;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apps.sky.cryptoticker.R;

/**
 * Created by subodhyadav on 12/10/17.
 */

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    String news_url;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent intent = getIntent();
        try {
            news_url = intent.getExtras().getString("url");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        webView = findViewById(R.id.webViewWindow);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.loadUrl(news_url);
    }
}
