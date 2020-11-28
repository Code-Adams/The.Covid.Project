package com.sakshmbhat.thecovidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.android.material.snackbar.Snackbar;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.CDATASection;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    //Don't modify their content once initial initialization is done
    private FloatingActionButton fAB;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int flag=0;

    private DatabaseReference rootReference;

    private RecyclerView requestRecycler;
    private ArrayList<RequestData> requestDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(authenticationAndRegistrationCheck())
        {
            initialiseData();
            setAdapter();
        }






        fAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Aintent = new Intent(MainActivity.this,AddRequest.class);
                startActivity(Aintent);
            }
        });



    }

    private void setAdapter() {
         requestDataList = new ArrayList<>();
        rootReference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    rootReference.child("Requests").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot Snapshot) {

                            if(Snapshot.exists()){

                                for(DataSnapshot dataSnapshot : Snapshot.getChildren()){

                                    if(dataSnapshot.child("Department_Name").getValue().toString().equals(snapshot.child("department").getValue().toString())){

                                        if(!dataSnapshot.child("Generator").getValue().toString().equals(currentUser.getUid())){

                                            RequestData requestData = dataSnapshot.getValue(RequestData.class);
                                            requestDataList.add(requestData);




                                        }

                                    }
                                    RequestData requestData = dataSnapshot.getValue(RequestData.class);
                                    requestDataList.add(requestData);

                                }
                                              requestRecycler.setHasFixedSize(true);
                                              requestRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                              requestAdapter adapter = new requestAdapter(requestDataList, MainActivity.this);
                                              requestRecycler.setAdapter(adapter);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialiseData() {

        rootReference= FirebaseDatabase.getInstance().getReference();
        requestRecycler= findViewById(R.id.requestRecycler);
        fAB=findViewById(R.id.myfab);

    }

    Boolean authenticationAndRegistrationCheck()
     {
         mAuth =FirebaseAuth.getInstance();
         currentUser= mAuth.getCurrentUser();

         if (currentUser == null) {
            //Send user to phone login activity
            Intent intent = new Intent(MainActivity.this, PhoneNumberActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return false;
        }else if(currentUser!=null){
             FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                         if(snapshot.exists()&& !snapshot.hasChild(currentUser.getUid())){

                             Intent Rintent = new Intent(MainActivity.this, RegistrationAvtivity.class);
                             Rintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                             Rintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                             flag=1;
                             startActivity(Rintent);
                             finish();


                         }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
             if(flag==1)
             return false;
             else
                 return true;
         }
         else{
             return  true;
         }
     }

//        private void setAdapterForRequests () {
//
//
//        // queryCurrentDepartment=rootref.child("Requests").orderByChild("Department_Name").equalTo(currentUserDept);
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUser.getUid() + "/");
//        DatabaseReference check = FirebaseDatabase.getInstance().getReference().child("Users");
//        check.child(mCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //Toast.makeText(MainActivity.this, snapshot.child("phoneNumber").getValue().toString(), Toast.LENGTH_SHORT).show();
//                String tempCurrentUserDept = snapshot.child("department").getValue().toString();
//                currentDeptSaver(tempCurrentUserDept);
////                DatabaseReference requestReference= FirebaseDatabase.getInstance().getReference("Requests/");
////               //Toast.makeText(MainActivity.this, requestReference.toString(), Toast.LENGTH_SHORT).show();
////                requestReference.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot Snapshot) {
////                       // Toast.makeText(MainActivity.this, Snapshot.child("phoneNumber").getValue().toString(), Toast.LENGTH_SHORT).show();
////                        for(DataSnapshot dataSnapshot :Snapshot.getChildren())
////                        {
////                            Toast.makeText(MainActivity.this, Snapshot.child("Item_Name").getValue().toString(), Toast.LENGTH_SHORT).show();
////                            if(dataSnapshot.child("Department_Name").getValue().toString().equals(currentUserDept)&&!dataSnapshot.child("Generator").getValue().toString().equals(mCurrentUser.getUid()))
////                            {
////                                RequestData requestData= dataSnapshot.getValue(RequestData.class);
////                                requestDataList.add(requestData);
////                            }
////
////                        }
////
////                        recycler.setHasFixedSize(true);
////                        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
////                        requestAdapter adapter = new requestAdapter(requestDataList,MainActivity.this);
////                        recycler.setAdapter(adapter);
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError error) {
////
////                    }
////                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        DatabaseReference newRequestReference = FirebaseDatabase.getInstance().getReference("Requests");
//
//        newRequestReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot Snapshot) {
//                Toast.makeText(MainActivity.this, currentUserDept, Toast.LENGTH_SHORT).show();
//                requestDataList = new ArrayList<>();
//
//                if (Snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : Snapshot.getChildren()) {
//                        Toast.makeText(MainActivity.this, dataSnapshot.child("Department_Name").getValue().toString() + "Thisone", Toast.LENGTH_SHORT).show();
//                        if (dataSnapshot.child("Department_Name").getValue().toString().equals(currentUserDept))//&&!dataSnapshot.child("Generator").getValue().toString().equals(mCurrentUser.getUid()))
//                        {
//
//                        }
//
//                        RequestData requestData = dataSnapshot.getValue(RequestData.class);
//                        requestDataList.add(requestData);
//
//                    }
//
//                    recycler.setHasFixedSize(true);
//                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    requestAdapter adapter = new requestAdapter(requestDataList, MainActivity.this);
//                    recycler.setAdapter(adapter);
//
//
//                } else {
//                    Toast.makeText(MainActivity.this, currentUserDept, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//
//    }
//
//        private void currentDeptSaver (String tempCurrentUserDept){
//
//        currentUserDept = tempCurrentUserDept;
//        // Toast.makeText(MainActivity.this,currentUserDept, Toast.LENGTH_SHORT).show();
//    }
//
//        //See if user is logged-in in on START METHOD if not then login first
//        @Override
//        protected void onStart () {
//        super.onStart();
//
//        checkRegistrationAndAuthentication();
//
//    }
//
//        private void checkRegistrationAndAuthentication () {
//
//        if (mCurrentUser == null) {
//            //Send user to phone login activity
//            Intent intent = new Intent(MainActivity.this, PhoneNumberActivity.class);
//            //Don't allow user to come back on pressing back button by clearing task and top
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        } else {
//            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            databaseReference = FirebaseDatabase.getInstance().getReference();
//            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (!snapshot.hasChild(uid)) {
//                        Intent intent = new Intent(MainActivity.this, RegistrationAvtivity.class);
//                        startActivity(intent);
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//
//        }
//
//    }
//    }
}