package com.example.controledeprodutos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class FormProdutoActivity extends AppCompatActivity {

    private ImageButton ibVoltar;

    private EditText edit_produto;
    private EditText edit_quantidade;
    private EditText edit_valor;

    private ProdutoDAO produtoDAO;

    private Produto produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_produto);

        produtoDAO = new ProdutoDAO(this);

        ibVoltar = findViewById(R.id.ibVoltar);
        edit_produto = findViewById(R.id.edit_produto);
        edit_quantidade = findViewById(R.id.edit_quantidade);
        edit_valor = findViewById(R.id.edit_valor);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            produto = (Produto) bundle.getSerializable("produto");
            editProduto();
        }

        ouvirCliques();
    }

    public void editProduto(){
        edit_produto.setText(produto.getNome());
        edit_quantidade.setText(String.valueOf(produto.getEstoque()));
        edit_valor.setText(String.valueOf(produto.getValor()));

    }

    public void salvarProduto(View view){
        String nome = edit_produto.getText().toString();
        String quantidade = edit_quantidade.getText().toString();
        String valor = edit_valor.getText().toString();


        if(!nome.isEmpty()){
            if(!quantidade.isEmpty()){
                int qtd = Integer.parseInt(quantidade);

                if(qtd >= 1){
                    if(!valor.isEmpty()){
                        double valorProduto = Double.parseDouble(valor);


                        if(valorProduto > 0){
                            if(produto == null) produto = new Produto();
                            produto.setNome(nome);
                            produto.setEstoque(qtd);
                            produto.setValor(valorProduto);

                            if(produto.getId() != 0){
                                produtoDAO.atualizaProduto(produto);
                            }else{
                                produtoDAO.salvarProduto(produto);
                            }

                            finish();

                        }else{
                            edit_valor.requestFocus();
                            edit_valor.setError("Informe um valor maior que 0.");
                        }

                    }else{
                        edit_valor.requestFocus();
                        edit_valor.setError("Informe o valor do produto");
                    }
                }else{
                    edit_quantidade.requestFocus();
                    edit_quantidade.setError("Informe um valor maior que 0.");
                }

            }else{
                edit_quantidade.requestFocus();
                edit_quantidade.setError("Informe uma quantidade válida");
            }
        }else{
            edit_produto.requestFocus();
            edit_produto.setError("Informe o nome do produto.");
        }
    }

    private void ouvirCliques(){

        ibVoltar.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}