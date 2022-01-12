package com.example.letflix;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.letflix.model.DATAMAIN;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PlayVideoTGTActivity extends AppCompatActivity {
    public static String code = "Test";
    public static boolean isStart = false;
    public static String url;

    private WebView webVideo, webChat;
    private String URLVideo = "http://20.192.4.125:8000/start/";
    private String URLChat = "http://20.192.4.125:3000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_t_g_t);
        webVideo = findViewById(R.id.viewPlay);
        webChat = findViewById(R.id.viewChat);

        WebSettings webSettings = webVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webVideo.setWebViewClient(new WebViewClient());
        webVideo.setWebChromeClient(new ChromeCreate());

        WebSettings webSettingsChat = webChat.getSettings();
        webSettingsChat.setJavaScriptEnabled(true);
        webChat.setWebViewClient(new WebViewClient());
        webChat.setWebChromeClient(new ChromeCreate());

        if(isStart){
            try {
                url = URLEncoder.encode(url, String.valueOf(StandardCharsets.UTF_8));
                if(url.length()==0) return;
                url = URLVideo+code+"/"+url;
                webVideo.loadUrl(url);
                Log.d("dataGet", url +" Video");
//                webVideo.setVisibility(View.VISIBLE);

                url = URLChat+code+"/"+ DATAMAIN.userLogin.getName();
                webChat.loadUrl(url);
//                webChat.setVisibility(View.VISIBLE);

                Log.d("dataGet", url +" chat");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    class ChromeCreate extends WebChromeClient{
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeCreate() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webVideo.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webVideo.restoreState(savedInstanceState);
    }
}
