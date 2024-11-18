package com.example.olx_clone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.olx_clone.R;
import com.example.olx_clone.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class AnunciosActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anuncios);

        // Configurações iniciais
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (autenticacao.getCurrentUser() == null) { // Usuário deslogado
            menu.setGroupVisible(R.id.group_deslogado, true);
        } else { // Usuário logado
            menu.setGroupVisible(R.id.group_logado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_cadastrar) {
            startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            return true;
        } else if (id == R.id.menu_sair) {
            autenticacao.signOut();
            invalidateOptionsMenu();
            return true;
        } else if (id == R.id.menu_anuncios) {
            startActivity(new Intent(getApplicationContext(), AnunciosActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
