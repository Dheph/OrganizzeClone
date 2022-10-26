package com.example.organizzeclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.organizzeclone.R;
import com.example.organizzeclone.activity.home.HomeActivity;
import com.example.organizzeclone.config.FirebaseConfig;
import com.example.organizzeclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private TextView loginEmail, loginPassword;
    private Button loginButton;
    private User user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                if (!email.isEmpty()) {
                    if (!password.isEmpty()) {
                        user = new User(email, password);
                        Log.d("registerorganizze", user.getEmail());
                        handleLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, "Adicione o password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Adicione o email", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void handleLogin() {
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    cleanFields();
                    goToHomePage();
                } else {
                    String exception = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "O email informado é inválido";
                    } catch (FirebaseAuthInvalidUserException e) {
                        exception = "Verifique o email e senha e tente novamente";
                    } catch (Exception e) {
                        exception = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToHomePage() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void cleanFields() {
        loginEmail.setText("");
        loginPassword.setText("");
    }
}