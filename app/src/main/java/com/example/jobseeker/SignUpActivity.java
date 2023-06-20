package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;

    EditText signupUsername, signupEmail, signupPass;
    Button signupBtn;
    TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        signupUsername = findViewById(R.id.sign_username);
        signupEmail = findViewById(R.id.sign_email);
        signupPass = findViewById(R.id.sign_pass);
        signupBtn = findViewById(R.id.sign_btn);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = signupUsername.getText().toString();
                String email = signupEmail.getText().toString();
                String pass = signupPass.getText().toString();


                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference("Users");

                            String uid= auth.getUid();

                            HelperClass helperClass = new HelperClass(uid,username,email,pass);
                            reference.child(uid).setValue(helperClass);



                            Toast.makeText(SignUpActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Signed Failed", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

            }
        });
    }
}