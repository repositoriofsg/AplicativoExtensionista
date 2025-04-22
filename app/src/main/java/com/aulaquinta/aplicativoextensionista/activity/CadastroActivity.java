package com.aulaquinta.aplicativoextensionista.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.model.Usuario;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class CadastroActivity extends AppCompatActivity {

    private EditText textInput_nome, textInput_email, textInput_senha;
    //private EditText textInput_nome, textInput_email, textInput_senha, textInput_telefone;

    //Button btnCadastro;
    //FirebaseDatabase database;
    //DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        textInput_nome = findViewById(R.id.textInput_nome);
        textInput_email = findViewById(R.id.textInput_email);
        textInput_senha = findViewById(R.id.textInput_senha);
        //textInput_telefone = findViewById(R.id.textInput_telefone);
    }

    public void validarCamposUsuario(View view){

        String nome = textInput_nome.getText().toString();
        String email = textInput_email.getText().toString();
        String senha = textInput_senha.getText().toString();
        //String telefone = textInput_telefone.getText().toString();

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
            cadastrarUsuario(usuario);
        }
    }

    public void cadastrarUsuario(Usuario usuario){

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()

        ).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                Toast.makeText( CadastroActivity.this, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
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
                    e.printStackTrace();
                }

                Toast.makeText( CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
            }
        });
    }
}