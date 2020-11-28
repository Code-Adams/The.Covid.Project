package com.sakshmbhat.thecovidproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
//import android.content.Context;

public class requestAdapter extends  RecyclerView.Adapter<requestAdapter.requestViewHolder> {

    private List<RequestData> list;
    private Context context;
    //private String category;

    //Constructor for list and context
    public requestAdapter(List<RequestData> passedList, Context passedContext) {
        this.list = passedList;
        this.context = passedContext;
        //this.category=passedCategory;
    }

    @NonNull
    @Override
    public requestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.simple_request_item,parent,false);

        //Return object of requestViewHolder
        return new requestViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull requestViewHolder holder, int position) {

        final RequestData requestData = list.get(position);

        holder.name.setText(requestData.getCreatorName());
        holder.UID.setText(requestData.getGenerator());
        holder.phoneNumber.setText(requestData.getPhoneNumber());
        holder.deptName.setText(requestData.getDepartment_Name());
        holder.productName.setText(requestData.getItem_Name());
        holder.quantity.setText(requestData.getItem_Quantity());

        try {
            Picasso.get().load(requestData.getUserImageurl()).into(holder.creatorImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, holder.phoneNumber.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent Cintent = new Intent(Intent.ACTION_CALL);
                Cintent.setData(Uri.parse("tel:"+holder.phoneNumber.getText().toString().trim()));

                context.startActivity(Cintent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class requestViewHolder extends RecyclerView.ViewHolder {

        private TextView  name,UID,phoneNumber,deptName,productName,quantity;
        private MaterialButton callUser;
        private CircleImageView creatorImage;

        public requestViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameOfRequestgenerator);
            UID = itemView.findViewById(R.id.fakeTextUid);
            phoneNumber = itemView.findViewById(R.id.fakeTextPhoneNumber);
            deptName = itemView.findViewById(R.id.departmentDisplay);
            productName= itemView.findViewById(R.id.productDisplay);
            quantity=itemView.findViewById(R.id.quantityDisplay);
            callUser=itemView.findViewById(R.id.callPerson);
            creatorImage=itemView.findViewById(R.id.userPicDisplay);


        }
    }

}

