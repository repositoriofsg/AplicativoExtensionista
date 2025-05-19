package com.aulaquinta.aplicativoextensionista.model;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    //private String telefone;

    public Usuario() {
    }

    // Métodos Get

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }



    // Métodos Set

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
