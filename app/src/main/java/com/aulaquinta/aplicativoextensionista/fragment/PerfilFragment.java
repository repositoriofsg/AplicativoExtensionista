package com.aulaquinta.aplicativoextensionista.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.activity.EditarPerfilActivity;
import com.aulaquinta.aplicativoextensionista.activity.HomeDoisActivity;
import com.aulaquinta.aplicativoextensionista.activity.LoginActivity;
import com.aulaquinta.aplicativoextensionista.activity.SettingsActivity;
import com.aulaquinta.aplicativoextensionista.adapter.PostagemPerfilAdapter;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.model.PostagemFeed;
import com.aulaquinta.aplicativoextensionista.model.PostagemPerfil;
import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment implements MenuProvider {

    private CircleImageView circleImageViewPerfil;
    //private TextInputEditText textInputTitulo, textInputDescricao;
    private TextView textViewPerfilNome;
    private TextView textViewPerfilEmail;
    private TextView textViewPerfilProfissaoValor;
    private TextView textViewPerfilDisponibilidadeValor;
    private String idUsuarioLogado;

    RecyclerView recyclerView;
    ArrayList<PostagemPerfil> listaPostagemPerfil;
    DatabaseReference databasePostagemPerfil;
    DatabaseReference databaseUsuarioReference;
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
        inicializarComponentesPerfil(rootView);

        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        //databaseUsuarioReference = ConfiguracaoFirebase.getFirebaseDatabase();

        // Consultar  dados do usuário (Authentication)
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        textViewPerfilNome.setText(usuarioPerfil.getDisplayName());
        textViewPerfilEmail.setText(usuarioPerfil.getEmail());

        // Consulta dados do usuário
        databaseUsuarioReference = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios").child(idUsuarioLogado);
        databaseUsuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);

                if(usuario != null){
                    if(usuario.getProfissao() != null){
                        textViewPerfilProfissaoValor.setText(usuario.getProfissao());
                    }
                    if(usuario.getDisponibilidade() != null){
                        textViewPerfilDisponibilidadeValor.setText(usuario.getDisponibilidade());
                    }
                }

//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    Usuario usuario = ds.getValue(Usuario.class);
//
//                    if(usuario != null){
//                        if(usuario.getProfissao() != null){
//                            textViewPerfilProfissaoValor.setText(usuario.getProfissao());
//                        }
//                        if(usuario.getDisponibilidade() != null){
//                            textViewPerfilDisponibilidadeValor.setText(usuario.getDisponibilidade());
//                        }
//                    }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Uri url = usuarioPerfil.getPhotoUrl();
        if(url != null){
            Glide.with(requireContext())
                    .load(url)
                    .into(circleImageViewPerfil);
        }else{
            circleImageViewPerfil.setImageResource(R.drawable.avatar);
        }

        // Consulta postagens
        recyclerView = rootView.findViewById(R.id.recyclerViewPerfil);
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

    public void inicializarComponentesPerfil(View view){
        circleImageViewPerfil = view.findViewById(R.id.circleImageViewPerfil);
        textViewPerfilNome = view.findViewById(R.id.textViewPerfilNome);
        textViewPerfilEmail = view.findViewById(R.id.textViewPerfilEmail);
        textViewPerfilProfissaoValor = view.findViewById(R.id.textViewPerfilProfissaoValor);
        textViewPerfilDisponibilidadeValor = view.findViewById(R.id.textViewPerfilDisponibilidadeValor);
    }
}