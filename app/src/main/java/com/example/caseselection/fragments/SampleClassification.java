package com.example.caseselection.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caseselection.InfoActivity;
import com.example.caseselection.R;
import com.example.caseselection.model.InfoStore;
import com.example.caseselection.model.ListDataStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;


public class SampleClassification extends Dialog {
    Context context;
    CheckBox new_sample,followup_sample,contact_sample,death_sample;
    EditText followup_sample_id,contact_sample_id;
    Button submit;
    ProgressBar progressBar;
    public SampleClassification(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.samle_classification);
        new_sample = (CheckBox) findViewById(R.id.new_sample);
        followup_sample = (CheckBox) findViewById(R.id.followup_sample);
        contact_sample = (CheckBox) findViewById(R.id.contact_sample);
        death_sample = (CheckBox) findViewById(R.id.death_sample);

        followup_sample_id = (EditText) findViewById(R.id.followup_sample_id);
        contact_sample_id = (EditText) findViewById(R.id.contact_sample_id);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(clickListener);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    private void togglebtn(){
        if (progressBar.getVisibility()==GONE) progressBar.setVisibility(View.VISIBLE);
        else progressBar.setVisibility(GONE);
        if (submit.getVisibility()==GONE) submit.setVisibility(View.VISIBLE);
        else submit.setVisibility(GONE);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            togglebtn();
            if(followup_sample.isChecked()){
                InfoStore.cls_id = "BANCOVID"+followup_sample_id.getText().toString();
                InfoStore.cls = 1;
                getData("cls_id",InfoStore.cls_id);
            }else if (contact_sample.isChecked()){
                InfoStore.cls_id = "BANCOVID"+contact_sample_id.getText().toString();
                InfoStore.cls = 2;
                context.startActivity(new Intent(context, InfoActivity.class));
            }else if(death_sample.isChecked()){
                InfoStore.cls = 3;
                context.startActivity(new Intent(context, InfoActivity.class));
            }else{
                InfoStore.cls = 0;
                context.startActivity(new Intent(context, InfoActivity.class));
            }
        }
    };

    private void getData(String key, String value){
        String url ="http://119.40.84.187/surveillance/api/v1/listcase?page=1"+"&"+key+"="+value;
        Log.d("SearchFollowUP",url);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        togglebtn();
                        Log.d("SearchFollowUP",response);
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i=0; i<data.length();i++){
                                JSONObject jObj = data.getJSONObject(i);

                                InfoStore.name = jObj.getString("name");
                                InfoStore.age = String.valueOf(jObj.getInt("age"));
                                InfoStore.mob = jObj.getString("mob");
                                InfoStore.add = jObj.getString("adds");
                                Integer sex = jObj.getInt("sex");
                                InfoStore.sex = sex;
                                Integer age_type = jObj.getInt("age_type");
                                InfoStore.age_type = age_type;
                                InfoStore.email = jObj.getString("email");
                                InfoStore.ocu = jObj.getString("ocu");
//                                InfoStore.coll_ads = jObj.getString("coll_ads");
//                                InfoStore.coll_from = jObj.getInt("coll_from");
//                                InfoStore.ref = jObj.getString("ref");
//                                InfoStore.ins_name = jObj.getString("ins_name");
//                                InfoStore.ins_dept = jObj.getString("ins_dept");
//                                InfoStore.ins_unit = jObj.getString("ins_unit");
//                                InfoStore.ins_unit_head = jObj.getString("ins_unit_head");
//                                InfoStore.ins_ward = jObj.getString("ins_ward");
//                                InfoStore.ins_cabin = jObj.getString("ins_cabin");
//                                InfoStore.fev = jObj.getInt("fev");
//                                InfoStore.headache = jObj.getInt("headache");
//                                InfoStore.cough = jObj.getInt("cough");
//                                InfoStore.breath = jObj.getInt("breath");
//                                InfoStore.onset = jObj.getString("onset");
//                                InfoStore.txt_oth_symp = jObj.getString("txt_oth_symp");
//                                InfoStore.cr = jObj.getInt("cr");
//                                InfoStore.th = jObj.getInt("th");
//                                InfoStore.th_coun = jObj.getString("th_coun");
////                                Date Conversion
//                                String dtStart = jObj.getString("th_date");
//                                Log.d("getDateValue:",dtStart);
//                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                                try {
//                                    Date date = format.parse(dtStart);
////                                    System.out.println(date);
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.setTime(date);
//                                    InfoStore.day = cal.get(Calendar.DAY_OF_MONTH);
//                                    InfoStore.mth = cal.get(Calendar.MONTH);
//                                    InfoStore.yr = cal.get(Calendar.YEAR);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                String dt = jObj.getString("spec_c_date");
//                                Log.d("getDateValue:",dt);
//                                SimpleDateFormat spec_format = new SimpleDateFormat("yyyy-MM-dd");
//                                try {
//                                    Date date = spec_format.parse(dt);
////                                    System.out.println(date);
//                                    Calendar cal = Calendar.getInstance();
//                                    cal.setTime(date);
//                                    InfoStore.spec_d = cal.get(Calendar.DAY_OF_MONTH);
//                                    InfoStore.spec_m = cal.get(Calendar.MONTH);
//                                    InfoStore.spec_yr = cal.get(Calendar.YEAR);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                InfoStore.cc =                 jObj.getInt("cc");
//                                InfoStore.hc =                 jObj.getInt("hc");
//                                InfoStore.copd =                 jObj.getInt("copd");
//                                InfoStore.asthma =                 jObj.getInt("asthma");
//                                InfoStore.ild =                 jObj.getInt("ild");
//                                InfoStore.dm =                 jObj.getInt("dm");
//                                InfoStore.ihd =                 jObj.getInt("ihd");
//                                InfoStore.htn =                 jObj.getInt("htn");
//                                InfoStore.ckd =                 jObj.getInt("ckd");
//                                InfoStore.cld =                 jObj.getInt("cld");
//                                InfoStore.md =                 jObj.getInt("md");
//                                InfoStore.ost =                 jObj.getInt("ost");
//                                InfoStore.prg =                 jObj.getInt("preg");
//                                InfoStore.crf_oth =                 jObj.getString("crf_oth");
//                                InfoStore.spec =                 jObj.getInt("spec");
//                                InfoStore.nasal =                 jObj.getInt("nasal");
//                                InfoStore.throat_swab =                 jObj.getInt("throat_swab");
//                                InfoStore.sputum =                 jObj.getInt("sputum");
//                                InfoStore.tracheal_aspirate =                 jObj.getInt("tracheal_aspirate");
//                                InfoStore.serum =                 jObj.getInt("serum");
//                                InfoStore.spec_oth_txt =                 jObj.getString("spec_oth_txt");
//                                InfoStore.remarks =                 jObj.getString("remarks");
//                                InfoStore.in_cond_by = jObj.getString("in_cond_by");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ListError",e.getMessage());
                        }
                        context.startActivity(new Intent(context,InfoActivity.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                togglebtn();
                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
