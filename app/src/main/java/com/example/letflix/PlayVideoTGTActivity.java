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

import com.example.letflix.model.CheckRoom;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.PostResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayVideoTGTActivity extends AppCompatActivity {
    public static String code = "Test";
    public static boolean isStart = false;
    public static String url;

    private WebView webVideo, webChat;
    private String URLVideo = "http://20.192.4.125:8000/start/";
    private String URLVideoJoin = "http://20.192.4.125:8000/watch/";
    private String URLChat = "http://20.192.4.125:3000/";

    protected void onDestroy() {
        //khi thằng chỉ phòng rời thì sẽ xóa code id
        //và code cũ k join được nữa
        //nhưng mấy thằng vẫn đang còn trong phòng sẽ vẫn xem bình thường k ảnh hưởng
        //chỉ là k join đc nữa thôi
        if(!isStart) return;
        //loading remove room
        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
        Call<CheckRoom> call = methods.leaveRoom(code);
        call.enqueue(new Callback<CheckRoom>() {
            @Override
            public void onResponse(Call<CheckRoom> call, Response<CheckRoom> response) {
                Log.d("dataGet", response.body().status + " Room remove");
            }

            @Override
            public void onFailure(Call<CheckRoom> call, Throwable t) {
                Log.d("dataGet", t.getMessage());
            }
        });

        Log.d("dataGet", "exit");
        super.onDestroy();
    }

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

        try {
            if(isStart){
                    url = URLEncoder.encode(url, String.valueOf(StandardCharsets.UTF_8));
                    if(url.length()==0) return;
                    url = URLVideo+code+"/"+url;
                    webVideo.loadUrl(url);
                    Log.d("dataGet", url +" Video");

                    url = URLChat+code+"/"+ DATAMAIN.userLogin.getName();
                    webChat.loadUrl(url);
                    Log.d("dataGet", url +" chat");

            }else {
                url = URLVideoJoin + code;
                webVideo.loadUrl(url);
                Log.d("dataGet", url +" Video");

                url = URLChat+code+"/"+ DATAMAIN.userLogin.getName();
                webChat.loadUrl(url);
                Log.d("dataGet", url +" chat");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
