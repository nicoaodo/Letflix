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

import com.example.letflix.model.CheckRoom;
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
    Button loginBtn, signupBtn, forgotBtn;
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
        forgotBtn = findViewById(R.id.forgotBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String email, password;
                email = emailBox.getText().toString();
                password = passwordBox.getText().toString();

                if(email.equals("") || password.equals("")){
                    Toast.makeText(LoginActivity.this, "Thông tin không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

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

//                                    if(DATAMAIN.typeLink == TypeLink.movie){
//                                        MovieData movieGet = null;
//                                        for (int i=0;i<DATAMAIN.movies.size();i++){
//                                            if(DATAMAIN.movies.get(i)._id.equals(DATAMAIN.valueLink))
//                                                movieGet = DATAMAIN.movies.get(i);
//                                        }
//                                        if(movieGet != null){
//                                            MovieDetailActivity.indexGet = movieGet;
//                                            startActivity(new Intent(LoginActivity.this, MovieDetailActivity.class));
//                                            DATAMAIN.typeLink = TypeLink.none;
//                                            DATAMAIN.valueLink = "";
//                                            return;
//                                        }
//                                    }else if(DATAMAIN.typeLink == TypeLink.invite){
//                                        //loading join room
//                                        APIInterface methods = RetrofitClient.getRetrofit().create(APIInterface.class);
//                                        Call<CheckRoom> callJoin = methods.leaveRoom(DATAMAIN.valueLink);
//                                        callJoin.enqueue(new Callback<CheckRoom>() {
//                                            @Override
//                                            public void onResponse(Call<CheckRoom> call, Response<CheckRoom> response) {
//                                                Log.d("dataGet", response.body().status + " Room code");
//                                                if(response.body().status){
//                                                    startActivity(new Intent(LoginActivity.this, PlayVideoTGTActivity.class));
//                                                    PlayVideoTGTActivity.isStart = false;
//                                                    PlayVideoTGTActivity.code = DATAMAIN.valueLink.toUpperCase();
//                                                }else
//                                                    Toast.makeText(LoginActivity.this, "Code Invalid!", Toast.LENGTH_LONG).show();
//                                            }
//
//                                            @Override
//                                            public void onFailure(Call<CheckRoom> call, Throwable t) {
//                                                Toast.makeText(LoginActivity.this, "Code Invalid!", Toast.LENGTH_LONG).show();
//                                                Log.d("dataGet", t.getMessage());
//                                            }
//                                        });
//                                    }

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

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
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