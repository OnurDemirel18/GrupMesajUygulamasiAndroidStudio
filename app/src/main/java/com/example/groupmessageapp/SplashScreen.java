package com.example.groupmessageapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    Button login, signup;
    FirebaseAuth firebaseAuth;
    Thread wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        login = (Button) findViewById(R.id.btn_login);
        signup = (Button) findViewById(R.id.btn_signup);
        firebaseAuth = FirebaseAuth.getInstance();
        SplashThread();

        if(firebaseAuth.getCurrentUser() != null){
            wait.start();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
            }
        });




    }

    public void SplashThread(){
        wait = new Thread(){
          @Override
          public void run() {
              try{
                  sleep(2000);
                  startActivity(new Intent(SplashScreen.this, MainActivity.class));
                  finish();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }

          }
        };
    }


}