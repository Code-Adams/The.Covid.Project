package com.sakshmbhat.thecovidproject.Authentication_and_Registration;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class setDate implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private EditText editText;
    private Calendar myCalendar;
    private Context ctx;
    private RelativeLayout relativeLayout;


    public setDate(EditText editText, RelativeLayout relativeLayoutContainingSpinner, Context getCtx){
        this.editText = editText;
        this.editText.setOnClickListener(this);
        myCalendar = Calendar.getInstance();
        this.ctx=getCtx;
        this.relativeLayout=relativeLayoutContainingSpinner;


}
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)     {
        // this.editText.setText();

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        editText.setText(sdformat.format(myCalendar.getTime()));

        checkAge(sdformat.format(myCalendar.getTime()));

    }

    private void checkAge(String format) {

        int ths,hunds,tens,ones,year;
        char cThs,cHunds,cOnes,cTens;
        cThs= format.charAt(6);
        cHunds= format.charAt(7);
        cTens=format.charAt(8);
        cOnes= format.charAt(9);

        ths= cThs-48;
        hunds= cHunds-48;
        tens=cTens-48;
        ones=cOnes-48;
        year=1000*ths+100*hunds+10*tens+ones;
        if(year > 1960)
        {
            relativeLayout.setVisibility(View.VISIBLE);
        }else{
            relativeLayout.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View v) {

        v.clearFocus();

            new DatePickerDialog( ctx,this, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();


    }

}
