package com.example.letflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.letflix.model.CheckRoom;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.PostResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Permission;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayVideoTGTActivity extends AppCompatActivity {
    public static String code = "Test";
    public static boolean isStart = false;
    public static String url;
    private CardView toggleChat, toggle_mic, toggle_camera, btnShare, videoSection, chatSection, meetSection;
    private ImageView chatCollapse;
    private WebView webVideo, webChat, webMeet;
    private RelativeLayout buttonPanel;
    private Permission permissionCamera, permissionAudio;
    private String URLVideo = "http://20.192.4.125:8000/start/";
    private String URLVideoJoin = "http://20.192.4.125:8000/watch/";
    private String URLChat = "http://20.192.4.125:3000/";
    private String URLMeet = "https://20.192.4.125/";
    private static final int REQUEST_CODE = 10;

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onDestroy() {
//
//        //khi thằng chỉ phòng rời thì sẽ xóa code id
//        //và code cũ k join được nữa
//        //nhưng mấy thằng vẫn đang còn trong phòng sẽ vẫn xem bình thường k ảnh hưởng
//        //chỉ là k join đc nữa thôi
//        if(!isStart) return;
//        //loading remove room
//        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
//        Call<CheckRoom> call = methods.leaveRoom(code);
//        call.enqueue(new Callback<CheckRoom>() {
//            @Override
//            public void onResponse(Call<CheckRoom> call, Response<CheckRoom> response) {
//                Log.d("dataGet", response.body().status + " Room remove");
//            }
//
//            @Override
//            public void onFailure(Call<CheckRoom> call, Throwable t) {
//                Log.d("dataGet", t.getMessage());
//            }
//        });
//
//        Log.d("dataGet", "exit");
//        //super.onDestroy();
//    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_t_g_t);
        DATAMAIN.typeLink = null;

        checkForPermission();



        buttonPanel = findViewById(R.id.buttonPanel);
        videoSection = findViewById(R.id.videoSection);
        chatSection = findViewById(R.id.chatSection);
        meetSection = findViewById(R.id.meetSection);

        webMeet = findViewById(R.id.viewMeet);
        webVideo = findViewById(R.id.viewPlay);
        webChat = findViewById(R.id.viewChat);

        videoSection.setVisibility(View.VISIBLE);
        chatSection.setVisibility(View.INVISIBLE);

        WebSettings webSettingsMeet = webMeet.getSettings();
        webSettingsMeet.setJavaScriptEnabled(true);
        webSettingsMeet.setMediaPlaybackRequiresUserGesture(false);
//        webMeet.addJavascriptInterface();
        webMeet.setWebViewClient(new WebViewClient());
        webMeet.setWebChromeClient(new ChromeCreate());


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

                    url = URLMeet+code;
                    webMeet.loadUrl(url);
                    Log.d("dataGet", url +" meet");

            }else {
                url = URLVideoJoin + code;
                webVideo.loadUrl(url);
                Log.d("dataGet", url +" Video");

                url = URLChat+code+"/"+ DATAMAIN.userLogin.getName();
                webChat.loadUrl(url);
                Log.d("dataGet", url +" chat");

                url = URLMeet+code;
                webMeet.loadUrl(url);
                Log.d("dataGet", url +" meet");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        //share button action
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                String shareSub = "Mời bạn xem.";
                String shareBody = "Xem cùng "+ DATAMAIN.userLogin.getName() + " trên Letflix.\nĐến xem ngay: http://www.letflix.com/invite/"+ code;
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });

        toggleChat = findViewById(R.id.toggle_chat);
        toggleChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatSection.setVisibility(View.VISIBLE);
                buttonPanel.setVisibility(View.INVISIBLE);
                meetSection.setVisibility(View.INVISIBLE);
            }
        });

        chatCollapse = findViewById(R.id.chatCollapse);
        chatCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPanel.setVisibility(View.VISIBLE);
                chatSection.setVisibility(View.INVISIBLE);
                meetSection.setVisibility(View.VISIBLE);
            }
        });



        toggle_camera = findViewById(R.id.toggle_camera);
        toggle_mic = findViewById(R.id.toggle_mic);





    }

    private void checkForPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            requestPermissions(permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted" , Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Denied" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onPeerConnected() {
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
