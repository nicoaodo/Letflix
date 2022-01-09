package com.example.letflix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import android.webkit.WebView;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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