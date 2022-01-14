package com.example.letflix;

import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
    PlayVideoTGTActivity playVideoTGTActivity;
    public JavaScriptInterface(PlayVideoTGTActivity playVideoTGTActivity) {
        this.playVideoTGTActivity = playVideoTGTActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
        playVideoTGTActivity.onPeerConnected();
    }
}
