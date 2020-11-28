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

     private FirebaseAuth mAuth;
     private FirebaseUser mCurrentUser;
     private DatabaseReference databaseReference;
    private FloatingActionButton mfab;
    private TextView fakeTextForUid,getFakeTextForPhonenumber;
    private List<RequestData> requestDataList;
    RecyclerView mRecyclerView;
    DatabaseReference rootref;
    RecyclerView recycler;
    Query queryCurrentDepartment;
    String currentUserDept="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fakeTextForUid=findViewById(R.id.fakeTextUid);
        getFakeTextForPhonenumber=findViewById(R.id.fakeTextPhoneNumber);
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser();
        recycler=findViewById(R.id.requestRecycler);
        rootref=FirebaseDatabase.getInstance().getReference();

        mfab=findViewById(R.id.myfab);

        if(mCurrentUser!=null)
        {
            setAdapterForRequests();
        }


        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Aintent = new Intent(MainActivity.this,AddRequest.class);
                startActivity(Aintent);
            }
        });



    }

    private void setAdapterForRequests() {


       // queryCurrentDepartment=rootref.child("Requests").orderByChild("Department_Name").equalTo(currentUserDept);
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    currentUserDept= snapshot.child("department").getValue().toString();
                    DatabaseReference requestReference= FirebaseDatabase.getInstance().getReference().child("Requests");
                    requestReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            requestDataList= new ArrayList<>();

                            if(snapshot.exists())
                            {

                                for(DataSnapshot dataSnapshot :snapshot.getChildren())
                                {
                                    if(dataSnapshot.child("Department_Name").getValue().toString().equals(currentUserDept)&&!dataSnapshot.child("Generator").getValue().toString().equals(mCurrentUser.getUid()))
                                    {
                                               RequestData requestData= dataSnapshot.getValue(RequestData.class);
                                               requestDataList.add(requestData);
                                    }

                                }

                                recycler.setHasFixedSize(true);
                                recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                requestAdapter adapter = new requestAdapter(requestDataList,MainActivity.this);
                                recycler.setAdapter(adapter);


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


    //See if user is logged-in in on START METHOD if not then login first
    @Override
    protected void onStart() {
        super.onStart();

        checkRegistrationAndAuthentication();



    }

         private void checkRegistrationAndAuthentication() {

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
        else {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.hasChild(uid)) {
                        Intent intent = new Intent(MainActivity.this, RegistrationAvtivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

}