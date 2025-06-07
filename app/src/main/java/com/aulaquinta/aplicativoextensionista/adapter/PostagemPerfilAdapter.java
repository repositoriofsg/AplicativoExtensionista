package com.aulaquinta.aplicativoextensionista.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.model.PostagemPerfil;

import java.util.ArrayList;


public class PostagemPerfilAdapter extends RecyclerView.Adapter<PostagemPerfilAdapter.ViewHolder> {

    ArrayList<PostagemPerfil> listaPostagemPerfil;

    public PostagemPerfilAdapter(ArrayList<PostagemPerfil> listaPostagemPerfil) {
        this.listaPostagemPerfil = listaPostagemPerfil;
    }

    @NonNull
    @Override
    public PostagemPerfilAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auxiliar_postagem_perfil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostagemPerfilAdapter.ViewHolder holder, int position) {
        PostagemPerfil postagemPerfil = listaPostagemPerfil.get(position);
        holder.textViewPostagemPerfilTitulo.setText(postagemPerfil.getTitulo());
        holder.textViewPostagemPerfilDescricao.setText(postagemPerfil.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaPostagemPerfil.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPostagemPerfilTitulo, textViewPostagemPerfilDescricao;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPostagemPerfilTitulo = itemView.findViewById(R.id.textViewPostagemPerfilTitulo);
            textViewPostagemPerfilDescricao = itemView.findViewById(R.id.textViewPostagemPerfilDescricao);
        }
    }
}

