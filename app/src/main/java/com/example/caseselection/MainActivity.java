package com.example.caseselection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caseselection.model.DataStore;
import com.example.caseselection.model.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    EditText uid,pass;
    Button submit,see_db;
    ProgressBar pBar;
    DatabaseHelper db;
    TextView status;
    String link = "http://34.87.96.172/";
//    http://control-center.corona.gov.bd/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check();
        init();
    }

    private void loadFacilities(){
        Cursor c = db.getFacility();
        if(c.moveToFirst()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = link+"api/gateway/get-facilities";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_toggle();
                        Log.d("login",response);
                        status.setText("Facilities loaded done");
                        new FacSave().execute(response);
                        status.setText("Attempt to load District");
                        loadDistricts();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_toggle();
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", DataStore.Token_type+" "+DataStore.Access_token);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void loadDistricts(){
        Cursor c = db.getDIS();
        if(c.moveToFirst()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = link+"api/gateway/get-districts";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        btn_toggle();
                        Log.d("DIS",response);
                        status.setText("District Load done");
                        new DisSave().execute(response);
                        status.setText("Attempt to load UPZ");
                        loadUpazila();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("dis_load",error.getMessage());
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", DataStore.Token_type+" "+DataStore.Access_token);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void loadUpazila(){
        Cursor c = db.getUP();
        if(c.moveToFirst()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = link+"api/gateway/get-upazilas";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        btn_toggle();
                        Log.d("UPZ",response);
                        status.setText("Upazila Load done");
                        new UpSave().execute(response);
                        status.setText("Attempt to down lab!");
                        loadLab();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("up_load",error.getMessage());
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", DataStore.Token_type+" "+DataStore.Access_token);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void loadLab(){
        Cursor c = db.getLab();
        if(c.moveToFirst()) {
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = link+"/api/gateway/get-labs";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
    //                        btn_toggle();
                        Log.d("labs",response);
                        status.setText("LAB Load done");
                        new LabSave().execute(response);
                        check();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("up_load",error.getMessage());
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", DataStore.Token_type+" "+DataStore.Access_token);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void check(){
        if(DataStore.Access_token == null ){
            Toast.makeText(MainActivity.this,"Enter Valid login credentials",Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(MainActivity.this,InfoActivity.class));
        }
    }

    private void init(){
        db = new DatabaseHelper(MainActivity.this);
        uid = (EditText) findViewById(R.id.user_id);
        pass = (EditText) findViewById(R.id.pass);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this.listener);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        see_db = (Button) findViewById(R.id.see_db);
        see_db.setOnClickListener(this.listener);
        status = (TextView) findViewById(R.id.status);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.see_db){
                printDB();
                return;
            }
            if (uid.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Please fill user name",Toast.LENGTH_SHORT).show();
                return;
            }else if(pass.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Please fill password",Toast.LENGTH_SHORT).show();
                return;
            }
            btn_toggle();
            status.setText("Attempt Login");
            check_login();
        }
    };

    private void check_login(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = link+"api/auth/login";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        btn_toggle();
                        Log.d("login",response);
                        status.setText("Login Success");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            DataStore.Access_token = jsonObject.getString("access_token");
                            DataStore.Token_type = jsonObject.getString("token_type");
                            DataStore.Expires_at = jsonObject.getString("expires_at");
                            status.setText("Attempt load facilities");
                            loadFacilities();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this,"Enter Valid login credentials",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_toggle();
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",uid.getText().toString());
                params.put("password",pass.getText().toString());
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void btn_toggle(){
        if(pBar.getVisibility()==View.GONE) pBar.setVisibility(View.VISIBLE); else pBar.setVisibility(View.GONE);
        if(submit.getVisibility()==View.GONE) submit.setVisibility(View.VISIBLE); else submit.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
    private void printDB(){
        Cursor c = db.getDIS();
        Log.d("DISTotal",String.valueOf(c.getCount()));
        if(c.moveToFirst()){
            do {
                Log.d("DIS",c.getString(c.getColumnIndex(DatabaseHelper.NAME)));
            }while (c.moveToNext());
        }
        c.close();
        Cursor up = db.getUP();
        Log.d("UPTotal",String.valueOf(up.getCount()));
        if(up.moveToFirst()){
            do {
                Log.d("UP",String.valueOf(up.getString(up.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+up.getString(up.getColumnIndex(DatabaseHelper.NAME)));
            }while (up.moveToNext());
        }
        up.close();
//        Cursor mc = db.getMC();
//        Log.d("MCTotal",String.valueOf(mc.getCount()));
//        if(mc.moveToFirst()){
//            do {
//                Log.d("MC",String.valueOf(mc.getInt(mc.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+mc.getString(mc.getColumnIndex(DatabaseHelper.NAME))+"-"+mc.getString(mc.getColumnIndex(DatabaseHelper.PARENT_ID)));
//            }while (mc.moveToNext());
//        }
//        mc.close();
//        Cursor city = db.getCITY();
//        Log.d("CITYTotal",String.valueOf(city.getCount()));
//        if(city.moveToFirst()){
//            do {
//                Log.d("CITY",String.valueOf(city.getInt(city.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+city.getString(city.getColumnIndex(DatabaseHelper.NAME))+"-"+city.getString(city.getColumnIndex(DatabaseHelper.PARENT_ID)));
//            }while (city.moveToNext());
//        }
//        city.close();
//        Cursor thana = db.getTHANA();
//        Log.d("THANATotal",String.valueOf(thana.getCount()));
//        if(thana.moveToFirst()){
//            do {
//                Log.d("Thana",String.valueOf(thana.getInt(thana.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+thana.getString(thana.getColumnIndex(DatabaseHelper.NAME))+"-"+thana.getString(thana.getColumnIndex(DatabaseHelper.PARENT_ID)));
//            }while (thana.moveToNext());
//        }
//        thana.close();
        Cursor facility = db.getFacility();
        Log.d("FACILITYTotal",String.valueOf(facility.getCount()));
        if(facility.moveToFirst()){
            do {
                Log.d("Facility",String.valueOf(facility.getString(facility.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+facility.getString(facility.getColumnIndex(DatabaseHelper.NAME)));
            }while (facility.moveToNext());
        }
        facility.close();
        Cursor lab = db.getLab();
        Log.d("Lab",String.valueOf(lab.getCount()));
        if(lab.moveToFirst()){
            do {
                Log.d("LAB",lab.getString(lab.getColumnIndex(DatabaseHelper.NAME)));
            }while (lab.moveToNext());
        }
        lab.close();
    }

    public class FacSave extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray facilities = jsonObject.getJSONArray("facilities");
                for(int i=0;i<facilities.length();i++){
                    ContentValues cv = new ContentValues();
                    JSONObject jobj = facilities.getJSONObject(i);
                    cv.put(DatabaseHelper.SELF_ID,jobj.getString("uid"));
                    cv.put(DatabaseHelper.NAME,jobj.getString("name"));
                    db.insertFacility(cv);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class DisSave extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray districts = jsonObject.getJSONArray("districts");
                for(int i=0; i<districts.length();i++){
                    JSONObject jObj = districts.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("district_name"));
                    db.insertDIS(contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class UpSave extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray upazilas = jsonObject.getJSONArray("upazilas");
                for(int i=0; i<upazilas.length();i++){
                    JSONObject jObj = upazilas.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getString("upazila_code"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("upazila_name"));
                    db.insertUP(contentValues);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public class LabSave extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray labs = jsonObject.getJSONArray("labs");
                for(int i=0; i<labs.length();i++){
                    JSONObject jObj = labs.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
//                                contentValues.put(DatabaseHelper.SELF_ID,jObj.getString("upazila_code"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    db.insertLab(contentValues);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
