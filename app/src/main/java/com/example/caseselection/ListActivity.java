package com.example.caseselection;

import android.database.Cursor;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.caseselection.fragments.CustomDialog;
import com.example.caseselection.fragments.SampleClassification;
import com.example.caseselection.interfaces.SearchInterface;
import com.example.caseselection.model.DataStore;
import com.example.caseselection.model.DatabaseHelper;
import com.example.caseselection.model.InfoStore;
import com.example.caseselection.model.ListDataStore;
import com.example.caseselection.model.PageControl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements SearchInterface {
    RecyclerView rec;
    RecAdapter recAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<ListDataStore> listDataStores;
    ProgressBar progressBar;
    DatabaseHelper db;
    String key,value;
    boolean loading = false;
//    Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoStore.clear();
                new SampleClassification(ListActivity.this).show();
            }
        });
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listDataStores.clear();
        loadData(1,false);
        ((Button) findViewById(R.id.refresh)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDataStores.clear();
                loadData(1,false);
            }
        });
        ((ImageView)findViewById(R.id.srch)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cdd = new CustomDialog(ListActivity.this,ListActivity.this);
//                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
            }
        });
    }

    private void init(){
        listDataStores = new ArrayList<>();
        rec = (RecyclerView) findViewById(R.id.rec);
        recAdapter = new RecAdapter(ListActivity.this,rec,ListActivity.this,listDataStores);
        rec.setAdapter(recAdapter);
        linearLayoutManager = new LinearLayoutManager(ListActivity.this);
        rec.setLayoutManager(linearLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.pBar);
        db = new DatabaseHelper(ListActivity.this);
        rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + 10) && PageControl.page_limit>PageControl.page_count) {
                    // End has been reached
                    // Do something
                    loadData(++PageControl.page_count,false);
                }
            }
        });
    }


    private void loadData(int count,boolean isSearch){
        btn_toggle();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://119.40.84.187/surveillance/api/v1/listcase?page="+String.valueOf(count)+"&u_id="+ DataStore.id;
        if (isSearch){
            url ="http://119.40.84.187/surveillance/api/v1/listcase?page="+String.valueOf(count)+"&"+key+"="+value+"&u_id="+ DataStore.id;
        }
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_toggle();
                        Log.d("login",response);
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            PageControl.page_count = jsonObject.getInt("current_page");
                            PageControl.page_limit = jsonObject.getInt("last_page");
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i=0; i<data.length();i++){
                                JSONObject jObj = data.getJSONObject(i);
                                ListDataStore dataStore = new ListDataStore();
                                if(jObj.getString("dis")==null || jObj.getString("dis").isEmpty()|| jObj.getString("dis").equals("null")){
                                    Log.d("DISError",jObj.getString("dis"));
                                    dataStore.dis = "";
                                }else{
                                    Cursor cdis = db.getDIS(jObj.getInt("dis"));
                                    if(cdis.moveToNext()){
                                        dataStore.dis = cdis.getString(cdis.getColumnIndex(DatabaseHelper.NAME));
                                        Log.d("DISget",cdis.getString(cdis.getColumnIndex(DatabaseHelper.NAME)));
                                    }else{
                                        Log.d("DISNFErr",String.valueOf(jObj.getInt("dis")));
                                        dataStore.dis = "";
                                    }
                                }
//Null check for Up /*
                                if(jObj.getString("up")==null || jObj.getString("up").isEmpty()|| jObj.getString("up").equals("null")){
                                    dataStore.up = "";
                                }else {
                                    Integer up = jObj.getInt("up");
                                    Cursor cup = db.getUP(up);
                                    if (cup.moveToNext()) {
                                        dataStore.up = cup.getString(cup.getColumnIndex(DatabaseHelper.NAME));
                                    } else {
                                        dataStore.up = "";
                                    }
                                }
                                //Null check for mc
                                if(jObj.getString("mc")==null || jObj.getString("mc").isEmpty()|| jObj.getString("mc").equals("null")){
                                    dataStore.mc = "";
                                }else {
                                    Integer mc = jObj.getInt("mc");
                                    Cursor cmc = db.getMC(mc);
                                    if (cmc.moveToNext()) {
                                        dataStore.mc = cmc.getString(cmc.getColumnIndex(DatabaseHelper.NAME));
                                    } else {
                                        dataStore.mc = "";
                                    }
                                }
                                //Null check for thana
                                if(jObj.getString("thana")==null || jObj.getString("thana").isEmpty()|| jObj.getString("thana").equals("null")){
                                    dataStore.thana = "";
                                }else {
                                    Integer thana = jObj.getInt("thana");
                                    Cursor cthana = db.getTHANA(thana);
                                    if (cthana.moveToNext()) {
                                        dataStore.thana = cthana.getString(cthana.getColumnIndex(DatabaseHelper.NAME));
                                    } else {
                                        dataStore.thana = "";
                                    }
                                }
                                //Null check for city
                                if(jObj.getString("citycorp")==null || jObj.getString("citycorp").isEmpty()|| jObj.getString("citycorp").equals("null")){
                                    dataStore.citycorp = "";
                                }else {
                                    Integer city = jObj.getInt("citycorp");
                                    Cursor ccity = db.getCITY(city);
                                    if (ccity.moveToNext()) {
                                        dataStore.citycorp = ccity.getString(ccity.getColumnIndex(DatabaseHelper.NAME));
                                    } else {
                                        dataStore.citycorp = "";
                                    }
                                }
                                dataStore.cls = jObj.getInt("cls");
                                dataStore.cls_id = jObj.getString("cls_id");
                                dataStore.id = jObj.getInt("id");
                                dataStore.name = jObj.getString("name");
                                dataStore.age = jObj.getString("age");
                                dataStore.ocu = jObj.getString("ocu");
                                dataStore.mob = jObj.getString("mob");
                                dataStore.add = jObj.getString("adds");
                                Integer sex = jObj.getInt("sex");
                                dataStore.sex = sex;
                                Integer age_type = jObj.getInt("age_type");
                                dataStore.age_type = age_type;
                                Integer confm_type = jObj.getInt("confm_type");
                                dataStore.confm_type = confm_type;
                                Integer emr_type = jObj.getInt("emr_type");
                                dataStore.emr_type = emr_type;
                                dataStore.lab_id = jObj.getString("lab_id");
                                dataStore.email = jObj.getString("email");
                                dataStore.coll_ads = jObj.getString("coll_ads");
                                dataStore.coll_from = jObj.getInt("coll_from");
                                dataStore.ref = jObj.getString("ref");
                                dataStore.ins_name = jObj.getString("ins_name");
                                dataStore.ins_dept =                 jObj.getString("ins_dept");
                                dataStore.ins_unit =                 jObj.getString("ins_unit");
                                dataStore.ins_unit_head =                 jObj.getString("ins_unit_head");
                                dataStore.ins_ward =                 jObj.getString("ins_ward");
                                dataStore.ins_cabin =                 jObj.getString("ins_cabin");
                                dataStore.fev =                 jObj.getInt("fev");
                                dataStore.headache =                 jObj.getInt("headache");
                                dataStore.cough =                 jObj.getInt("cough");
                                dataStore.breath =                 jObj.getInt("breath");
                                dataStore.onset =                 jObj.getString("onset");
                                dataStore.txt_oth_symp =                 jObj.getString("txt_oth_symp");
                                dataStore.cr =                 jObj.getInt("cr");
                                dataStore.th =                 jObj.getInt("th");
                                dataStore.th_coun =                 jObj.getString("th_coun");
//                                Date Conversion
                                String dtStart = jObj.getString("th_date");
                                Log.d("getDateValue:",dtStart);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = format.parse(dtStart);
//                                    System.out.println(date);
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    dataStore.day = cal.get(Calendar.DAY_OF_MONTH);
                                    dataStore.mth = cal.get(Calendar.MONTH);
                                    dataStore.yr = cal.get(Calendar.YEAR);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String dt = jObj.getString("spec_c_date");
                                Log.d("getDateValue:",dt);
                                SimpleDateFormat spec_format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = spec_format.parse(dt);
//                                    System.out.println(date);
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    dataStore.spec_d = cal.get(Calendar.DAY_OF_MONTH);
                                    dataStore.spec_m = cal.get(Calendar.MONTH);
                                    dataStore.spec_yr = cal.get(Calendar.YEAR);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dataStore.cc =                 jObj.getInt("cc");
                                dataStore.hc =                 jObj.getInt("hc");
                                dataStore.copd =                 jObj.getInt("copd");
                                dataStore.asthma =                 jObj.getInt("asthma");
                                dataStore.ild =                 jObj.getInt("ild");
                                dataStore.dm =                 jObj.getInt("dm");
                                dataStore.ihd =                 jObj.getInt("ihd");
                                dataStore.htn =                 jObj.getInt("htn");
                                dataStore.ckd =                 jObj.getInt("ckd");
                                dataStore.cld =                 jObj.getInt("cld");
                                dataStore.md =                 jObj.getInt("md");
                                dataStore.ost =                 jObj.getInt("ost");
                                dataStore.preg =                 jObj.getInt("preg");
                                dataStore.crf_oth =                 jObj.getString("crf_oth");
                                dataStore.spec =                 jObj.getInt("spec");
                                dataStore.nasal =                 jObj.getInt("nasal");
                                dataStore.throat_swab =                 jObj.getInt("throat_swab");
                                dataStore.sputum =                 jObj.getInt("sputum");
                                dataStore.tracheal_aspirate =                 jObj.getInt("tracheal_aspirate");
                                dataStore.serum =                 jObj.getInt("serum");
                                dataStore.spec_oth_txt =                 jObj.getString("spec_oth_txt");
                                dataStore.remarks =                 jObj.getString("remarks");
                                dataStore.in_cond_by = jObj.getString("in_cond_by");
                                dataStore.bg = jObj.getString("bg");
                                dataStore.nid = jObj.getString("nid");
                                if(jObj.getString("paid")==null || jObj.getString("paid").isEmpty()||
                                        jObj.getString("paid").equals("null")) {
                                    dataStore.paid =1;
                                }else
                                    dataStore.paid = jObj.getInt("paid");
                                dataStore.receipt_no = jObj.getString("receipt_no");
                                if(jObj.getString("reason")==null || jObj.getString("reason").isEmpty()||
                                        jObj.getString("reason").equals("null")) {
                                    dataStore.reason =3;
                                }else
                                    dataStore.reason = jObj.getInt("reason");
                                if(jObj.getString("rel")==null || jObj.getString("rel").isEmpty()||
                                        jObj.getString("rel").equals("null")) {
                                    dataStore.rel =0;
                                }else
                                    dataStore.rel = jObj.getInt("rel");
                                dataStore.dept_name = jObj.getString("dept_name");
                                dataStore.service_code = jObj.getString("service_code");
                                dataStore.freedom_id = jObj.getString("freedom_id");
                                dataStore.free_oth_details = jObj.getString("free_oth_details");

                                if(jObj.getString("f_dose")==null ||jObj.getString("f_dose")=="null" || jObj.getString("f_dose").isEmpty()){
                                    dataStore.f_dose = 0;
                                }else{
                                    dataStore.f_dose = Integer.parseInt(jObj.getString("f_dose"));
                                }
                                if(jObj.getString("s_dose")==null || jObj.getString("s_dose")=="null" || jObj.getString("s_dose").isEmpty()){
                                    dataStore.s_dose = 0;
                                }else {
                                    dataStore.s_dose = jObj.getInt("s_dose");
                                }
                                if(jObj.getString("no_dose")==null || jObj.getString("no_dose")=="null" || jObj.getString("no_dose").isEmpty()){
                                    dataStore.no_dose = 0;
                                }else {
                                    dataStore.no_dose = jObj.getInt("no_dose");
                                }
                                if(jObj.getString("yes_inf")==null ||jObj.getString("yes_inf")=="null" || jObj.getString("yes_inf").isEmpty()){
                                    dataStore.yes_inf = 0;
                                }else {
                                    dataStore.yes_inf = jObj.getInt("yes_inf");
                                }
                                if(jObj.getString("no_inf")==null || jObj.getString("no_inf")=="null" || jObj.getString("no_inf").isEmpty()){
                                    dataStore.no_inf = 0;
                                }else {
                                    dataStore.no_inf = jObj.getInt("no_inf");
                                }
                                if(jObj.getString("un_inf")==null || jObj.getString("un_inf")=="null" || jObj.getString("un_inf").isEmpty()){
                                    dataStore.un_inf = 0;
                                }else {
                                    dataStore.un_inf = jObj.getInt("un_inf");
                                }
                                dataStore.f_dose_date = jObj.getString("f_dose_date");
                                dataStore.s_dose_date = jObj.getString("s_dose_date");
                                dataStore.rt_pcr_my = jObj.getString("rt_pcr_my");
                                listDataStores.add(dataStore);
                            }
                            recAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ListError",e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btn_toggle();
                Toast.makeText(ListActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    private void btn_toggle(){
        if(progressBar.getVisibility()!=View.VISIBLE){
            loading = true;
            progressBar.setVisibility(View.VISIBLE);
        }else{
            loading = false;
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void search(String key, String value) {
//        Log.d("SearchInList",key+" value "+value);
        if (key.isEmpty() || value.isEmpty()){
            return;
        }
        Log.d("Search",key+"-"+value);
        this.key = key;
        this.value = value;
        listDataStores.clear();
        loadData(1,true);
    }
}
