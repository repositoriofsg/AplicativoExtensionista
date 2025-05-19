package com.aulaquinta.aplicativoextensionista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton, registerButton;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

//        loginButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString();
//            String senha = passwordEditText.getText().toString();
//            Toast.makeText(this, "Login: " + email, Toast.LENGTH_SHORT).show();
//        });
//
//        registerButton.setOnClickListener(v -> {
//            Toast.makeText(this, "Ir para cadastro", Toast.LENGTH_SHORT).show();
//        });
    }

    public void validateUserSignIn(View view){

        String email = emailEditText.getText().toString();
        String senha = passwordEditText.getText().toString();
        //String telefone = textInput_telefone.getText().toString();

        boolean emptyEmail = false;
        boolean emptySenha = false;
        //boolean emptyTelefone = false;

        if(email.isEmpty()) emptyEmail = true;
        if(senha.isEmpty()) emptySenha = true;
        //if(!telefone.isEmpty()) emptyTelefone = true;

        if(emptyEmail || emptySenha){
            Toast.makeText( LoginActivity.this, "Preencha os dados", Toast.LENGTH_SHORT).show();
        }else{
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setSenha(senha);
            userSignIn(usuario);
        }
    }

    public void userSignIn(Usuario usuario){

        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                openHomeScreen();
                //Toast.makeText( LoginActivity.this, "sucesso.", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText( LoginActivity.this, "Erro ao autenticar usuário.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openSignupScreen(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    public void openHomeScreen(){ // Removido o View view porque não está sendo chamado a partir da interface
        //Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class); // Temporario
        startActivity(intent);
    }
}
