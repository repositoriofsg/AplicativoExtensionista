package com.aulaquinta.aplicativoextensionista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    Button buttonSettingsSair;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayoutSettings), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        inicializarComponentesSettings();

        buttonSettingsSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogoff();
            }
        });
    }

    private void userLogoff(){
        try{
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } catch (Exception e) {
            Log.v("ex", (String) Objects.requireNonNull(e.getMessage()));
        }
    }

    public void inicializarComponentesSettings(){
        buttonSettingsSair = findViewById(R.id.buttonSettingsSair);
    }
}