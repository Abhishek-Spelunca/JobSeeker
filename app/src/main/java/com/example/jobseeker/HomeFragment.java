package com.example.jobseeker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;


public class HomeFragment extends Fragment {


    FirebaseAuth auth;
    LinearLayout err;
    ImageView noData;
    TextView text;
    RecyclerView recyclerView;
    List<DataClass> dataList;
    DatabaseReference reference;
    ValueEventListener eventListener;
    androidx.appcompat.widget.SearchView searchView,locationView;
    MyAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_home, container, false);

        auth=FirebaseAuth.getInstance();


        recyclerView=v.findViewById(R.id.recyclerView);
        err=v.findViewById(R.id.error);
        text=v.findViewById(R.id.text);
        noData=v.findViewById(R.id.noData);
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


        return v;
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
    public void searchLocation(String text){
        ArrayList<DataClass> searchLocation=new ArrayList<>();
        for (DataClass dataClass:dataList){
            if (dataClass.getDataLocation().toLowerCase().contains(text.toLowerCase())){
                searchLocation.add(dataClass);
            }
        }
        adapter.searchDataList(searchLocation);
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
