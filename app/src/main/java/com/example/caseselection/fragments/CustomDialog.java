package com.example.caseselection.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.caseselection.R;
import com.example.caseselection.interfaces.SearchInterface;

public class CustomDialog extends Dialog {
    SearchInterface searchInterface;
    CheckBox id,name,date;
    EditText txt_id,txt_name,txt_date;
    Button btn;
    public CustomDialog(@NonNull Context context, SearchInterface searchInterface) {
        super(context);
        this.searchInterface = searchInterface;
//        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_blank);
        id = (CheckBox) findViewById(R.id.id);
        name = (CheckBox) findViewById(R.id.name);
        date = (CheckBox) findViewById(R.id.date);

        txt_id = (EditText) findViewById(R.id.txt_id);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_date = (EditText) findViewById(R.id.txt_date);
        btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(onClickListener);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (id.isChecked()){
                searchInterface.search("id",txt_id.getText().toString());
            }else if(name.isChecked()){
                searchInterface.search("name",txt_name.getText().toString());
            }else{
                searchInterface.search("dat",txt_date.getText().toString());
            }
            dismiss();
        }
    };
}
