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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check();
        init();
        loadData();
    }

    private void loadData(){
        Cursor c = db.getDIS();
        if(c.moveToFirst()) {
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://119.40.84.187/surveillance/public/js/compact.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        btn_toggle();
                        Log.d("DIS",response);
                        // Display the first 500 characters of the response string.
                        new InDB().execute(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                btn_toggle();
                Log.e("dis_load",error.getMessage());
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }

    private void check(){
        if(DataStore.id ==0 ){
            Toast.makeText(MainActivity.this,"Enter Valid login credentials",Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(MainActivity.this,ListActivity.class));
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
        see_db.setOnClickListener(listener);
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
            }
            if (uid.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Please fill user name",Toast.LENGTH_SHORT).show();
                return;
            }else if(pass.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this,"Please fill password",Toast.LENGTH_SHORT).show();
                return;
            }
            btn_toggle();
            check_login();
        }
    };

    private void check_login(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://119.40.84.187/surveillance/api/v1/login";

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
                                JSONObject login_user = jsonObject.getJSONObject("login_user");
                                DataStore.id = login_user.getInt("id");
                                DataStore.username = login_user.getJSONObject("employee").getString("name");
                                startActivity(new Intent(MainActivity.this, ListActivity.class));
                            }else{
                                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("username",uid.getText().toString());
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
                Log.d("DIS",String.valueOf(c.getInt(c.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+c.getString(c.getColumnIndex(DatabaseHelper.NAME)));
            }while (c.moveToNext());
        }
        c.close();
        Cursor up = db.getUP();
        Log.d("UPTotal",String.valueOf(up.getCount()));
        if(up.moveToFirst()){
            do {
                Log.d("UP",String.valueOf(up.getInt(up.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+up.getString(up.getColumnIndex(DatabaseHelper.NAME))+"-"+up.getString(up.getColumnIndex(DatabaseHelper.PARENT_ID)));
            }while (up.moveToNext());
        }
        up.close();
        Cursor mc = db.getMC();
        Log.d("MCTotal",String.valueOf(mc.getCount()));
        if(mc.moveToFirst()){
            do {
                Log.d("MC",String.valueOf(mc.getInt(mc.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+mc.getString(mc.getColumnIndex(DatabaseHelper.NAME))+"-"+mc.getString(mc.getColumnIndex(DatabaseHelper.PARENT_ID)));
            }while (mc.moveToNext());
        }
        mc.close();
        Cursor city = db.getCITY();
        Log.d("CITYTotal",String.valueOf(city.getCount()));
        if(city.moveToFirst()){
            do {
                Log.d("CITY",String.valueOf(city.getInt(city.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+city.getString(city.getColumnIndex(DatabaseHelper.NAME))+"-"+city.getString(city.getColumnIndex(DatabaseHelper.PARENT_ID)));
            }while (city.moveToNext());
        }
        city.close();
        Cursor thana = db.getTHANA();
        Log.d("THANATotal",String.valueOf(thana.getCount()));
        if(thana.moveToFirst()){
            do {
                Log.d("Thana",String.valueOf(thana.getInt(thana.getColumnIndex(DatabaseHelper.SELF_ID)))+"-"+thana.getString(thana.getColumnIndex(DatabaseHelper.NAME))+"-"+thana.getString(thana.getColumnIndex(DatabaseHelper.PARENT_ID)));
            }while (thana.moveToNext());
        }
    }

    public class InDB extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                JSONObject jsonObject = new JSONObject(strings[0]);
                JSONArray dis = jsonObject.getJSONArray("district");
                JSONArray up = jsonObject.getJSONArray("upazilla");
                JSONArray city = jsonObject.getJSONArray("city");
                JSONArray thana = jsonObject.getJSONArray("thana");
                JSONArray municipalty = jsonObject.getJSONArray("municipalty");

                for(int i=0; i<dis.length();i++){
                    JSONObject jObj = dis.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getInt("id"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    db.insertDIS(contentValues);
                }
                for(int i=0; i<up.length();i++){
                    JSONObject jObj = up.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getInt("id"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    contentValues.put(DatabaseHelper.PARENT_ID,jObj.getString("parent_id"));
                    db.insertUP(contentValues);
                }
                for(int i=0; i<city.length();i++){
                    JSONObject jObj = city.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getInt("id"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    contentValues.put(DatabaseHelper.PARENT_ID,jObj.getString("parent_id"));
                    db.insertCITY(contentValues);
                }
                for(int i=0; i<thana.length();i++){
                    JSONObject jObj = thana.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getInt("id"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    contentValues.put(DatabaseHelper.PARENT_ID,jObj.getString("parent_id"));
                    db.insertTHANA(contentValues);
                }
                for(int i=0; i<municipalty.length();i++){
                    JSONObject jObj = municipalty.getJSONObject(i);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DatabaseHelper.SELF_ID,jObj.getInt("id"));
                    contentValues.put(DatabaseHelper.NAME,jObj.getString("name"));
                    contentValues.put(DatabaseHelper.PARENT_ID,jObj.getString("parent_id"));
                    db.insertMC(contentValues);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
