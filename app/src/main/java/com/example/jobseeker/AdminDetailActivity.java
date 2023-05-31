package com.example.jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdminDetailActivity extends AppCompatActivity {

    TextView backBtn,detailTitle,detailCompany,detailLocation,detailPay,detailType,detailDesc,detailDate,titleCompany,detailWeb;
    ImageView detailImage;
    FloatingActionButton deleteButton;
    String key="";
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);

        detailTitle=findViewById(R.id.detailTitle);
        detailCompany=findViewById(R.id.detailCompany);
        detailLocation=findViewById(R.id.detailLocation);
        detailPay=findViewById(R.id.detailPayScale);
        detailType=findViewById(R.id.detailType);
        detailDesc=findViewById(R.id.detailDesc);
        detailDate=findViewById(R.id.detailCreated);
        deleteButton=findViewById(R.id.deleteBtn);
        titleCompany=findViewById(R.id.detailCompany2);
        detailWeb=findViewById(R.id.company_website);
        backBtn=findViewById(R.id.previous);



        detailImage=findViewById(R.id.detailLogo);


        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            detailTitle.setText(bundle.getString("Title"));
            detailCompany.setText(bundle.getString("Company"));
            detailLocation.setText(bundle.getString("Location"));
            detailPay.setText(bundle.getString("PayScale"));
            detailType.setText(bundle.getString("Type"));
            detailDesc.setText(bundle.getString("Description"));
            detailDate.setText(bundle.getString("Date"));
            detailWeb.setText(bundle.getString("CompanyUrl"));
            titleCompany.setText(bundle.getString("Company"));
            key=bundle.getString("Key");
            imageUrl=bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Jobs");
                FirebaseStorage storage=FirebaseStorage.getInstance();

                StorageReference storageReference=storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        reference.child(key).removeValue();
                        Toast.makeText(AdminDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                        finish();
                    }
                });
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}