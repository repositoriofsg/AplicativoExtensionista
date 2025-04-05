package com.aulaquinta.aplicativoextensionista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aulaquinta.aplicativoextensionista.R;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String senha = passwordEditText.getText().toString();
            Toast.makeText(this, "Login: " + email, Toast.LENGTH_SHORT).show();
        });

        registerButton.setOnClickListener(v -> {
            Toast.makeText(this, "Ir para cadastro", Toast.LENGTH_SHORT).show();
        });
    }
}
