package com.example.caseselection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caseselection.fragments.CalenderDialog;
import com.example.caseselection.interfaces.CalenderInterface;
import com.example.caseselection.model.DataStore;
import com.example.caseselection.model.DatabaseHelper;
import com.example.caseselection.model.InfoStore;
import com.example.caseselection.model.PageControl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InfoActivity extends AppCompatActivity implements CalenderInterface {
    TextView spec_c_date,case_id;
    AutoCompleteTextView orgUnit,dis,up;
    EditText mob,rel_seq,name,age,add;
    RadioGroup relation,sex;
    Button submit;
    TableRow row_rel_s;

    ProgressBar pBar;
    DatabaseHelper db;
    private FusedLocationProviderClient fusedLocationClient;
//    private LocationManager locationManager;
//    private String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_info);
        init();
    }


    private void init(){
        db = new DatabaseHelper(InfoActivity.this);

        orgUnit = (AutoCompleteTextView) findViewById(R.id.orgUnit);
        orgUnit.setText(InfoStore.orgUnit);
        orgUnit.setAdapter(getAdapter(db.getFacility()));
        spec_c_date = (TextView) findViewById(R.id.spec_c_date);
        spec_c_date.setText(InfoStore.spec_c_date);
        spec_c_date.setOnClickListener(listener);
        mob = (EditText) findViewById(R.id.mob);
        mob.setText(InfoStore.mob);
        relation = (RadioGroup) findViewById(R.id.relation);
        relation.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.relation == "SELF") relation.check(R.id.rel_s);
        else if(InfoStore.relation == "CHILD") relation.check(R.id.rel_ch);
        else if(InfoStore.relation == "SPOUSE") relation.check(R.id.rel_sp);
        else if(InfoStore.relation == "BROSIS") relation.check(R.id.rel_oth);
        row_rel_s = (TableRow) findViewById(R.id.row_rel_s);
        rel_seq = (EditText) findViewById(R.id.rel_saku);
        rel_seq.setText(InfoStore.relation_seq);
        rel_seq.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String gen_id= mob.getText().toString();
                    if(InfoStore.relation == "CHILD") gen_id+="1";
                    else if(InfoStore.relation == "SPOUSE") gen_id+="2";
                    else if(InfoStore.relation == "BROSIS") gen_id+="9";
                    gen_id+=rel_seq.getText();
                    case_id.setText(gen_id);
                    InfoStore.relation_seq = Integer.parseInt(rel_seq.getText().toString());
                }
            }
        });
//        add listener
        case_id = (TextView) findViewById(R.id.case_id);
        case_id.setText(InfoStore.cls_id);

        name = (EditText) findViewById(R.id.name);
        name.setText(InfoStore.name);
        age = (EditText) findViewById(R.id.age);
        age.setText(InfoStore.age);
        sex = (RadioGroup) findViewById(R.id.sex);
        sex.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.sex=="MALE") sex.check(R.id.male);
        else if(InfoStore.sex=="FEMALE") sex.check(R.id.female);
        else sex.check(R.id.others);
        dis = (AutoCompleteTextView) findViewById(R.id.dis);
        dis.setText(InfoStore.dis);
        dis.setAdapter(getAdapter(db.getDIS()));
        up = (AutoCompleteTextView) findViewById(R.id.up);
        up.setText(InfoStore.up);
        up.setAdapter(getAdapter(db.getUP()));
        add = (EditText) findViewById(R.id.add);
        add.setText(InfoStore.add);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(listener);
    }
// AutoComplete adapter
    private ArrayAdapter<String> getAdapter(Cursor c){
        ArrayList<String> str_arr = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                str_arr.add(c.getString(c.getColumnIndex("name")));
            }while (c.moveToNext());
        }
        return new ArrayAdapter<String>(InfoActivity.this,android.R.layout.simple_dropdown_item_1line,str_arr);
    }
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(group.getId()==R.id.relation){
                if (checkedId == R.id.rel_s){
                    case_id.setText(mob.getText().toString()+"00");
                    InfoStore.relation = "SELF";
                    if(((TableRow) findViewById(R.id.row_rel_s)).getVisibility()==View.VISIBLE) {
                        ((TableRow) findViewById(R.id.row_rel_s)).setVisibility(GONE);
                        rel_seq.setText("");
                    }
                }else if(checkedId == R.id.rel_ch){
                    InfoStore.relation = "CHILD";
                    if(((TableRow) findViewById(R.id.row_rel_s)).getVisibility()!=View.VISIBLE)
                        ((TableRow) findViewById(R.id.row_rel_s)).setVisibility(View.VISIBLE);
                }else if(checkedId == R.id.rel_sp){
                    InfoStore.relation = "SPOUSE";
                    if(((TableRow) findViewById(R.id.row_rel_s)).getVisibility()!=View.VISIBLE)
                        ((TableRow) findViewById(R.id.row_rel_s)).setVisibility(View.VISIBLE);
                }else if(checkedId == R.id.rel_oth){
                    InfoStore.relation = "BROSIS";
                    if(((TableRow) findViewById(R.id.row_rel_s)).getVisibility()!=View.VISIBLE)
                        ((TableRow) findViewById(R.id.row_rel_s)).setVisibility(View.VISIBLE);
                }
            }else if(group.getId()==R.id.sex){
                if(checkedId == R.id.male)
                    InfoStore.sex = "MALE";
                else if(checkedId == R.id.female)
                    InfoStore.sex = "FEMALE";
                else
                    InfoStore.sex = "OTHERS";
            }
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.spec_c_date){
                new CalenderDialog(InfoActivity.this,InfoActivity.this,
                        InfoStore.spec_c_date,"SCD",spec_c_date).show();
                return;
            }
            setValues();
            if(!checkValues()){
                return;
            }
            btn_toggle();
            sendData();
        }
    };
    private void setValues(){
//        dis,up,city,mc,thana;
        InfoStore.orgUnit = orgUnit.getText().toString();
        InfoStore.spec_c_date = spec_c_date.getText().toString();
        InfoStore.mob = mob.getText().toString();
        InfoStore.cls_id = case_id.getText().toString();
        InfoStore.name = name.getText().toString();
        InfoStore.age = age.getText().toString();
        InfoStore.dis = dis.getText().toString();
        InfoStore.up = up.getText().toString();
        InfoStore.add = add.getText().toString();
    }

    private boolean checkValues(){
        if (InfoStore.name.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InfoStore.mob.isEmpty() || InfoStore.mob.length()!=11){
            Toast.makeText(InfoActivity.this,"Check Mobile Number",Toast.LENGTH_SHORT).show();
            return false;
        }else if(InfoStore.relation.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert relation with mobile owner",Toast.LENGTH_SHORT).show();
            return false;
        }else if(InfoStore.age.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert age",Toast.LENGTH_SHORT).show();
            return false;
        }else if(InfoStore.sex.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert sex",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InfoStore.dis.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert District name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(InfoStore.up.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert Upazila name",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void sendData(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String link = "http://34.87.96.172/";
//    http://control-center.corona.gov.bd/
        String url = link+"api/gateway/register";
//        if (InfoStore.id>0) {
//            url = "http://119.40.84.187/surveillance/api/v1/casesubmit/"+String.valueOf(InfoStore.id);
//        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_toggle();
                        Log.d("login",response);
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String msg = jsonObject.getString("msg");
                            if(success == 200) {
//                                JSONObject login_user = jsonObject.getJSONObject("login_user");
//                                DataStore.id = login_user.getInt("id");
//                                DataStore.username = login_user.getJSONObject("employee").getString("name");
//                                startActivity(new Intent(MainActivity.this, InfoActivity.class));
                                Toast.makeText(InfoActivity.this,msg,Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(InfoActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_toggle();
                Toast.makeText(InfoActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", DataStore.Token_type+" "+DataStore.Access_token);
                return params;
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                String mRequestBody = "";
                try {
                    JSONObject jsonObject = new JSONObject();
                    Cursor corg = db.getFacility(InfoStore.orgUnit);
                    String org="";
                    if(corg.moveToFirst())
                       org  = corg.getString(corg.getColumnIndex(DatabaseHelper.SELF_ID));
                    jsonObject.put("orgUnit",org);
                    jsonObject.put("trackedEntityType", "oZYvB4PszOC");
                    JSONArray exceptions = new JSONArray();
                    JSONObject jobj = new JSONObject();
                    jobj.put("orgUnit",org);
                    jobj.put("program","uYjxkTbwRNf ");
                    jobj.put("enrollmentDate",InfoStore.spec_c_date);
                    jobj.put("incidentDate",InfoStore.spec_c_date);
                    exceptions.put(jobj);
                    jsonObject.put("enrollments",exceptions);

                    JSONArray attributes = new JSONArray();
                    JSONObject jmob = new JSONObject();
                    jmob.put("attribute","fctSQp5nAYl");
                    jmob.put("value",InfoStore.mob);
                    attributes.put(jmob);
                    JSONObject jrel = new JSONObject();
                    jrel.put("attribute","zxiFiP1zayz");
                    jrel.put("value",InfoStore.relation);
                    attributes.put(jrel);
                    JSONObject jname = new JSONObject();
                    jname.put("attribute", "BJRySVYQjqz");
                    jname.put("value", InfoStore.name);
                    attributes.put(jname);
                    JSONObject jage = new JSONObject();
                    jage.put("attribute", "oTeSYfM9FoG");
                    jage.put("value", InfoStore.age);
                    attributes.put(jage);
                    JSONObject jgen = new JSONObject();
                    jgen.put("attribute", "MqvgUDLekRe");
                    jgen.put("value", InfoStore.sex);
                    attributes.put(jgen);
                    JSONObject jadd = new JSONObject();
                    jadd.put("attribute", "Xhdn49gUd52");
                    jadd.put("value", InfoStore.add);
                    attributes.put(jadd);
                    JSONObject jdis = new JSONObject();
                    jdis.put("attribute", "i5jFnYJvSXD");
                    jdis.put("value", InfoStore.dis);
                    attributes.put(jdis);
                    JSONObject jup = new JSONObject();
                    jup.put("attribute", "z2yA9jLAJWq");
                    Cursor cup = db.getFacility(InfoStore.up);
                    String sup="";
                    if(cup.moveToFirst())
                        sup  = cup.getString(cup.getColumnIndex(DatabaseHelper.SELF_ID));
                    jup.put("value",sup);
                    attributes.put(jup);
                    jsonObject.put("attributes",attributes);
                    mRequestBody = jsonObject.toString();
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                }catch (JSONException je){
                    je.printStackTrace();
                    return null;
                }catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    private void btn_toggle(){
        if(pBar.getVisibility()== GONE) pBar.setVisibility(View.VISIBLE); else pBar.setVisibility(GONE);
        if(submit.getVisibility()== GONE) submit.setVisibility(View.VISIBLE); else submit.setVisibility(GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        finish();
    }

    @Override
    public void getDate(String dat, TextView editText) {
        editText.setText(dat);
    }
}
