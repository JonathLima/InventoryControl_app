package com.example.controledeprodutos.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.autentication.LoginActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash2);

    new Handler(Looper.getMainLooper()).postDelayed(this::verificaLogin, 3000);

  }

  private void verificaLogin(){
    if(FirebaseHelper.isAuthenticated()){
      startActivity(new Intent(this, MainActivity.class));
    }else{
      startActivity(new Intent(this, LoginActivity.class));
    }
  }

}