package com.example.jobseeker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context context;
    private List<DataClass> dataList;

    public MyAdapter(Context context, List<DataClass> dataList) {

        this.context = context;
        this.dataList = dataList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getLogo()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getDataTitle());
        holder.recCompany.setText(dataList.get(position).getDataCompany());
        holder.recAddress.setText(dataList.get(position).getDataLocation());
        holder.recPay.setText(dataList.get(position).getDataPay());
        holder.recType.setText(dataList.get(position).getDataType());
        holder.recUrl.setText(dataList.get(position).getDataCompanyUrl());
        holder.recDesc.setText(dataList.get(position).getDataDesc());
        holder.recDate.setText(dataList.get(position).getDataDate());

        String admin= FirebaseAuth.getInstance().getCurrentUser().getUid();


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (admin.equals("5iCUYJUk2FRUcMKMmwmPgGz7tHH3")){//checking for Admin
                    intent = new Intent(context, AdminDetailActivity.class);
                }else {
                    intent = new Intent(context, DetailActivity.class);
                }
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getLogo());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());
                intent.putExtra("Company", dataList.get(holder.getAdapterPosition()).getDataCompany());
                intent.putExtra("Location", dataList.get(holder.getAdapterPosition()).getDataLocation());
                intent.putExtra("PayScale", dataList.get(holder.getAdapterPosition()).getDataPay());
                intent.putExtra("Type", dataList.get(holder.getAdapterPosition()).getDataType());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDesc());
                intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDataDate());
                intent.putExtra("CompanyUrl",dataList.get(holder.getAdapterPosition()).getDataCompanyUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList){
        dataList=searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle,recCompany,recUrl,recType,recPay,recAddress,recDesc,recDate;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage=itemView.findViewById(R.id.recImg);
        recTitle=itemView.findViewById(R.id.rec_title);
        recCompany=itemView.findViewById(R.id.rec_company);
        recPay=itemView.findViewById(R.id.rec_pay);
        recType=itemView.findViewById(R.id.rec_type);
        recAddress=itemView.findViewById(R.id.rec_location);
        recUrl=itemView.findViewById(R.id.rec_Url);
        recDesc=itemView.findViewById(R.id.rec_Desc);
        recDate=itemView.findViewById(R.id.rec_Created);
        recCard=itemView.findViewById(R.id.recCard);



    }
}
