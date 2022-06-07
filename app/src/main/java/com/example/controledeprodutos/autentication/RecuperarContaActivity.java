package com.example.controledeprodutos.autentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.helper.FirebaseHelper;

import java.util.Objects;

public class RecuperarContaActivity extends AppCompatActivity {

  private EditText edit_email;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recuperar_conta);
    iniciaComponentes();
    configCliques();

  }


  private void configCliques(){
    findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());
  }


  public void recuperarSenha(View view){
    String email = edit_email.getText().toString().trim();

    if(!email.isEmpty()){

      progressBar.setVisibility(View.VISIBLE);
      enviaEmail(email);



    }else{
      edit_email.requestFocus();
      edit_email.setError("Informe seu email");
    }

  }

  private void enviaEmail(String email){
    FirebaseHelper.getAuth().sendPasswordResetEmail(email)
            .addOnCompleteListener(task ->{
        if(task.isSuccessful()){
          Toast.makeText(this, "Email enviado com sucesso!", Toast.LENGTH_SHORT).show();
        }else{
          String error = Objects.requireNonNull(task.getException()).getMessage();
          Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);

    });
  }

  private void iniciaComponentes(){
    edit_email = findViewById(R.id.recuperar_email);
    progressBar = findViewById(R.id.progressBar);
  }
}