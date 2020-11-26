package com.sakshmbhat.thecovidproject.Authentication_and_Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sakshmbhat.thecovidproject.MainActivity;
import com.sakshmbhat.thecovidproject.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationAvtivity extends AppCompatActivity {

    private Spinner department;
    private EditText editTextFromDate,firstNameField,lastNameField,addressField,phoneNumberField,apartmentnumberField;
    private Button submit;
    private String deptSelected;
    Boolean ageLessThanSixty=false;
    private RelativeLayout relativeLayoutContainingSpinner;
    private TextView clickToAddImage;
    private CircleImageView   circleImageView;

    Dialog dialog;

    private  final int REQ=1;
    private Bitmap bitmap=null;

    private AlertDialog dialogBox;
    private AlertDialog.Builder dialogBuilder;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private StorageReference storageReference;
    private DatabaseReference databaseReference,dbRef;

   private String firstName;
   private String lastName;
   private String dob;
   private String phoneNumber;
   private String address;
   private String apartment;
   private String downloadUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initialiseParameters();
        setSpinner();
        Toast.makeText(this, mCurrentUser.getUid(), Toast.LENGTH_SHORT).show();
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                deptSelected = department.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        clickToAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDataFields();
            }
        });
    }

    private void initialiseParameters() {


        department=findViewById(R.id.deptCategorySpinner);
        editTextFromDate= findViewById(R.id.birthDay);
        firstNameField=findViewById(R.id.firstName);
        lastNameField=findViewById(R.id.lastName);
        addressField=findViewById(R.id.address);
        phoneNumberField=findViewById(R.id.phoneNumber);
        apartmentnumberField=findViewById(R.id.apartmentNo);
        submit=findViewById(R.id.submitBtn);
        relativeLayoutContainingSpinner=findViewById(R.id.relativeLayoutWithSpinner);
        clickToAddImage=findViewById(R.id.clickToAddImage);
        circleImageView=findViewById(R.id.userPic);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        mCurrentUser= mAuth.getCurrentUser();
        //Setting calender on edit text
        setDate DOB= new setDate(editTextFromDate,relativeLayoutContainingSpinner,RegistrationAvtivity.this);

    }

    private void verifyDataFields() {

         firstName=firstNameField.getText().toString().trim();
         lastName=lastNameField.getText().toString().trim();
         dob = editTextFromDate.getText().toString().trim();
         phoneNumber=phoneNumberField.getText().toString().trim();
         address=addressField.getText().toString().trim();
         apartment=apartmentnumberField.getText().toString().trim();

        if(firstName.isEmpty()){
            firstNameField.setError("First name required!");
            firstNameField.requestFocus();
        }else if(lastName.isEmpty()){
            lastNameField.setError("Last name required!");
            lastNameField.requestFocus();
        }else if(dob.isEmpty()){
            editTextFromDate.setError("Birthday required!");
            editTextFromDate.requestFocus();
        }else if(checkAge(dob)&&deptSelected.equals("Select Department")){
                       SetError("Please choose a department!");
                Toast.makeText(RegistrationAvtivity.this, phoneNumber, Toast.LENGTH_SHORT).show();
                // ((TextView)department.getChildAt(0)).setError("Message");
        }else if(phoneNumber.isEmpty()){
            phoneNumberField.setError("Phone no required!");
            phoneNumberField.requestFocus();
        }else if(phoneNumber.length()!=10){
            phoneNumberField.setError("Enter a valid mobile number");
            phoneNumberField.requestFocus();
        }else if(address.isEmpty()) {
            addressField.setError("Address required!");
            addressField.requestFocus();
        }else if(apartment.isEmpty()) {
            apartmentnumberField.setError("Apartment no. required!");
            apartmentnumberField.requestFocus();
        }else if(bitmap==null){

            //If image is not selected confirm from user if he/she is sure?
            //First build a dialog
            dialogBuilder = new AlertDialog.Builder(RegistrationAvtivity.this);
            dialogBuilder.setTitle("Proceed without an image?");
            //Create negative and positive responses and add click listener to them.
            dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialogBox.dismiss();
                    uploadUserData();

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialogBox.dismiss();

                }
            });
            //create dialog box and show it
            try {
                dialogBox = dialogBuilder.create();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogBox.show();

        }else{
            uploadImage();
        }
    }

    private void uploadImage() {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        dialog = new Dialog(RegistrationAvtivity.this);
        dialog.setTitle("Uploading Image");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        ByteArrayOutputStream BOAS= new ByteArrayOutputStream();

        //Compress the file
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,BOAS);
        byte[] finalImageForUpload= BOAS.toByteArray();
        //Creating File Path
        final StorageReference imageFilePath;
        imageFilePath= storageReference.child("Users").child(finalImageForUpload+"jpg");

        //Creating Task to upload
        final UploadTask uploadTask = imageFilePath.putBytes(finalImageForUpload);

        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //getting download url
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    //saving DownloadUrl
                                    downloadUrl=String.valueOf(uri);
                                    //Upload to database along with otherdata
                                    dialog.cancel();
                                    uploadUserData();
                                }
                            });
                        }
                    });
                }else{

                    dialog.dismiss();
                    Toast.makeText(RegistrationAvtivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegistrationAvtivity.this, "Try again!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void uploadUserData() {
        dialog.cancel();
        dialog = new Dialog(RegistrationAvtivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if(deptSelected.equals("Select Department"))
        {
            deptSelected="Senior Citizen";
        }
        //Add user to department colmn to
        dbRef=databaseReference.child("Departments").child(deptSelected).child("Users");
        dbRef.push().setValue(mCurrentUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                userData data= new userData(firstName,lastName,downloadUrl,mCurrentUser.getUid(),dob,deptSelected,phoneNumber,address,apartment,"0");

                dbRef=databaseReference.child("Users").child(mCurrentUser.getUid());

                dbRef.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.cancel();
                        Toast.makeText(RegistrationAvtivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(RegistrationAvtivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.cancel();
                        Toast.makeText(RegistrationAvtivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                        Toast.makeText(RegistrationAvtivity.this, "Try again.",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationAvtivity.this, "Opps! Something went wrong.",Toast.LENGTH_SHORT).show();
                Toast.makeText(RegistrationAvtivity.this, "Try again.",Toast.LENGTH_SHORT).show();

            }
        });




    }

    private void setSpinner() {

        String[] items= new String[] {"Select Department","A","B","C","D"};

        department.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

    }

    Boolean checkAge(String dob) {

        int ths,hunds,tens,ones,year;
        char cThs,cHunds,cOnes,cTens;
        cThs= dob.charAt(6);
        cHunds= dob.charAt(7);
        cTens=dob.charAt(8);
        cOnes= dob.charAt(9);

        ths= cThs-48;
        hunds= cHunds-48;
        tens=cTens-48;
        ones=cOnes-48;
        year=1000*ths+100*hunds+10*tens+ones;
        if(year > 1960)
        {
            return true;
        }else{
            return false;
        }


    }
    public void SetError(String errorMessage){
        View view = department.getSelectedView();

        // Set TextView in Secondary Unit spinner to be in error so that red (!) icon
        // appears, and then shake control if in error
        TextView tvListItem = (TextView)view;

        // Set fake TextView to be in error so that the error message appears
        TextView tvInvisibleError = (TextView)findViewById(R.id.tvInvisibleError);

        // Shake and set error if in error state, otherwise clear error
        if(errorMessage != null)
        {
            tvListItem.setError(errorMessage);
            tvListItem.requestFocus();

            // Shake the spinner to highlight that current selection
            // is invalid -- SEE COMMENT BELOW
            Animation shake = AnimationUtils.loadAnimation(RegistrationAvtivity.this, R.anim.shake);
            department.startAnimation(shake);

            tvInvisibleError.requestFocus();
            tvInvisibleError.setError(errorMessage);
        }
        else
        {
            tvListItem.setError(null);
            tvInvisibleError.setError(null);
        }
    }

    private void openGallery() {

        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult(getImage, REQ);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== REQ && resultCode ==RESULT_OK){

            assert data != null;
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Set bitmap to show image preview in circle image view
            circleImageView.setImageBitmap(bitmap);

        }

    }


}