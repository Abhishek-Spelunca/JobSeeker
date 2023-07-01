package com.example.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AllJobsActivity extends AppCompatActivity {

    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference reference;
    ValueEventListener eventListener;
    TextView back;
    MyAdapter adapter;
    LinearLayout err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jobs);

        swipe = findViewById(R.id.swipe);
        back=findViewById(R.id.back);

        recyclerView = findViewById(R.id.recyclerView);
        err =findViewById(R.id.error);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        if (isConnected()) {
            recyclerView.setVisibility(View.VISIBLE);
            dataList = new ArrayList<>();

            adapter = new MyAdapter(this, dataList);
            recyclerView.setAdapter(adapter);
            reference = FirebaseDatabase.getInstance().getReference("Jobs");
            dialog.show();
            eventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {

                        DataClass dataClass = itemSnapshot.getValue(DataClass.class);
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

        } else {
            err.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                rearrangeItems();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void rearrangeItems() {
        Collections.shuffle(dataList, new Random(System.currentTimeMillis()));
        adapter = new MyAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
    }
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active = cm.getActiveNetworkInfo();
        if (active != null && active.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}