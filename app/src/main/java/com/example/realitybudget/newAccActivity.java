package com.example.realitybudget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class newAccActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailInput;
    private EditText passwordInput;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acc);

        auth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passInput);
        signUpBtn = findViewById(R.id.newUserBtn);


    }

    public void accAdded(View v){
        String user = emailInput.getText().toString().trim();
        String pass = passwordInput.getText().toString().trim();

        if (user.isEmpty()){
            emailInput.setError("Email cannot be empty");
        }
        if(pass.isEmpty()){
            passwordInput.setError("Password cannot be empty");
        }else{
            auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(newAccActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(newAccActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(newAccActivity.this, "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }



    public void goBack(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

    }
}