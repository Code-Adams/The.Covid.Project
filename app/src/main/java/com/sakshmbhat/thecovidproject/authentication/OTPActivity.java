package com.sakshmbhat.thecovidproject.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sakshmbhat.thecovidproject.R;

public class OTPActivity extends AppCompatActivity {

    private EditText otp;
    private Button verifyOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        otp=findViewById(R.id.otpField);
        verifyOTP=findViewById(R.id.verifyOTP);
    }
}