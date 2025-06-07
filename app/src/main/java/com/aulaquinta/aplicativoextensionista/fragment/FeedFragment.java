package com.aulaquinta.aplicativoextensionista.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.activity.ConteudoPostagemActivity;
import com.aulaquinta.aplicativoextensionista.adapter.PostagemFeedAdapter;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.helper.RecyclerViewInterface;
import com.aulaquinta.aplicativoextensionista.model.Postagem;
import com.aulaquinta.aplicativoextensionista.model.PostagemFeed;
import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class FeedFragment extends Fragment implements MenuProvider{

    private CircleImageView CircleImageViewFeed;
    private TextView textViewPostagemFeedNomeUsuario;
    private TextView textViewPostagemFeedData;
    private TextView textViewPostagemFeedTitulo;
    private TextView textViewPostagemFeedDescricao;
    private TextView textViewPostagemFeedDisciplina;
    //private Button buttonPostagemFeed;
    private ProgressBar progressBarFeed;
    private String idUsuarioLogado;

    RecyclerView recyclerView;
    //RecyclerViewInterface recyclerViewInterface;
    ArrayList<PostagemFeed> listaPostagemFeed;
    DatabaseReference databasePostagemFeed;
    //DatabaseReference databaseUsuarioFeed;
    PostagemFeedAdapter adapterPostagemFeed;

    Usuario usuario;

    public FeedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerViewFeed);
        progressBarFeed = rootView.findViewById(R.id.progressBarFeed);

        showProgressBar();

        listaPostagemFeed = new ArrayList<>();

//        if(!listaPostagemFeed.isEmpty()){
//            hideProgressBar();
//        }
//        listaPostagemFeed.clear();

        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        databasePostagemFeed = ConfiguracaoFirebase.getFirebaseDatabase().child("postagem");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //adapterPostagemFeed = new PostagemFeedAdapter(listaPostagemFeed);
        //Toast.makeText(getContext(), "Clicou: " + position, Toast.LENGTH_SHORT).show();
        adapterPostagemFeed = new PostagemFeedAdapter(listaPostagemFeed, position -> {
            PostagemFeed postagemFeed = listaPostagemFeed.get(position);
            //String data = postagemFeed.getData();

            Intent intent = new Intent(requireActivity(), ConteudoPostagemActivity.class);

            intent.putExtra("FOTO", postagemFeed.getCaminhoFoto());
            intent.putExtra("NOME", postagemFeed.getNome());
            intent.putExtra("DATA", postagemFeed.getData());
            intent.putExtra("TITULO", postagemFeed.getTitulo());
            intent.putExtra("DESCRICAO", postagemFeed.getDescricao());
            intent.putExtra("EMAIL", postagemFeed.getEmail());
            if(postagemFeed.getTelefone() != null){
                intent.putExtra("TELEFONE", postagemFeed.getTelefone());
            }else{
                intent.putExtra("TELEFONE", "Telefone não informado");
            }
            startActivity(intent);

            //Toast.makeText(getContext(), "Data: " + data, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapterPostagemFeed);

        databasePostagemFeed.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postagemSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot ds: postagemSnapshot.getChildren()) {
                        PostagemFeed postagemFeed = ds.getValue(PostagemFeed.class);
                        listaPostagemFeed.add(postagemFeed);
                    }
                }
                listaPostagemFeed.sort(Comparator.comparingLong(PostagemFeed::getDataOrdenarTimeStamp).reversed());
                if(!listaPostagemFeed.isEmpty()){
                    hideProgressBar();
                }
                adapterPostagemFeed.notifyDataSetChanged();
                //adapterPostagemFeed.notifyItemInserted(listaPostagemFeed.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;

        //backup - consultando com id do usuario
//        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
//        databasePostagemFeed = ConfiguracaoFirebase.getFirebaseDatabase().child("postagem").child(idUsuarioLogado);
//
//        databasePostagemFeed.orderByChild("data");
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapterPostagemFeed = new PostagemFeedAdapter(listaPostagemFeed);
//        recyclerView.setAdapter(adapterPostagemFeed);
//
//        databasePostagemFeed.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds: snapshot.getChildren())
//                {
//                    PostagemFeed postagemFeed = ds.getValue(PostagemFeed.class);
//
//                    FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
//
//                    String tmpNome = usuarioPerfil.getDisplayName();
//                    if(tmpNome != null){
//                        postagemFeed.setNome(usuarioPerfil.getDisplayName());
//                    }
//
//                    Uri tmpCaminhoFoto = usuarioPerfil.getPhotoUrl();
//                    if(tmpCaminhoFoto != null){
//                        postagemFeed.setCaminhoFotoFeed(usuarioPerfil.getPhotoUrl().toString());
//                    }
//
//                    listaPostagemFeed.add(postagemFeed);
//                }
//                Collections.reverse(listaPostagemFeed);
//                adapterPostagemFeed.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    private void showProgressBar() {
        progressBarFeed.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBarFeed.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(this, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_feed, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.icone_toolbar_busca).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.icone_toolbar_busca).getActionView();
        searchView.setQueryHint("Pesquisar postagem...");
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}