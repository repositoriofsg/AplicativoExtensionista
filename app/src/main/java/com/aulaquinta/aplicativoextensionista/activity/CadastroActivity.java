package com.aulaquinta.aplicativoextensionista.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.model.Usuario;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;


public class CadastroActivity extends AppCompatActivity {

    private EditText textInput_nome, textInput_email, textInput_senha, textInput_telefone;

    Spinner dropdownCadastro;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textInput_nome = findViewById(R.id.textInput_nome);
        textInput_email = findViewById(R.id.textInput_email);
        textInput_senha = findViewById(R.id.textInput_senha);
        textInput_telefone = findViewById(R.id.textInput_telefone);
        //aplicarMascaraTelefone(textInput_telefone);

        dropdownCadastro = this.findViewById(R.id.spinner_disciplinas_interesse);
        initSpinnerCadastro();
    }

    private void initSpinnerCadastro() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.disciplinas_array,
                android.R.layout.simple_spinner_item
        );

        dropdownCadastro.setAdapter(adapter);
        dropdownCadastro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void validarCamposUsuario(View view){

        String nome = textInput_nome.getText().toString();
        String email = textInput_email.getText().toString();
        String senha = textInput_senha.getText().toString();
        String telefone = textInput_telefone.getText().toString();

        boolean emptyNome = false;
        boolean emptyEmail = false;
        boolean emptySenha = false;
        //boolean emptyTelefone = false;

        if(nome.isEmpty()) emptyNome = true;
        if(email.isEmpty()) emptyEmail = true;
        if(senha.isEmpty()) emptySenha = true;
        //if(!telefone.isEmpty()) emptyTelefone = true;

        if(emptyNome || emptyEmail || emptySenha){
            Toast.makeText( CadastroActivity.this, "Preencha os dados", Toast.LENGTH_SHORT).show();
        }else{
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setTelefone(telefone);
            cadastrarUsuario(usuario);
        }
    }

    public void cadastrarUsuario(Usuario usuario){

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()

        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                try{
                    //progressBar.setVisibility(View.INVISIBLE);

                    // Salvar dados no Realtime Database (Firebase)
                    String idUsuario = Objects.requireNonNull(task.getResult().getUser()).getUid();
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    // Salvar dados no profile (Firebase)
                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                    Toast.makeText( CadastroActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception e) {
                    Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
                }
            }else{
                String excecao;

                try{
                    throw task.getException();

                }catch( FirebaseAuthWeakPasswordException e){
                    excecao = "A senha precisa ser mais forte.";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    excecao = "E-mail inválido.";
                } catch (FirebaseAuthUserCollisionException e) {
                    excecao = "Conta já cadastrada.";
                } catch (Exception e) {
                    excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                    Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
                }

                Toast.makeText( CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void aplicarMascaraTelefone(EditText editText) {
//        textInput_telefone.addTextChangedListener(new TextWatcher() {
//            boolean isUpdating;
//            String old = "";
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String str = s.toString().replaceAll("[^\\d]", "");
//
//                String mascara = "";
//                if (isUpdating) {
//                    old = str;
//                    isUpdating = false;
//                    return;
//                }
//
//                int length = str.length();
//                int i = 0;
//
//                if (length > 0) mascara += "(";
//                for (; i < length && i < 2; i++) mascara += str.charAt(i);
//                if (length > 2) mascara += ") ";
//                for (; i < length && i < 7; i++) mascara += str.charAt(i);
//                if (length > 7) mascara += "-";
//                for (; i < length && i < 11; i++) mascara += str.charAt(i);
//
//                isUpdating = true;
//                textInput_telefone.setText(mascara);
//                textInput_telefone.setSelection(mascara.length());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
}