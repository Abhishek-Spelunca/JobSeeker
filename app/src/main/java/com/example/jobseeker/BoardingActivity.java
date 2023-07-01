package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class BoardingActivity extends AppCompatActivity {
    Button board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);
        board = findViewById(R.id.board);


        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BoardingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String admin = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (admin.equals("5iCUYJUk2FRUcMKMmwmPgGz7tHH3")) {//checking for Admin
                startActivity(new Intent(BoardingActivity.this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(BoardingActivity.this, HomePageActivity.class));
                finish();
            }

        }
    }
}