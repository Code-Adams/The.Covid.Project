package com.sakshmbhat.thecovidproject.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.sakshmbhat.thecovidproject.MainActivity;
import com.sakshmbhat.thecovidproject.R;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class PhoneNumberActivity extends AppCompatActivity {

    private EditText phoneNumber,countryCode;
    private Button submitBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private TextView errorText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        mAuth= FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser();

        phoneNumber=findViewById(R.id.phoneNumberField);
        countryCode=findViewById(R.id.countryCodeField);
        progressBar=findViewById(R.id.progressBar);
        submitBtn=findViewById(R.id.submitBtn);
        errorText=findViewById(R.id.errorText);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryCodeStr = countryCode.getText().toString();
                String phoneNumberStr = phoneNumber.getText().toString();

                if(countryCodeStr.isEmpty()){
                    countryCode.setError("Country code required!");
                    countryCode.requestFocus();
                    errorText.setText(R.string.Multiple_Empty_Field);
                    errorText.setVisibility(View.VISIBLE);
                }else if(phoneNumberStr.isEmpty()){
                    phoneNumber.setError("Phone number is must!");
                    phoneNumber.requestFocus();
                    errorText.setText(R.string.Multiple_Empty_Field);
                    errorText.setVisibility(View.VISIBLE);
                }else{
                    //remove error text
                    errorText.setVisibility(View.GONE);
                    //start progress bar
                    progressBar.setVisibility(View.VISIBLE);
                    //Disable the button
                    submitBtn.setEnabled(false);
                    //get full phone number ie country code + number
                    String fullPhoneNumber=countryCodeStr+" "+phoneNumberStr;
                    errorText.setText(fullPhoneNumber);
                    errorText.setVisibility(View.VISIBLE);
                    //Send a verification code to the user's phone
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            fullPhoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            PhoneNumberActivity.this,
                            mCallbacks);
//                    PhoneAuthOptions options =
//                            PhoneAuthOptions.newBuilder(mAuth)
//                                    .setPhoneNumber(fullPhoneNumber)       // Phone number to verify
//                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                    .setActivity(PhoneNumberActivity.this)                 // Activity (for callback binding)
//                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                    .build();
//                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }
        });

        //Check if verification failed or succeed
       mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
           @Override
           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

           }

           @Override
           public void onVerificationFailed(@NonNull FirebaseException e) {

           }

           @Override
           public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
               super.onCodeSent(s, forceResendingToken);

               //If code is sent to the user goto otp activity
               Intent otpIntent= new Intent(PhoneNumberActivity.this,OTPActivity.class);
               startActivity(otpIntent);
           }
       };

    }


    //See if user is logged-in in on START METHOD if yes send to main activity
    @Override
    protected void onStart() {
        super.onStart();

        if(mCurrentUser!=null)
        {
            //Send user to main activity
            Intent intent= new Intent(PhoneNumberActivity.this,MainActivity.class);
            //Don't allow user to come back on pressing back button by clearing task and top
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
}