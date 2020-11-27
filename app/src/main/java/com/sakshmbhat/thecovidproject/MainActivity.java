package com.sakshmbhat.thecovidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sakshmbhat.thecovidproject.Authentication_and_Registration.PhoneNumberActivity;
import com.sakshmbhat.thecovidproject.Authentication_and_Registration.RegistrationAvtivity;

public class MainActivity extends AppCompatActivity {

     private FirebaseAuth mAuth;
     private FirebaseUser mCurrentUser;
     private DatabaseReference databaseReference;
    private FloatingActionButton mfab;
    RecyclerView mrecyclerView;
    DatabaseReference rootref;
    Query queryCurrentDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootref=FirebaseDatabase.getInstance().getReference().child("Requests");
        queryCurrentDepartment=rootref.orderByChild("Department_Name").equalTo("A");
        mfab=findViewById(R.id.myfab);
        mrecyclerView=(RecyclerView)findViewById(R.id.Unique_work_assigned);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerView.setHasFixedSize(true);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddRequest.class);
                startActivity(intent);
            }
        });



        mAuth= FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser();

    }

    //See if user is logged-in in on START METHOD if not then login first
    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser==null)
        {
            //Send user to phone login activity
            Intent intent= new Intent(MainActivity.this, PhoneNumberActivity.class);
            //Don't allow user to come back on pressing back button by clearing task and top
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(uid)){
                    Intent intent =new Intent(MainActivity.this, RegistrationAvtivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseRecyclerAdapter<simpleRequest,RequestViewHolder> adapter=new FirebaseRecyclerAdapter<simpleRequest, RequestViewHolder>(
                simpleRequest.class,
                R.layout.simple_request_item,
                RequestViewHolder.class,
                queryCurrentDepartment
        ) {
            @Override
            protected void populateViewHolder(RequestViewHolder requestViewHolder, simpleRequest simpleRequest, int i) {
                requestViewHolder.setDepartmentName(simpleRequest.getDepartment_Name());
                requestViewHolder.setItemName(simpleRequest.getItem_Name());
                requestViewHolder.setQuantity(simpleRequest.getItem_Quantity());
            }
        };


        mrecyclerView.setAdapter(adapter);
    }
    public  static class RequestViewHolder extends RecyclerView.ViewHolder
    {
        View myview;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            myview=itemView;
        }
        void setDepartmentName(String dname)
        {
            TextView t1=myview.findViewById(R.id.Assigned_itemName);
            t1.setText(dname);
        }
        void setItemName(String dname)
        {
            TextView t2=myview.findViewById(R.id.Assigned_itemQuantity);
            t2.setText(dname);
        }
        void setQuantity(String dname)
        {
            TextView t3=myview.findViewById(R.id.AssignedBy);
            t3.setText(dname);
        }
    }
}