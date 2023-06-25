package com.example.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView back_btn, detailTitle,detailCompany,titleCompany,detailLocation,detailPay,detailType,detailDesc,detailDate,detailWeb;
    ImageView detailImage;
    Button apply_btn,save_btn;
    String key="";
    String imageUrl="";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailTitle=findViewById(R.id.detailTitle);
        detailCompany=findViewById(R.id.detailCompany);
        detailLocation=findViewById(R.id.detailLocation);
        detailPay=findViewById(R.id.detailPayScale);
        detailType=findViewById(R.id.detailType);
        detailDesc=findViewById(R.id.detailDesc);
        detailDate=findViewById(R.id.detailCreated);
        titleCompany=findViewById(R.id.detailCompany2);
        detailWeb=findViewById(R.id.company_website);
        apply_btn=findViewById(R.id.apply);
        back_btn=findViewById(R.id.back);
        save_btn=findViewById(R.id.save);

        detailImage=findViewById(R.id.detailLogo);

        bundle=getIntent().getExtras();

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
        detailDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailDesc.setMaxLines(Integer.MAX_VALUE);
            }
        });

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }
    private void showBottomDialog(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        TextView urlLayout=dialog.findViewById(R.id.url);
        Button closeDialog=dialog.findViewById(R.id.close);
        bundle=getIntent().getExtras();
        if (bundle!=null){
            urlLayout.setText(bundle.getString("CompanyUrl"));
        }
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void saveData(){
        bundle=getIntent().getExtras();

        if (bundle!=null){
            String title=bundle.getString("Title");
            String company=bundle.getString("Company");
            String location=bundle.getString("Location");
            String pay=bundle.getString("PayScale");
            String type=bundle.getString("Type");
            String desc=bundle.getString("Description");
            String date=bundle.getString("Date");
            String web=bundle.getString("CompanyUrl");
            key=bundle.getString("Key");
            imageUrl=bundle.getString("Image");


            DataClass dataClass=new DataClass(title,company,type,pay,location,desc,date,web,imageUrl);
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Saved Jobs").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(title).exists()){
                        removeData();
                    }else{


                    ref.child(title).setValue(dataClass).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(DetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {onBackPressed();}
            });
        }
    }
    public void removeData(){
        String title=bundle.getString("Title");
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Saved Jobs");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(title).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DetailActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}