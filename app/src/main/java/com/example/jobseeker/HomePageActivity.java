package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav
                , R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View header=navigationView.getHeaderView(0);
        TextView user=header.findViewById(R.id.nav_user);
        TextView email=header.findViewById(R.id.nav_email);


        Intent intent=getIntent();
        String loggedUser=intent.getStringExtra("loginInfo");

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        Query checkType;

        DatabaseReference reference= database.getReference("Users");
        if (Patterns.EMAIL_ADDRESS.matcher(loggedUser).matches()){
            checkType = reference.orderByChild("email").equalTo(loggedUser);
        }else {

            checkType= reference.orderByChild("username").equalTo(loggedUser);
        }

        checkType.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userDB = snapshot.child(loggedUser).child("username").getValue(String.class);
                    String emailDB = snapshot.child(loggedUser).child("email").getValue(String.class);
                    user.setText(""+userDB);
                    email.setText(""+emailDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (itemId == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
        } else if (itemId == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        } else if (itemId == R.id.nav_applied) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AppliedJobFragment()).commit();
        } else if (itemId == R.id.logout) {
            auth.signOut();
            startActivity(new Intent(HomePageActivity.this, LoginActivity.class));
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}