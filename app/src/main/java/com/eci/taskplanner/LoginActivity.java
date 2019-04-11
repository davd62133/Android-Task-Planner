package com.eci.taskplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    public LoginActivity instance = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private boolean verify(int id){
        boolean notCorrect = false;
        if(((EditText)findViewById(id)).getText().toString().equals("")){
            notCorrect = true;
            ((EditText) findViewById(id)).setError("This field con not be blank");
        }
        return  notCorrect;
    }

    public void onClick(View v){
        boolean login = true;
        if(verify(R.id.usernameText)){
            login = false;
        }
        if(verify(R.id.passwordText)){
            login = false;
        }
        if(login){
            fetchAccessToken();
        }
    }


    private void fetchAccessToken(){

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = Controller.getRetrofit();

                AuthService authService = retrofit.create(AuthService.class);

                String email = ((EditText) findViewById(R.id.usernameText)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordText)).getText().toString();

                try {
                    Response<Token> response = authService.sighIn(new LoginWrapper(email,password)).execute();
                    Token token = response.body();
                    if(token != null){
                        SharedPreferences sharedPref =
                            getSharedPreferences( getString( R.string.preference_file_key ), Context.MODE_PRIVATE );
                        sharedPref.edit().putString(LaunchActivity.TOKEN_KEY, token.getAccessToken());
                        Intent intent = new Intent(getInstance(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public LoginActivity getInstance(){
        return instance;
    }

}
