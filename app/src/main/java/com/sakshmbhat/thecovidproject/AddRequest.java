package com.sakshmbhat.thecovidproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class AddRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private  EditText ItemName;
    private EditText Quantity;
    Button AddBtn;
    DatabaseReference mref;
    DatabaseReference mdep;
    TextView textView;
    ProgressBar progressBar;
    String depto="",request_key="",newReq="",uid="",phone="",creatorName="",imageUrl="";
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        Spinner deptSpinner=findViewById(R.id.dept_spinner);
        ArrayAdapter <CharSequence> adapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.Departments, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deptSpinner.setAdapter(adapter);
        deptSpinner.setOnItemSelectedListener(this);
        ItemName=findViewById(R.id.itemName);
        Quantity=findViewById(R.id.quantity);
        textView=findViewById(R.id.simpler);
        AddBtn=findViewById(R.id.AddToDatabase);
        mdep=FirebaseDatabase.getInstance().getReference().child("Departments");
        mref=FirebaseDatabase.getInstance().getReference();
        mauth=FirebaseAuth.getInstance();
        uid=mauth.getCurrentUser().getUid().trim();


        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mitemName=ItemName.getText().toString().trim();
                String mitemQuantity=Quantity.getText().toString().trim();
                if(TextUtils.isEmpty(mitemName)||TextUtils.isEmpty(mitemQuantity))
                {
                    return;
                }
                request_key=mref.child("Requests").push().getKey();
                mref.child("Requests").child(request_key).child("Item_Name").setValue(mitemName);
                mref.child("Requests").child(request_key).child("Item_Quantity").setValue(mitemQuantity);
                mref.child("Requests").child(request_key).child("Department_Name").setValue(depto);
                mref.child("Requests").child(request_key).child("Generator").setValue(uid);
                mref.child("Requests").child(request_key).child("Status").setValue("0");


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            phone = snapshot.child("phoneNumber").getValue().toString();
                            creatorName=snapshot.child("firstName").getValue().toString().trim() +" "+snapshot.child("lastName").getValue().toString().trim();
                            imageUrl=snapshot.child("imageUrl").getValue().toString().trim();
                            mref.child("Requests").child(request_key).child("imageUrl").setValue(imageUrl);
                            mref.child("Requests").child(request_key).child("phoneNumber").setValue(phone);
                            mref.child("Requests").child(request_key).child("creatorName").setValue(creatorName);
                        }
                        else{

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                newReq="Request Number"+new Random().nextInt(100000);
                DatabaseReference mref2=mdep.child(depto).child("Pending_requests").child(newReq);
                mref2.setValue(request_key);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        depto=parent.getItemAtPosition(position).toString();
        textView.setText(depto);
        Toast.makeText(parent.getContext(),depto,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}