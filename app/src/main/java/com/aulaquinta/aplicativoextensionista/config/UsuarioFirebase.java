package com.aulaquinta.aplicativoextensionista.config;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.aulaquinta.aplicativoextensionista.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class UsuarioFirebase {

    //FirebaseAuth auth = FirebaseAuth.getInstance();


    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAuth();
        return usuario.getCurrentUser();
    }

    public static void atualizarNomeUsuario(String nome){
        try{
            FirebaseUser usuarioLogado = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nome)
                    .build();
            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }
                }
            });
        } catch (Exception e) {
            Log.d("exception", (String) Objects.requireNonNull(e.getMessage()));
        }
    }

    public static void atualizarFotoUsuario(Uri url){
        try{
            FirebaseUser usuarioLogado = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri(url)
                    .build();
            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar foto de perfil");
                    }
                }
            });
        } catch (Exception e) {
            Log.d("exception", (String) Objects.requireNonNull(e.getMessage()));
        }
    }

    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseUser = getUsuarioAtual();

        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setId(firebaseUser.getUid());

        if(firebaseUser.getPhotoUrl() == null){
            usuario.setCaminhoFoto("");
        }else{
            usuario.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }
        return usuario;
    }

    public static String getIdentificadorUsuario(){
        FirebaseUser usuarioLogado = getUsuarioAtual();
        return usuarioLogado.getUid().toString();
    }
}
