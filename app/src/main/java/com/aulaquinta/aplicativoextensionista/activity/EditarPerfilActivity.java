package com.aulaquinta.aplicativoextensionista.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {

    private CircleImageView circleImageViewEditarPerfil;
    private TextView textViewAlterarFoto;
    private TextInputEditText textInputEditarPerfilNome, textInputEditarPerfilEmail;
    private Button buttonEditarPerfilSalvar;
    private Usuario usuarioLogado;
    private String idUsuario;
    private static final int SELECAO_GALERIA = 200;

    private ActivityResultLauncher<String> pickImageLauncher;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuções iniciais
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageReference = ConfiguracaoFirebase.getFirebaseStorageReference();
        idUsuario = UsuarioFirebase.getIdentificadorUsuario();

        // Configura toobar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        inicializarComponentesEditarPerfil();

        // Recuperar dados do usuário
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        textInputEditarPerfilNome.setText(usuarioPerfil.getDisplayName());
        textInputEditarPerfilEmail.setText(usuarioPerfil.getEmail());
        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){
            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(circleImageViewEditarPerfil);
        }else{
            circleImageViewEditarPerfil.setImageResource(R.drawable.avatar);
        }

        // Salvar alteração do nome
        buttonEditarPerfilSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualizado = Objects.requireNonNull(textInputEditarPerfilNome.getText()).toString();

                // Atualizar nome no perfil (Firebase)
                UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

                // Atualizar nome no RealTime Database (Firebase)
                usuarioLogado.setNome(nomeAtualizado);
                usuarioLogado.atualizar();
                Toast.makeText( EditarPerfilActivity.this, "Dados alterados com sucess!.", Toast.LENGTH_SHORT).show();
            }
        });

//        pickImageLauncher = registerForActivityResult(
//                new ActivityResultContracts.GetContent(),
//                uri -> {
//                    if (uri != null) {
//                       circleImageViewEditarPerfil.setImageURI(uri);
//                    }
//                });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {});

        textViewAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText( EditarPerfilActivity.this, "CLICADO", Toast.LENGTH_SHORT).show();
                pickImageLauncher.launch("image/*");

//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                if(i.resolveActivity(getPackageManager()) != null){
//                    // Para funcionar o resolveActivity precisa ter o <queries> no manifesto
//
//                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;

            try
            {
//                switch (requestCode){
//                    case SELECAO_GALERIA:
//                        Uri localImagemSelecionada = data.getData();
//                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
//                        break;
//                }

                Uri localImagemSelecionada = Objects.requireNonNull(data).getData();
                imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);

                if(imagem != null){
                    circleImageViewEditarPerfil.setImageBitmap(imagem);

                    // Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    // Salvar imagem no firebase
                    StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(idUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( EditarPerfilActivity.this, "Erro no upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizarFotoUsuario(url);
                                }
                            });
                        }
                    });
                }
            } catch (Exception e) {
                Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
            }
        }
    }

    private void atualizarFotoUsuario(Uri url){
        UsuarioFirebase.atualizarFotoUsuario(url);
        usuarioLogado.setCaminhoFoto(url.toString());
        usuarioLogado.atualizar();
        Toast.makeText( EditarPerfilActivity.this, "Imagem atualizada com sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentesEditarPerfil(){
        circleImageViewEditarPerfil = findViewById(R.id.circleImageViewEditarPerfil);
        textViewAlterarFoto = findViewById(R.id.textViewAlterarFoto);
        textInputEditarPerfilNome = findViewById(R.id.textInputEditarPerfilNome);
        textInputEditarPerfilEmail = findViewById(R.id.textInputEditarPerfilEmail);
        buttonEditarPerfilSalvar = findViewById(R.id.buttonEditarPerfilSalvar);
        textInputEditarPerfilEmail.setFocusable(false);
    }
}