package com.example.kinocastefm_app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth=FirebaseAuth.getInstance();
        progressBar=binding.progressBar;
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.toLogin.setOnClickListener(this);
        binding.registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==binding.toLogin.getId()){
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            String email=String.valueOf(binding.emailInput.getText());
            String username=String.valueOf(binding.usernameInput.getText());
            String password=String.valueOf(binding.passwordInput.getText());
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(RegisterActivity.this, "Empty data", Toast.LENGTH_SHORT).show();
                return ;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser userFb = mAuth.getCurrentUser();
                                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                Map<String, Object> user=new HashMap<>();
                                user.put("uid", userFb.getUid());
                                user.put("username", username);
                                firestore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(RegisterActivity.this, "Registered with success", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(), UserSpaceActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Your registration failed"+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}