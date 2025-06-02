package com.aulaquinta.aplicativoextensionista.model;

import com.aulaquinta.aplicativoextensionista.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String caminhoFoto;

    public Usuario() {

    }

    public void salvar(){
        DatabaseReference referenceFirebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference referenceUsuarios = referenceFirebase.child("usuarios").child(getId());
        referenceUsuarios.setValue( this );
    }

    public void atualizar(){
        DatabaseReference referenceFirebase = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference referenceUsuarios = referenceFirebase
                .child("usuarios")
                .child(getId());

        Map<String, Object> valoresUsuario = converterParaMap();
        referenceUsuarios.updateChildren(valoresUsuario);
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("id", getId());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());
        return usuarioMap;
    }

    // - - - - - - - - - - - - - - - - - - - -  [ GET ]

    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }
    public String getTelefone() {
        return telefone;
    }
    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    // - - - - - - - - - - - - - - - - - - - -  [ SET ]

    public void setId(String id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
