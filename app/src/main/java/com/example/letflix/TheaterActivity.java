package com.example.letflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                Toast.makeText(context, joinCode, Toast.LENGTH_LONG).show();
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