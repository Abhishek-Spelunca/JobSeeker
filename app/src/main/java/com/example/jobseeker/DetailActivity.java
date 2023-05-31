package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView back_btn, detailTitle,detailCompany,titleCompany,detailLocation,detailPay,detailType,detailDesc,detailDate,detailWeb;
    ImageView detailImage;
    Button apply_btn;
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
}