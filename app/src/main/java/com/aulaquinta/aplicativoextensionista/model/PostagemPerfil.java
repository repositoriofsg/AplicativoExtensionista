package com.aulaquinta.aplicativoextensionista.model;

public class PostagemPerfil {

    private String titulo, descricao;

    public PostagemPerfil() {
    }

    public PostagemPerfil(String descricao, String titulo) {
        this.descricao = descricao;
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTitulo() {
        return titulo;
    }
}
