package com.aulaquinta.aplicativoextensionista.model;

import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.Map;

public class Postagem {

    private String id;
    private String idUsuario;
    private String titulo;
    private String descricao;
    private String topicos;
    private String data;
    private String nome;
    private String caminhoFoto;


    public Postagem() {
        DatabaseReference referenceFirebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference referencePostagem = referenceFirebase.child("postagem");
        String idPostagem = referencePostagem.push().getKey();
        setId(idPostagem);
    }

    public boolean salvar(){
        DatabaseReference referenceFirebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference referencePostagem = referenceFirebase.child("postagem")
                .child(getIdUsuario())
                .child(getId());
        referencePostagem.setValue( this );
        return true;
    }

    // [] - - - - - - - - - - - - - - - - - - - -  [ GET ]

    public String getId() {
        return id;
    }
    public String getIdUsuario() {
        return idUsuario;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getTopicos() {
        return topicos;
    }
    public String getNome() {
        return nome;
    }
    public String getCaminhoFoto() {
        return caminhoFoto;
    }
    public Map<String, String> getData() {
        return ServerValue.TIMESTAMP;
    }

    // [] - - - - - - - - - - - - - - - - - - - -  [ SET ]

    public void setId(String id) {
        this.id = id;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setTopicos(String topicos) {
        this.topicos = topicos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
