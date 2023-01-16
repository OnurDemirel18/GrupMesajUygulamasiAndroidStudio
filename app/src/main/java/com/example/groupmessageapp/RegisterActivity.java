package com.example.groupmessageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    Button login, signup;
    EditText email, password;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        login = (Button) findViewById(R.id.r_btn_login);
        signup = (Button) findViewById(R.id.r_btn_signup);
        email = (EditText) findViewById(R.id.r_email);
        password = (EditText) findViewById(R.id.r_password);
        firebaseAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupClick();
            }
        });


    }

    private void signupClick() {
        String userpassword = password.getText().toString();
        String useremail = email.getText().toString();

        if(userpassword.isEmpty()){
            Toast.makeText(this,"Şifre boş olamaz", Toast.LENGTH_SHORT).show();
        }

        if(userpassword.length()<6){
            Toast.makeText(this,"Şifre 6 karakterden az olamaz", Toast.LENGTH_SHORT).show();
        }

        if(useremail.isEmpty()){
            Toast.makeText(this,"e mail boş olamaz", Toast.LENGTH_SHORT).show();
        }

        firebaseAuth.createUserWithEmailAndPassword(useremail, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, SplashScreen.class));
                }else{
                    Toast.makeText(RegisterActivity.this, "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}