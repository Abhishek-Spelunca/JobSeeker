package com.example.jobseeker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnFailureListener;
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


public class HomeFragment extends Fragment {


    FirebaseAuth auth;

    Spinner type;
    SwipeRefreshLayout swipe;
    LinearLayout err;
    TextView text;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference reference;
    ValueEventListener eventListener;
    androidx.appcompat.widget.SearchView searchView,locationView;
    MyAdapter adapter;
    ArrayList<DataClass> searchList;

    String[] types={"full-time","part-time","temporary","internship","default"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

        auth=FirebaseAuth.getInstance();

        swipe=v.findViewById(R.id.swipe);

        recyclerView=v.findViewById(R.id.recyclerView);
        err=v.findViewById(R.id.error);
        type=v.findViewById(R.id.spinner);
        text=v.findViewById(R.id.text);
        searchView=v.findViewById(R.id.search);
        locationView=v.findViewById(R.id.searchLocation);
        searchView.clearFocus();
        locationView.clearFocus();

        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(gridLayoutManager);


        AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();
        if (isConnected())
        {
            recyclerView.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            dataList=new ArrayList<>();

            adapter=new MyAdapter(getActivity(),dataList);
            recyclerView.setAdapter(adapter);
            reference= FirebaseDatabase.getInstance().getReference("Jobs");
            dialog.show();
            eventListener=reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dataList.clear();
                    recyclerView.setVisibility(View.VISIBLE);
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

        }else {
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
        locationView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchLocation(newText);
                return true;
            }
        });

        ArrayAdapter ad=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,types);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(ad);
        type.setSelection(4);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ty=types[position];
                if (ty.equals("default")){
                    rearrangeItems();
                }else {
                    searchtype(ty);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                rearrangeItems();
            }
        });

        return v;
    }

    private void rearrangeItems() {
        Collections.shuffle(dataList,new Random(System.currentTimeMillis()));
        adapter=new MyAdapter(getActivity(),dataList);
        recyclerView.setAdapter(adapter);
    }

    public void searchList(String text){
        searchList=new ArrayList<>();
        for (DataClass dataClass:dataList){
            if (dataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
    public void searchLocation(String text){
         searchList=new ArrayList<>();
        for (DataClass dataClass:dataList){
            if (dataClass.getDataLocation().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
    public void searchtype(String text){
        searchList=new ArrayList<>();
        for (DataClass dataClass:dataList){
            if (dataClass.getDataType().toLowerCase().contains(text.toLowerCase())){
                searchList.add(dataClass);
            }
        }
        adapter.searchDataList(searchList);
    }
    private boolean isConnected(){
        ConnectivityManager cm=(ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo active=cm.getActiveNetworkInfo();
        if (active!=null && active.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
