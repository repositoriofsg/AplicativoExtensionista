package com.aulaquinta.aplicativoextensionista.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aulaquinta.aplicativoextensionista.R;
import com.aulaquinta.aplicativoextensionista.activity.CadastroActivity;
import com.aulaquinta.aplicativoextensionista.activity.HomeDoisActivity;
import com.aulaquinta.aplicativoextensionista.activity.LoginActivity;
import com.aulaquinta.aplicativoextensionista.config.UsuarioFirebase;
import com.aulaquinta.aplicativoextensionista.model.Postagem;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class NewPostFragment extends Fragment {
    // - - - - - - - - - - - - - - - - - - - -  [ Componentes ]
    private TextInputEditText textInputTitulo, textInputDescricao;
    private Button buttonPublicar;
    private Spinner spinnerDisciplinas;

    // - - - - - - - - - - - - - - - - - - - -  [ Variaveis ]
    private String idUsuarioLogado;
    private String phoneUsuarioLogado;
    private FirebaseUser firebaseUser;


    public NewPostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_post, container, false);
        //spinnerDisciplinas = rootView.findViewById(R.id.spinnerDisciplinas);
        inicializarComponentesNovaPostagem(rootView);
        initSpinnerNovaPostagem();
        //idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        firebaseUser = UsuarioFirebase.getUsuarioAtual();
        phoneUsuarioLogado = firebaseUser.getPhoneNumber();
        idUsuarioLogado = firebaseUser.getUid();

        buttonPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicarPostagem();
            }
        });
        return rootView;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_post, container, false);
    }
    private void publicarPostagem(){
        Postagem postagem = new Postagem();
        postagem.setIdUsuario(idUsuarioLogado);
        postagem.setTitulo(Objects.requireNonNull(textInputTitulo.getText()).toString());
        postagem.setDescricao(Objects.requireNonNull(textInputDescricao.getText()).toString());
        postagem.setNome(firebaseUser.getDisplayName());
        if(firebaseUser.getPhotoUrl() != null){
            postagem.setCaminhoFoto(firebaseUser.getPhotoUrl().toString());
        }
        postagem.setEmail(firebaseUser.getEmail());
        postagem.setTelefone(firebaseUser.getPhoneNumber());

        if(postagem.salvar()){
            Toast.makeText( requireActivity(), "Postagem salva com sucesso!", Toast.LENGTH_SHORT).show();
        }
        openHomeScreen();
    }

    private void initSpinnerNovaPostagem() {
        String[] items = new String[]{
                "Choose apple", "Choose boy", " Choose cat", "Choose dog",
        };
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, items);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.disciplinas_array,
                android.R.layout.simple_spinner_item
        );

        spinnerDisciplinas.setAdapter(adapter);
        spinnerDisciplinas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
                //Log.v("item", (String) parent.getItemAtPosition(position));
                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void inicializarComponentesNovaPostagem(View view){
        textInputTitulo = view.findViewById(R.id.textInputTitulo);
        textInputDescricao = view.findViewById(R.id.textInputDescricao);
        spinnerDisciplinas = view.findViewById(R.id.spinnerDisciplinas);
        buttonPublicar = view.findViewById(R.id.buttonPublicar);
    }

    public void openHomeScreen(){
        Intent intent = new Intent(requireActivity(), HomeDoisActivity.class); // Temporario
        startActivity(intent);
    }
}