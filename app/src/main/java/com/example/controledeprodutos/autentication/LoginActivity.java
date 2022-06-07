package com.example.controledeprodutos.autentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.activity.MainActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

  private EditText login_email;
  private EditText login_senha;
  private TextView text_criar_conta;
  private TextView text_recuperar_senha;
  private ProgressBar progressBar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    iniciaComponentes();
    configCliques();

  }

  public void logar(View view) {
    String email = login_email.getText().toString().trim();
    String senha = login_senha.getText().toString();

    if (!email.isEmpty()) {
      if (!senha.isEmpty()) {
        progressBar.setVisibility(View.VISIBLE);
        validaLogin(email, senha);

      } else {
        login_senha.requestFocus();
        login_senha.setError("Informe sua senha");
      }

    } else {
      login_email.requestFocus();
      login_email.setError("Informe seu email");
    }
  }

  private void validaLogin(String email, String senha) {
    FirebaseHelper.getAuth().signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener(task -> {

              if (task.isSuccessful()) {
                FirebaseHelper.getAuth().getCurrentUser();
                finish();
                startActivity(new Intent(this, MainActivity.class));

              } else {
                String error = Objects.requireNonNull(task.getException()).getMessage();
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
              }
            });
  }

  private void configCliques() {
    text_criar_conta.setOnClickListener(view -> startActivity(new Intent(this, CriarContaActivity.class)));
    text_recuperar_senha.setOnClickListener(view -> startActivity(new Intent(this, RecuperarContaActivity.class)));
  }

  private void iniciaComponentes() {
    login_email = findViewById(R.id.login_email);
    login_senha = findViewById(R.id.login_senha);
    text_criar_conta = findViewById(R.id.text_criar_conta);
    text_recuperar_senha = findViewById(R.id.text_recuperar_senha);
    progressBar = findViewById(R.id.progressBar);
  }
}