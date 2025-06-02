package com.aulaquinta.aplicativoextensionista.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx. appcompat. widget. Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.databinding.ActivityHomeDoisBinding;
import com.aulaquinta.aplicativoextensionista.fragment.FeedFragment;
import com.aulaquinta.aplicativoextensionista.fragment.NewPostFragment;
import com.aulaquinta.aplicativoextensionista.fragment.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeDoisActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActivityHomeDoisBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeDoisBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        //setContentView(binding.getRoot());

        // - - - - - - - - - - - - - - - - - - - -  [ NÃO REMOVER - Ajusta paddings em relação aos items do sistema Android (statusBar, etc) ]
        setContentView(R.layout.activity_home_dois);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ConstraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        auth = ConfiguracaoFirebase.getFirebaseAuth();
        replaceFragment(new FeedFragment());
        configureBottomNavigation();
    }

    private void configureBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        enableNavigation(bottomNavigationView);
    }

    private void enableNavigation(BottomNavigationView view){
        view.setOnItemSelectedListener(item -> {
            var itemId = item.getItemId();
            if(itemId == R.id.navbar_home){
                replaceFragment(new FeedFragment());
                toolbar.setTitle("Home");
            }
            if(itemId == R.id.navbar_postagem){
                replaceFragment(new NewPostFragment());
                toolbar.setTitle("Nova postagem");
            }
            if(itemId == R.id.navbar_perfil){
                replaceFragment(new PerfilFragment());
                toolbar.setTitle("Perfil");
            }
            return true;
        });
    }

//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_toolbar, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    //@Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        var itemId = item.getItemId();
//        if(itemId == R.id.icone_toolbar_sair){
//            userLogoff();
//        }
//        if(itemId == R.id.icone_toolbar_editar){
//            startActivity(new Intent(getApplicationContext(), EditarPerfilActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}