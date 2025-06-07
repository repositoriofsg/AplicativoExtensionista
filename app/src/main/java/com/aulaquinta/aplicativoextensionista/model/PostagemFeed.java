package com.aulaquinta.aplicativoextensionista.model;

import android.net.Uri;

import com.aulaquinta.aplicativoextensionista.helper.HelperData;

public class PostagemFeed {

    private String idUsuario;
    private String caminhoFoto;
    private String nome;
    private String titulo, descricao;
    private Long data;
    private String email;
    private String telefone;

    public PostagemFeed() {
    }

    public PostagemFeed(String caminhoFoto, String nome, String titulo, String descricao, Long data, String email, String telefone) {
        this.caminhoFoto = caminhoFoto;
        this.nome = nome;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.email = email;
        this.telefone = telefone;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    public String getCaminhoFoto() {
        return caminhoFoto;
    }
    public String getNome() {
        return nome;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public Long getDataOrdenarTimeStamp(){
        return data;
    }
    public String getData(){
        try{
            return HelperData.ajustarData(data);
        } catch(Exception e) {
            return "data";
        }
    }
    public String getEmail() {
        return email;
    }
    public String getTelefone() {
        return telefone;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public void setData(Long data) {
        this.data = data;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
