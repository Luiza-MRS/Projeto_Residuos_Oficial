package com.example.olx_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.olx_clone.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MeusAnunciosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meus_anuncios);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Adicionar a seta de voltar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurar o Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            // Ação ao clicar no FAB
            Toast.makeText(MeusAnunciosActivity.this, "FAB Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),CadastrarAnuncioActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Ação para voltar para a `AnunciosActivity`
            onBackPressed(); // Ou use startActivity(new Intent(this, AnunciosActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
