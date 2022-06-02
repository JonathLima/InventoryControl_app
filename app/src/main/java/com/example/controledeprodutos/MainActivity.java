package com.example.controledeprodutos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterProduto.OnClick {
    private AdapterProduto adapterProduto;
    private ProdutoDAO produtoDAO;
    private List<Produto> produtoList = new ArrayList<>();
    private SwipeableRecyclerView rvProdutos;

    private TextView textInfo;
    private ImageButton ibAdd;
    private ImageButton ibMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        produtoDAO = new ProdutoDAO(this);
        produtoList = produtoDAO.getListProdutos();

        textInfo = findViewById(R.id.text_info);
        ibAdd = findViewById(R.id.ib_add);
        ibMenu = findViewById(R.id.ib_menu);
        rvProdutos = findViewById(R.id.rvProdutos);

        configRecyclerView();
        ouvinteCliques();
    }

    @Override
    protected void onStart() {
        super.onStart();
        configRecyclerView();
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
                }
                return true;
            });

            popupMenu.show();

        });

    }

    private void configRecyclerView(){

        produtoList.clear();
        produtoList = produtoDAO.getListProdutos();

        verifyQtdList();

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
                produtoDAO.deletaProduto(produto);
                produtoList.remove(produto);
                adapterProduto.notifyItemRemoved(position);

                verifyQtdList();

            }
        });

    }

    private void verifyQtdList(){
        if(produtoList.size() == 0){
            textInfo.setVisibility(View.VISIBLE);
        }else{
            textInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickListener(Produto produto) {
        Intent intent = new Intent(this, FormProdutoActivity.class);
        intent.putExtra("produto", produto);
        startActivity(intent);
    }


}