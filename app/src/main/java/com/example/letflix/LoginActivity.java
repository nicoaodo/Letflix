package com.example.letflix;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.MovieData;
import com.example.letflix.model.PostResponse;
import com.example.letflix.model.TypeLink;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    Button loginBtn, signupBtn;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang kiểm tra thông tin");
        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.createBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful())
                        {
                            APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
                            Call<PostResponse> call = methods.setUser(new User("",email,password));
                            call.enqueue(new Callback<PostResponse>() {
                                @Override
                                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                                    User user = new User(response.body().message, email, password);
                                    DATAMAIN.userLogin = user;

                                    Gson gson = new Gson();
                                    String jsonString = gson.toJson(user);
                                    boolean success = GetCacheDir.writeAllCachedText(DATAMAIN.contextCache, DATAMAIN.CACHEACCOUNT, jsonString);

                                    if(success)
                                        Log.d("dataGet", "Login success: "+response.body().message);
                                }

                                @Override
                                public void onFailure(Call<PostResponse> call, Throwable t) {
                                    Log.d("dataGet", t.getMessage());
                                }
                            });

                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            database.collection("Users").document("userData").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);
                                    Log.d("dataGet", "Cached document data: " + user.getName());
                                }
                            });
//
                            //boolean success = GetCacheDir.writeAllCachedText(LoginActivity.this, DATAMAIN.CACHEACCOUNT, auth.getCurrentUser().getDisplayName());
                           startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                           //startActivity(new Intent(LoginActivity.this, SocketActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
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