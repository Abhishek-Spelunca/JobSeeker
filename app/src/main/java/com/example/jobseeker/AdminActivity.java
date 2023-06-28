package com.example.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AdminActivity extends AppCompatActivity {

    TextView logout;
    FirebaseAuth auth;
    SwipeRefreshLayout swipe;

    FloatingActionButton fab;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference reference;
    ValueEventListener eventListener;
    androidx.appcompat.widget.SearchView searchView;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        auth=FirebaseAuth.getInstance();

        logout=findViewById(R.id.admin_logout);
        fab=findViewById(R.id.fab);
        recyclerView=findViewById(R.id.recyclerView);
        searchView=findViewById(R.id.search);
        searchView.clearFocus();
        swipe=findViewById(R.id.swipe1);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(AdminActivity.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);


        AlertDialog.Builder builder=new AlertDialog.Builder(AdminActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();


        dataList=new ArrayList<>();

        adapter=new MyAdapter(AdminActivity.this,dataList);
        recyclerView.setAdapter(adapter);

        reference= FirebaseDatabase.getInstance().getReference("Jobs");
        dialog.show();

        eventListener=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot:snapshot.getChildren()){
                    DataClass dataClass=itemSnapshot.getValue(DataClass.class);
                    dataClass.setKey(itemSnapshot.getKey());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                rearrangeItems();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this,UploadJobActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(AdminActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            }
        });
    }
    public void searchList(String text){
        ArrayList<DataClass> searchList=new ArrayList<>();
        for (DataClass dataClass:dataList){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
    private void rearrangeItems() {
        Collections.shuffle(dataList,new Random(System.currentTimeMillis()));
        adapter=new MyAdapter(this,dataList);
        recyclerView.setAdapter(adapter);
    }
}