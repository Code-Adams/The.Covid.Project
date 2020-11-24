package com.sakshmbhat.thecovidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sakshmbhat.thecovidproject.authentication.PhoneNumberActivity;

public class MainActivity extends AppCompatActivity {

     private FirebaseAuth mAuth;
     private FirebaseUser mCurrentUser;

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

    }
}