package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText inputName,inputPhone,inputAge,inputAddress,inputQualify,inputExperience;
    Button uploadBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        auth=FirebaseAuth.getInstance();

        inputName=findViewById(R.id.upload_name);
        inputPhone=findViewById(R.id.upload_phone);
        inputAge=findViewById(R.id.upload_age);
        inputAddress=findViewById(R.id.upload_address);
        inputQualify=findViewById(R.id.upload_qualification);
        inputExperience=findViewById(R.id.upload_experience);
        uploadBtn=findViewById(R.id.upload_btn);


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=inputName.getText().toString();
                String phone=inputPhone.getText().toString();
                String age=inputAge.getText().toString();
                String address=inputAddress.getText().toString();
                String qualify=inputQualify.getText().toString();
                String experience=inputExperience.getText().toString();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");

                ProfileHelper helper=new ProfileHelper(name,phone,age,address,qualify,experience);
                reference.child(auth.getCurrentUser().getUid()).child("profile").setValue(helper);
                Toast.makeText(UpdateProfileActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateProfileActivity.this, ProfileFragment.class));
                finish();
            }
        });
    }
}