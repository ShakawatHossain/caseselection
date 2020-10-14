package com.example.caseselection.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.caseselection.R;
import com.example.caseselection.interfaces.CalenderInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalenderDialog extends Dialog {
    CalenderInterface calenderInterface;
    int day,mth,yr;
    DatePicker datePicker;
    Button btn;
    String title_name;
    TextView editText;
    public CalenderDialog(@NonNull Context context, CalenderInterface calenderInterface, String date, String name, TextView editText) {
        super(context);
        this.calenderInterface = calenderInterface;
        this.title_name = name;
        this.editText = editText;
        if(date.isEmpty() || date=="null"){
            Date dt = new Date();
//      System.out.println(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            day = cal.get(Calendar.DAY_OF_MONTH);
            mth = cal.get(Calendar.MONTH);
            yr = cal.get(Calendar.YEAR);
        }else{
            SimpleDateFormat spec_format = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date dt = spec_format.parse(date);
//          System.out.println(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dt);
                day = cal.get(Calendar.DAY_OF_MONTH);
                mth = cal.get(Calendar.MONTH);
                yr = cal.get(Calendar.YEAR);
            }catch (ParseException parseException){

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cld_view);
        datePicker = (DatePicker) findViewById(R.id.cald);
        datePicker.init(this.yr,this.mth,this.day,null);
        btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String dat = String.valueOf(datePicker.getYear())+"-"+
                    String.valueOf(datePicker.getMonth()+1)+"-"+String.valueOf(datePicker.getDayOfMonth());
            calenderInterface.getDate(dat,editText);
            dismiss();
        }
    };
}
