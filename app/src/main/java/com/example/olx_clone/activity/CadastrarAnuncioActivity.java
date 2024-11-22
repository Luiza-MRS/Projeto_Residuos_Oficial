package com.example.olx_clone.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.olx_clone.R;
import com.example.olx_clone.helper.Permissoes;

import java.util.ArrayList;
import java.util.List;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText campoTitulo, campoDescricao, campoValor, campoTelefone;
    private ImageView imagem1, imagem2, imagem3;
    private final String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private final List<String> listaFotosRecuperadas = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);
        inicializarComponentes();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        // Adicionar TextWatcher para formatar o telefone
        adicionarTextWatcherTelefone();

        // Inicializar ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri imagemSelecionada = data.getData();
                            String caminhoImagem = imagemSelecionada.toString();

                            // Configura imagem no ImageView
                            if (requestCode == 1) {
                                imagem1.setImageURI(imagemSelecionada);
                            } else if (requestCode == 2) {
                                imagem2.setImageURI(imagemSelecionada);
                            } else if (requestCode == 3) {
                                imagem3.setImageURI(imagemSelecionada);
                            }

                            listaFotosRecuperadas.add(caminhoImagem);
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imageCadastro1) {
            escolherImagem(1);
        } else if (id == R.id.imageCadastro2) {
            escolherImagem(2);
        } else if (id == R.id.imageCadastro3) {
            escolherImagem(3);
        } else {
            throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    public void escolherImagem(int requestCode) {
        this.requestCode = requestCode;
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(i);
    }

    private void inicializarComponentes() {
        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        campoTelefone = findViewById(R.id.editTelefone);
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void adicionarTextWatcherTelefone() {
        campoTelefone.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private boolean isDeleting = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isDeleting = count > 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    campoTelefone.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("\\D", "");
                    StringBuilder formatted = new StringBuilder();

                    if (!cleanString.isEmpty()) {
                        formatted.append("(").append(cleanString.substring(0, Math.min(cleanString.length(), 2)));
                    }

                    if (cleanString.length() > 2) {
                        formatted.append(") ").append(cleanString.substring(2, Math.min(cleanString.length(), 3)));
                    }

                    if (cleanString.length() > 3) {
                        formatted.append(cleanString.substring(3, Math.min(cleanString.length(), 7)));
                    }

                    if (cleanString.length() > 7) {
                        formatted.append("-").append(cleanString.substring(7, Math.min(cleanString.length(), 11)));
                    }

                    current = formatted.toString();
                    campoTelefone.setText(current);
                    campoTelefone.setSelection(current.length());

                    campoTelefone.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void salvarAnuncio(View view) {
        String valor = campoValor.getText().toString();
        Log.d("salvar", "salvarAnuncio: " + valor);

        // Lógica para salvar o anúncio
        String titulo = campoTitulo.getText().toString();
        String descricao = campoDescricao.getText().toString();
        String telefone = campoTelefone.getText().toString();

        // Para propósitos de depuração
        Log.d("salvar", "Titulo: " + titulo);
        Log.d("salvar", "Descricao: " + descricao);
        Log.d("salvar", "Telefone: " + telefone);

        // Exemplo de lógica adicional para salvar o anúncio
        // anuncio.setTitulo(titulo);
        // anuncio.setDescricao(descricao);
        // anuncio.setValor(valor);
        // anuncio.setTelefone(telefone);

        Toast.makeText(this, "Anúncio salvo!", Toast.LENGTH_SHORT).show();

        // Redirecionar para "Meus Anúncios"
        Intent intent = new Intent(CadastrarAnuncioActivity.this, MeusAnunciosActivity.class);
        startActivity(intent);
        finish();
    }
}
