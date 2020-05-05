package br.com.ifood.cursoandroid.ifood.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.ifood.cursoandroid.ifood.R;
import br.com.ifood.cursoandroid.ifood.helper.ConfiguracaoFirebase;
import br.com.ifood.cursoandroid.ifood.helper.UsuarioFirebase;
import br.com.ifood.cursoandroid.ifood.model.Usuario;

public class ConfiguracoesUsuarioActivity extends AppCompatActivity {

    private EditText editUsuarioNome, editUsuarioEmail, editTelefone, editSenha;
    private Button buttonConfirmar;
    private String idUsuario;
    private DatabaseReference firebaseRef;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_usuario);

        //Configurações iniciais
        inicializarComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
//        idUsuario = UsuarioFirebase.getIdUsuario();
//        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //Configurações Toolbar
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Configurações usuário");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editUsuarioEmail.getText().toString();
                String senha = editSenha.getText().toString();

                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {

                        //Verifica estado do switch
                        autenticacao.createUserWithEmailAndPassword(
                                email, senha
                        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    Toast.makeText(ConfiguracoesUsuarioActivity.this,
                                            "Cadastro realizado com sucesso!",
                                            Toast.LENGTH_SHORT).show();
                                    String tipoUsuario = getIntent().getStringExtra("type");
                                    UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                    abrirTelaPrincipal(tipoUsuario);

                                }else {

                                    String erroExcecao = "";

                                    try{
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        erroExcecao = "Digite uma senha mais forte!";
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        erroExcecao = "Por favor, digite um e-mail válido";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        erroExcecao = "Este conta já foi cadastrada";
                                    } catch (Exception e) {
                                        erroExcecao = "ao cadastrar usuário: "  + e.getMessage();
                                        e.printStackTrace();
                                    }

                                    Toast.makeText(ConfiguracoesUsuarioActivity.this,
                                            "Erro: " + erroExcecao ,
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }else {
                        Toast.makeText(ConfiguracoesUsuarioActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ConfiguracoesUsuarioActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Recupera dados do usuário
//        recuperarDadosUsuario();

    }

    private void recuperarDadosUsuario(){

        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child( idUsuario );

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getValue() != null ){

                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    editUsuarioNome.setText( usuario.getNome() );
                    editUsuarioEmail.setText( usuario.getEndereco() );

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void validarDadosUsuario(View view){

        //Valida se os campos foram preenchidos
        String nome = editUsuarioNome.getText().toString();
        String endereco = editUsuarioEmail.getText().toString();

        if( !nome.isEmpty()){
            if( !endereco.isEmpty()){

                Usuario usuario = new Usuario();
                usuario.setIdUsuario( idUsuario );
                usuario.setNome( nome );
                usuario.setEndereco( endereco );
                usuario.salvar();

                exibirMensagem("Dados atualizados com sucesso!");
                finish();

            }else{
                exibirMensagem("Digite seu endereço completo!");
            }
        }else{
            exibirMensagem("Digite seu nome!");
        }

    }

    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }

    private void inicializarComponentes(){
        editUsuarioNome = findViewById(R.id.editUsuarioNome);
        editUsuarioEmail = findViewById(R.id.editUsuarioEndereco);
        editTelefone = findViewById(R.id.editTelefone);
        editSenha = findViewById(R.id.editSenha);
        buttonConfirmar = findViewById(R.id.buttonAcesso);
    }

    private void abrirTelaPrincipal(String tipoUsuario){
        if(tipoUsuario.equals("E")){//empresa
            startActivity(new Intent(getApplicationContext(), EmpresaActivity.class));
        }else{//usuario
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }
    }

}
