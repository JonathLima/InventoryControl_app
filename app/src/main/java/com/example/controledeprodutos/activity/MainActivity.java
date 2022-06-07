package com.example.controledeprodutos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledeprodutos.adapter.AdapterProduto;
import com.example.controledeprodutos.autentication.LoginActivity;
import com.example.controledeprodutos.helper.FirebaseHelper;
import com.example.controledeprodutos.model.Produto;
import com.example.controledeprodutos.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {
    private AdapterProduto adapterProduto;

    private List<Produto> produtoList = new ArrayList<>();
    private SwipeableRecyclerView rvProdutos;

    private TextView textInfo;
    private ImageButton ibAdd;
    private ImageButton ibMenu;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();
        ouvinteCliques();
        configRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        recuperaProdutos();
    }

    private void ouvinteCliques(){
        ibAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, FormProdutoActivity.class));
        });

        ibMenu.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(this, ibMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_toolbar, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.menu_sobre) {

                    Toast.makeText(this, "Controle de Estoque", Toast.LENGTH_SHORT).show();

                }else if(menuItem.getItemId() == R.id.menu_sair){
                    FirebaseHelper.getAuth().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                }
                return true;
            });


            popupMenu.show();

        });

    }

    private void configRecyclerView(){

        rvProdutos.setLayoutManager(new LinearLayoutManager(this));
        rvProdutos.setHasFixedSize(true);

        adapterProduto = new AdapterProduto(produtoList, this);
        rvProdutos.setAdapter(adapterProduto);

        rvProdutos.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                Produto produto = produtoList.get(position);
                onClickListener(produto);
            }

            @Override
            public void onSwipedRight(int position) {
                Produto produto = produtoList.get(position);
                produtoList.remove(produto);
                produto.deletarProduto();
                adapterProduto.notifyItemRemoved(position);

                verifyQtdList();
            }
        });

    }

    private void recuperaProdutos(){
        DatabaseReference produtosRef = FirebaseHelper.getDatabaseReference()
                .child("produtos")
                .child(FirebaseHelper.getIdFirebase());
        produtosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                produtoList.clear();

                for(DataSnapshot snap : snapshot.getChildren()){
                    Produto produto = snap.getValue(Produto.class);


                    produtoList.add(produto);
                }
                verifyQtdList();
                Collections.reverse(produtoList);
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyQtdList(){
        if(produtoList.size() == 0){
            textInfo.setText("Nenhum produto cadastrado.");
            textInfo.setVisibility(View.VISIBLE);
        }else{
            textInfo.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }

    private void iniciaComponentes(){
        textInfo = findViewById(R.id.text_info);
        ibAdd = findViewById(R.id.ib_add);
        ibMenu = findViewById(R.id.ib_menu);
        rvProdutos = findViewById(R.id.rvProdutos);
        progressBar = findViewById(R.id.progressBar);
    }

}