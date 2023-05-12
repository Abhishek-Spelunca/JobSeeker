package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    EditText loginUsername, loginPassword;
    Button loginBtn;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_pass);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUsers();
                }
            }
        });
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUsers() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(userUsername);

        if (checkInput(userUsername)) {
            auth.signInWithEmailAndPassword(userUsername, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(LoginActivity.this, HomePageActivity.class);
                    intent.putExtra("loginInfo",userUsername);
                    startActivity(intent);                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (userUsername.equals("admin") && userPassword.equals("admin123")) {
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        } else {
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        loginUsername.setError(null);
                        String passDB = snapshot.child(userUsername).child("password").getValue(String.class);

                        if (!Objects.equals(passDB, userPassword)) {
                            loginUsername.setError(null);

                            String usernameDB=snapshot.child(userUsername).child("username").getValue(String.class);
                            String emailDB=snapshot.child(userUsername).child("email").getValue(String.class);


                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(LoginActivity.this, HomePageActivity.class);
                            intent.putExtra("loginInfo",userUsername);
                            startActivity(intent);

                        } else {
                            loginPassword.setError("Invalid");
                            loginPassword.requestFocus();
                        }
                    } else {
                        loginUsername.setError("Invalid");
                        loginUsername.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public Boolean checkInput(String userInput) {
        return Patterns.EMAIL_ADDRESS.matcher(userInput).matches();
    }

}