package com.example.jobseeker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadJobActivity extends AppCompatActivity {

    ImageView logo;
    Button uploadBtn;
    EditText uploadTitle,uploadCompany,uploadType,uploadUrl,uploadPay,uploadLocation,uploadDesc,uploadDate;
    String imageUrl;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_job);

        logo=findViewById(R.id.logo);
        uploadTitle=findViewById(R.id.job_title);
        uploadCompany=findViewById(R.id.company_name);
        uploadType=findViewById(R.id.job_type);
        uploadUrl=findViewById(R.id.job_url);
        uploadPay=findViewById(R.id.pay_scale);
        uploadLocation=findViewById(R.id.location);
        uploadDate=findViewById(R.id.Date);
        uploadDesc=findViewById(R.id.upload_description);
        uploadBtn=findViewById(R.id.add_btn);

        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent data=result.getData();
                            uri=data.getData();
                            logo.setImageURI(uri);
                        }else {
                            Toast.makeText(UploadJobActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker=new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/+");
                activityResultLauncher.launch(photoPicker);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData(){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Companies Logo")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder=new AlertDialog.Builder(UploadJobActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog=builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage=uriTask.getResult();
                imageUrl=urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

    public void uploadData(){
        String title=uploadTitle.getText().toString();
        String company=uploadCompany.getText().toString();
        String companyUrl=uploadUrl.getText().toString();
        String type=uploadType.getText().toString();
        String payScale=uploadPay.getText().toString();
        String location=uploadLocation.getText().toString();
        String date=uploadDate.getText().toString();
        String desc=uploadDesc.getText().toString();

       DataClass dataClass=new DataClass(title,company,type,payScale,location,desc,date,companyUrl,imageUrl);

        FirebaseDatabase.getInstance().getReference("Jobs").child(title).setValue(dataClass).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UploadJobActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UploadJobActivity.this, AdminActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadJobActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}