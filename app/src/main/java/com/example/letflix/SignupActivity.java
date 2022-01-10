package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.PostResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    EditText emailBox, passwordBox, nameBox;
    Button loginBtn, signupBtn;

    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox);
        nameBox = findViewById(R.id.namebox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.createBtn);
        signupBtn = findViewById(R.id.loginbtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass, name;
                email = emailBox.getText().toString();
                pass = passwordBox.getText().toString();
                name = nameBox.getText().toString();

                final User user = new User();
                user.setEmail(email);
                user.setPass(pass);
                user.setName(name);

                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {


                            APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
                            Call<PostResponse> call = methods.setUser(user);
                            call.enqueue(new Callback<PostResponse>() {
                                @Override
                                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                    DATAMAIN.userLogin = user;

                                    Gson gson = new Gson();
                                    String jsonString = gson.toJson(user);
                                    boolean success = GetCacheDir.writeAllCachedText(DATAMAIN.contextCache, DATAMAIN.CACHEACCOUNT, jsonString);

                                    if(success)
                                        Log.d("dataGet", "Signup success: "+response.body().message);
                                }
                                @Override
                                public void onFailure(Call<PostResponse> call, Throwable t) {
                                    Log.d("dataGet", t.getMessage());
                                }
                            });

                            Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoadingActivity.class));
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }

    private void hideNavigationBar(){
        //Hide nav gesture
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
