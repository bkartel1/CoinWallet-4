package com.example.victorseguido.myapplication.UI.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.victorseguido.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView buttonRegister;
    Button  buttonSignIn;
    EditText editTextEmail, editTextPass;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        buttonRegister = (TextView) findViewById(R.id.register_button);
        buttonSignIn = (Button) findViewById(R.id.btnSignIn);
        editTextEmail = (EditText) findViewById(R.id.login_email);
        editTextPass = (EditText) findViewById(R.id.login_password);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    String email = editTextEmail.getText().toString();
            //    String pass = editTextPass.getText().toString();
            //    registrar(email,pass);
     //           startActivity(new Intent(LoginActivity.this,PortfolioActivity2.class));
                startActivity(new Intent(LoginActivity.this,UserRegister.class));
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });

       buttonSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email = editTextEmail.getText().toString();
               String pass = editTextPass.getText().toString();
               iniciarSesion(email,pass);
           }
       });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){

                    Log.i("SESION","Sesion iniciada con mail: "+ user.getEmail() + "  y con ID:"+ user.getUid());
                    startActivity(new Intent(LoginActivity.this,PortfolioActivity2.class));
                }else{
                    Log.i("Sesion" , "sesion cerrada");
                }
            }
        };
    }


    private void iniciarSesion(String email, String pass){

        Pattern pattern = Pattern.compile("^[A-Za-z0-9._]{1,16}+@{1}+[a-z]{1,7}\\.[a-z]{1,3}$");
        Matcher mail = pattern.matcher(email);

        if((!email.isEmpty() && !pass.isEmpty())|| (pass.length()>5) || mail.find()){

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){ // si usuario se ha creado correctamente
                        Toast.makeText(LoginActivity.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(LoginActivity.this, "Login o Password incorrecto", Toast.LENGTH_SHORT).show();

                       // Log.e("Sesion", task.getException().getMessage()+" ");
                    }
                }
            });
        }else{
            Toast.makeText(LoginActivity.this, "Login o Password incorrecto", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick (View view){ }


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if (mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}