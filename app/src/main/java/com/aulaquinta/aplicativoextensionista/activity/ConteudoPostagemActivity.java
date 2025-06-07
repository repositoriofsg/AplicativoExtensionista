package com.aulaquinta.aplicativoextensionista.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aulaquinta.aplicativoextensionista.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConteudoPostagemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conteudo_postagem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayoutConteudo), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Conteúdo postagem");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icone_toolbar_fechar);

        // Recuperar dados intent
        String foto = getIntent().getStringExtra("FOTO");
        String nome = getIntent().getStringExtra("NOME");
        String data = getIntent().getStringExtra("DATA");
        String titulo = getIntent().getStringExtra("TITULO");
        String descricao = getIntent().getStringExtra("DESCRICAO");
        String email = getIntent().getStringExtra("EMAIL");
        String telefone = getIntent().getStringExtra("TELEFONE");

        CircleImageView circleImageViewConteudo = findViewById(R.id.circleImageViewConteudo);
        TextView textViewConteudoNome = findViewById(R.id.textViewConteudoNome);
        TextView textViewConteudoData = findViewById(R.id.textViewConteudoData);
        TextView textViewConteudoTitulo = findViewById(R.id.textViewConteudoTitulo);
        TextInputEditText textInputEditTextConteudoTituloValor = findViewById(R.id.textInputEditTextConteudoTituloValor);
        TextView textViewConteudoDescricao = findViewById(R.id.textViewConteudoDescricao);
        TextInputEditText textInputEditTextConteudoDescricaoValor = findViewById(R.id.textInputEditTextConteudoDescricaoValor);
        TextView textViewConteudoEmail = findViewById(R.id.textViewConteudoEmail);
        TextView textViewConteudoTelefone = findViewById(R.id.textViewConteudoTelefone);


        if(foto != null){
            Glide.with(this)
                    .load(foto)
                    .into(circleImageViewConteudo);
        }else{
            circleImageViewConteudo.setImageResource(R.drawable.avatar);
        }
        textViewConteudoNome.setText(nome);
        textViewConteudoData.setText(data);
        textInputEditTextConteudoTituloValor.setText(titulo);
        textInputEditTextConteudoDescricaoValor.setText(descricao);
        textViewConteudoEmail.setText(email);
        textViewConteudoTelefone.setText(telefone);

        textViewConteudoEmail.setOnClickListener(v -> {
            String text = textViewConteudoEmail.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("E-mail", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ConteudoPostagemActivity.this, "E-mail copiado!", Toast.LENGTH_SHORT).show();
        });

        textViewConteudoTelefone.setOnClickListener(v -> {
            String text = textViewConteudoTelefone.getText().toString();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Telefone", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(ConteudoPostagemActivity.this, "Telefone copiado!", Toast.LENGTH_SHORT).show();
        });
    }
}