package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {


    TextView username,name,email,age,address,qualification,number,experience;
    ImageView profilePic,resume;
    Button editBtn;
    FirebaseUser auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        auth= FirebaseAuth.getInstance().getCurrentUser();
        username=v.findViewById(R.id.profileUsername);
        name=v.findViewById(R.id.profileName);
        email=v.findViewById(R.id.profileEmail);
        experience=v.findViewById(R.id.profileExperience);
        age=v.findViewById(R.id.profileAge);
        address=v.findViewById(R.id.profileAddress);
        number=v.findViewById(R.id.profilePhone);
        qualification=v.findViewById(R.id.profileQualify);
        editBtn=v.findViewById(R.id.edit_btn);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Users");

        Query profile=reference.orderByChild("uid").equalTo(auth.getUid());

        profile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String n=snapshot.child(auth.getUid()).child("profile").child("name").getValue(String.class);
                    if (n==null) name.setText("name");
                    name.setText(""+n);

                    String ag=snapshot.child(auth.getUid()).child("profile").child("age").getValue(String.class);
                    if (ag==null) age.setText("age");
                    age.setText(""+ag);

                    String add=snapshot.child(auth.getUid()).child("profile").child("address").getValue(String.class);
                    if (add==null) address.setText("address");
                    address.setText(""+add);

                    String num=snapshot.child(auth.getUid()).child("profile").child("phone").getValue(String.class);
                    if (num==null) number.setText("number");
                    number.setText(""+num);

                    String qual=snapshot.child(auth.getUid()).child("profile").child("qualify").getValue(String.class);
                    if (qual==null) qualification.setText("qualification");
                    qualification.setText(""+qual);

                    String exp=snapshot.child(auth.getUid()).child("profile").child("experience").getValue(String.class);
                    if (exp==null) experience.setText("Experience");
                    experience.setText(""+exp);

                    String un=snapshot.child(auth.getUid()).child("username").getValue(String.class);
                    username.setText(""+un);

                    String em=snapshot.child(auth.getUid()).child("email").getValue(String.class);
                    email.setText(""+em);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),UpdateProfileActivity.class));
            }
        });
        return v;
    }
}