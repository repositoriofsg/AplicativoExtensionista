package com.aulaquinta.aplicativoextensionista.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.activity.EditarPerfilActivity;
import com.aulaquinta.aplicativoextensionista.activity.HomeDoisActivity;
import com.aulaquinta.aplicativoextensionista.activity.LoginActivity;
import com.aulaquinta.aplicativoextensionista.activity.SettingsActivity;
import com.aulaquinta.aplicativoextensionista.adapter.PostagemPerfilAdapter;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.model.PostagemPerfil;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PerfilFragment extends Fragment implements MenuProvider {

    //Toolbar toolbarPerfil;
    private TextInputEditText textInputTitulo, textInputDescricao;
    private String idUsuarioLogado;

    RecyclerView recyclerView;
    ArrayList<PostagemPerfil> listaPostagemPerfil;
    DatabaseReference databasePostagemPerfil;
    PostagemPerfilAdapter adapterPostagemPerfil;

    FirebaseAuth auth;


    public PerfilFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewPerfil);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        databasePostagemPerfil = ConfiguracaoFirebase.getFirebaseDatabase().child("postagem").child(idUsuarioLogado);
        listaPostagemPerfil = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPostagemPerfil = new PostagemPerfilAdapter(listaPostagemPerfil);
        recyclerView.setAdapter(adapterPostagemPerfil);

        databasePostagemPerfil.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    PostagemPerfil postagemPerfil = ds.getValue(PostagemPerfil.class);
                    listaPostagemPerfil.add(postagemPerfil);
                }
                adapterPostagemPerfil.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_perfil, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.icone_toolbar_settings) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
            return true;
        } else if (menuItem.getItemId() == R.id.icone_toolbar_editar) {
            startActivity(new Intent(getContext(), EditarPerfilActivity.class));
            return true;
        }
        return false;
    }
}