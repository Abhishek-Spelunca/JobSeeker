package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitle,detailCompany,titleCompany,detailLocation,detailPay,detailType,detailDesc,detailDate,detailWebsite;
    ImageView detailImage;
    String key="";
    String imageUrl="";

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
        detailWebsite=findViewById(R.id.detailWebsite);

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
            detailWebsite.setText(bundle.getString("CompanyUrl"));
            titleCompany.setText(bundle.getString("Company"));
            key=bundle.getString("Key");
            imageUrl=bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);
        }

    }
}