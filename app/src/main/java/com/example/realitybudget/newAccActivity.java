package com.example.realitybudget;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class newAccActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailInput, passwordInput, fullNameInput, phoneInput;
    private Button signUpBtn;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_acc);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        fullNameInput =findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passInput);
        phoneInput = findViewById(R.id.phoneInput);
        signUpBtn = findViewById(R.id.newUserBtn);


    }

    public void accAdded(View v){
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String pass = passwordInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        if (email.isEmpty()){
            emailInput.setError("Email cannot be empty");
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            emailInput.setError("Please enter a valid email");
        }

        if (fullName.isEmpty()){
            fullNameInput.setError("Name cannot be empty");
        }
        else if(!fullName.matches("^[a-zA-Z]+ ?[a-zA-Z]+$")){
            fullNameInput.setError("Please enter valid name");
        }

        if(phone.isEmpty()){
            phoneInput.setError("Phone cannot be empty");
        } else if (!phone.matches("^\\d{10}$")) {
            phoneInput.setError("Please enter a valid phone number");
        }

        if(pass.isEmpty()){
            passwordInput.setError("Password cannot be empty");
        }else if(!pass.isEmpty() && pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")){
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(newAccActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        userID = auth.getCurrentUser().getUid();
                        DocumentReference docRef = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fName",fullName);
                        user.put("email",email);
                        user.put("password", pass);
                        user.put("phone", phone);
                        docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                            }
                        });
                        startActivity(new Intent(newAccActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(newAccActivity.this, "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            passwordInput.setError("Password is not valid");
        }
    }



    public void goBack(View v){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);

    }
}