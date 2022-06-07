package com.example.controledeprodutos.autentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.controledeprodutos.R;
import com.example.controledeprodutos.activity.MainActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import java.util.Objects;

public class CriarContaActivity extends AppCompatActivity {

    private EditText edit_nome;
    private EditText edit_email;
    private EditText edit_senha;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        iniciaComponentes();

        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.ibVoltar).setOnClickListener(view -> finish());
    }

    public void validaDados(View view){
        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString().trim();
        String senha = edit_senha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){

                    progressBar.setVisibility(View.VISIBLE);
                    Usuario usuario = new Usuario();
                    usuario.setNome(nome);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);

                    salvarCadastro(usuario);

                }else{
                    edit_senha.requestFocus();
                    edit_senha.setError("Informe sua senha");
                }
            }else{
                edit_email.requestFocus();
                edit_email.setError("Informe seu email");
            }
        }else{
            edit_nome.requestFocus();
            edit_nome.setError("Informe seu nome");
        }
    }

    private void salvarCadastro(Usuario usuario){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                        usuario.setId(id);

                        finish();
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                });
    }

    private void iniciaComponentes() {
        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);
    }


}