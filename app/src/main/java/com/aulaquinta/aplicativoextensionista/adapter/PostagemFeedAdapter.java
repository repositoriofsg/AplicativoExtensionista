package com.aulaquinta.aplicativoextensionista.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.activity.EditarPerfilActivity;
import com.aulaquinta.aplicativoextensionista.activity.HomeDoisActivity;
import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.fragment.FeedFragment;
import com.aulaquinta.aplicativoextensionista.model.PostagemFeed;
import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostagemFeedAdapter extends RecyclerView.Adapter<PostagemFeedAdapter.ViewHolder> {

    //private static Context context;
    private ArrayList<PostagemFeed> listaPostagemFeed;
    //private String idUsuario;
    private StorageReference storageReference;

    public PostagemFeedAdapter(ArrayList<PostagemFeed> listaPostagemFeed) {
        this.listaPostagemFeed = listaPostagemFeed;
    }

    @NonNull
    @Override
    public PostagemFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auxiliar_postagem_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostagemFeedAdapter.ViewHolder holder, int position) {

        storageReference = ConfiguracaoFirebase.getFirebaseStorageReference();

        PostagemFeed postagemFeed = listaPostagemFeed.get(position);

        storageReference = storageReference
                .child("imagens")
                .child("perfil")
                .child(postagemFeed.getIdUsuario() + ".jpeg");

        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(holder.CircleImageViewFeed.getContext())
                    .load(uri)
                    .into(holder.CircleImageViewFeed);
        }).addOnFailureListener(e -> {
            holder.CircleImageViewFeed.setImageResource(R.drawable.avatar);
        });

        holder.textViewPostagemFeedNomeUsuario.setText(postagemFeed.getNome());
        holder.textViewPostagemFeedTitulo.setText(postagemFeed.getTitulo());
        holder.textViewPostagemFeedDescricao.setText(postagemFeed.getDescricao());
        holder.textViewPostagemFeedData.setText(postagemFeed.getData());
    }

    @Override
    public int getItemCount() {
        return listaPostagemFeed.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView CircleImageViewFeed;
        TextView textViewPostagemFeedNomeUsuario;
        TextView textViewPostagemFeedTitulo;
        TextView textViewPostagemFeedDescricao;
        TextView textViewPostagemFeedData;

        //String idUsuario;
        //StorageReference storageReference;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CircleImageViewFeed = itemView.findViewById(R.id.CircleImageViewFeed);
            textViewPostagemFeedNomeUsuario = itemView.findViewById(R.id.textViewPostagemFeedNomeUsuario);
            textViewPostagemFeedTitulo = itemView.findViewById(R.id.textViewPostagemFeedTitulo);
            textViewPostagemFeedDescricao = itemView.findViewById(R.id.textViewPostagemFeedDescricao);
            textViewPostagemFeedData = itemView.findViewById(R.id.textViewPostagemFeedData);
        }
    }
}
