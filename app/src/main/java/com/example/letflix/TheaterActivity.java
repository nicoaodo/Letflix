package com.example.letflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letflix.model.CheckRoom;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.PostResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheaterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theater);

        Context context = this;

        TextView codeBox = findViewById(R.id.codeBox);
        Button joinBtn = findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String joinCode = codeBox.getText().toString();

                //loading join room
                APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
                Call<CheckRoom> call = methods.leaveRoom(joinCode);
                call.enqueue(new Callback<CheckRoom>() {
                    @Override
                    public void onResponse(Call<CheckRoom> call, Response<CheckRoom> response) {
                        Log.d("dataGet", response.body().status + " Room code");
                        if(response.body().status){
                            startActivity(new Intent(TheaterActivity.this, PlayVideoTGTActivity.class));
                            PlayVideoTGTActivity.isStart = false;
                            PlayVideoTGTActivity.code = joinCode.toUpperCase();
                        }else
                            Toast.makeText(context, "Room not found!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<CheckRoom> call, Throwable t) {
                        Toast.makeText(context, "Room not found!", Toast.LENGTH_LONG).show();
                        Log.d("dataGet", t.getMessage());
                    }
                });
            }
        });







        //logo click listener
        ImageView btnLogo = findViewById(R.id.btnHome);
        btnLogo.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
                startActivity(new Intent(TheaterActivity.this, DashboardActivity.class));

            }
        });

        //bottom view click listener
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.itemHome:
                        startActivity(new Intent(context, DashboardActivity.class));
                        break;
                    case R.id.itemRap:

                        break;
                    case R.id.itemLogout:
                        boolean success = GetCacheDir.writeAllCachedText(DATAMAIN.contextCache, DATAMAIN.CACHEACCOUNT, "");
                        if(success)
                            startActivity(new Intent(context, LoginActivity.class));
                        break;
                }
                return false;
            }
        });


    }
}