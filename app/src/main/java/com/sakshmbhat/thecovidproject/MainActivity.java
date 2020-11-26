package com.sakshmbhat.thecovidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sakshmbhat.thecovidproject.Authentication_and_Registration.PhoneNumberActivity;
import com.sakshmbhat.thecovidproject.Authentication_and_Registration.RegistrationAvtivity;

public class MainActivity extends AppCompatActivity {

     private FirebaseAuth mAuth;
     private FirebaseUser mCurrentUser;
     private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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


    }
}