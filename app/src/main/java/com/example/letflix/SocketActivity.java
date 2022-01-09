package com.example.letflix;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class SocketActivity extends AppCompatActivity {
    WebView testWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        testWebView = findViewById(R.id.testWebView);
        testWebView.loadUrl("http://20.192.4.125:5000");
        testWebView.getSettings().setJavaScriptEnabled(true);
        testWebView.setWebChromeClient (new WebChromeClient());
    }






}