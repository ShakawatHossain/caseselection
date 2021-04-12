package com.example.caseselection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.CompoundButton;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InfoActivity extends AppCompatActivity implements CalenderInterface {
    TextView info_head, onset, s_dose_date,f_dose_date;
    AutoCompleteTextView dis, up, city, mc, thana;
    TextInputLayout coll_ads_txt;
    EditText name, age, mob, add, lab_id, email, coll_ads, ref, ins_name, ins_dept, ins_unit, ins_unit_head,
            ins_ward, ins_cabin, txt_oth_symp, th_coun, crf_oth, spec_oth_txt, remarks, in_cond_by, cls_id,
            ocu, nid, dept_name, service_code, receipt_no, freedom_id, free_oth_details,rt_pcr_my;
    RadioGroup sex, age_type, confm_type, emr_type, coll_from, fev, headache, cough, breath, cr, th, cc, hc, spec,
            bg, payment, frees, rel;
    DatePicker th_date, spec_c_date;
    CheckBox copd, asthma, ild, dm, ihd, htn, ckd, cld, md, ost, prg, nasal, throat_swab, sputum, tracheal_aspirate, serum,
            f_dose,s_dose,no_dose,yes_inf,no_inf,un_inf;
    double lat, lng;
    Button submit;
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
        Log.d("start","Init Called");
        db = new DatabaseHelper(InfoActivity.this);
        info_head = (TextView) findViewById(R.id.info_head);
        info_head.setText("Case Information ("+ DataStore.username+" )");
        dis = (AutoCompleteTextView) findViewById(R.id.dis);
        dis.setText(InfoStore.dis);
        dis.setAdapter(getAdapter(db.getDIS()));
        up = (AutoCompleteTextView) findViewById(R.id.up);
        up.setText(InfoStore.up);
        up.setAdapter(getAdapter(db.getUP()));
        city = (AutoCompleteTextView) findViewById(R.id.city);
        city.setText(InfoStore.citycorp);
        city.setAdapter(getAdapter(db.getCITY()));
        mc = (AutoCompleteTextView) findViewById(R.id.mc);
        mc.setText(InfoStore.mc);
        mc.setAdapter(getAdapter(db.getMC()));
        thana = (AutoCompleteTextView) findViewById(R.id.thana);
        thana.setText(InfoStore.thana);
        thana.setAdapter(getAdapter(db.getTHANA()));
        name = (EditText) findViewById(R.id.name);
        Log.d("name from",InfoStore.name);
        name.setText(InfoStore.name);
        age = (EditText) findViewById(R.id.age);
        age.setText(InfoStore.age);
        ocu = (EditText) findViewById(R.id.ocu);
        ocu.setText(InfoStore.ocu);
        mob = (EditText) findViewById(R.id.mob);
        mob.setText(InfoStore.mob);
        add = (EditText) findViewById(R.id.add);
        add.setText(InfoStore.add);
        sex = (RadioGroup) findViewById(R.id.sex);
        sex.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.sex==0) sex.check(R.id.male); else sex.check(R.id.female);
        age_type = (RadioGroup) findViewById(R.id.age_type);
        age_type.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.age_type==0) age_type.check(R.id.year); else age_type.check(R.id.month);
        confm_type = (RadioGroup) findViewById(R.id.confm_type);
        confm_type.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.confm_type==0) confm_type.check(R.id.confm); else confm_type.check(R.id.suggest);
        emr_type = (RadioGroup) findViewById(R.id.emr_type);
        emr_type.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.emr_type==0) emr_type.check(R.id.norm); else emr_type.check(R.id.emr);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this.listener);
        pBar = (ProgressBar) findViewById(R.id.pBar);
        coll_ads_txt = (TextInputLayout) findViewById(R.id.coll_ads_txt);
        cls_id = (EditText) findViewById(R.id.cls_id);
        if(InfoStore.cls_id.isEmpty() || InfoStore.cls_id=="null"){
            cls_id.setVisibility(GONE);
        }else {
            cls_id.setText(InfoStore.cls_id);
        }
        lab_id = (EditText) findViewById(R.id.lab_id);
        lab_id.setText(InfoStore.lab_id);
        email = (EditText) findViewById(R.id.email);
        email.setText(InfoStore.email);
        coll_from = (RadioGroup) findViewById(R.id.coll_from);
        if (InfoStore.coll_from==1) {coll_from.check(R.id.other); coll_ads_txt.setVisibility(View.VISIBLE);}
        coll_from.setOnCheckedChangeListener(checkedChangeListener);
        coll_ads = (EditText) findViewById(R.id.coll_ads);
        coll_ads.setText(InfoStore.coll_ads);
        ref = (EditText) findViewById(R.id.ref);
        ref.setText(InfoStore.ref);
        ins_name = (EditText) findViewById(R.id.ins_name);
        ins_name.setText(InfoStore.ins_name);
        ins_dept = (EditText) findViewById(R.id.ins_dept);
        ins_dept.setText(InfoStore.ins_dept);
        ins_unit = (EditText) findViewById(R.id.ins_unit);
        ins_unit.setText(InfoStore.ins_unit);
        ins_unit_head = (EditText) findViewById(R.id.ins_unit_head);
        ins_unit_head.setText(InfoStore.ins_unit_head);
        ins_ward = (EditText) findViewById(R.id.ins_ward);
        ins_ward.setText(InfoStore.ins_ward);
        ins_cabin = (EditText) findViewById(R.id.ins_cabin);
        ins_cabin.setText(InfoStore.ins_cabin);
//      suspect criteria
        fev = (RadioGroup) findViewById(R.id.fev);
        if(InfoStore.fev==1){ fev.check(R.id.fev_yes); }else{ fev.check(R.id.fev_no);}
        fev.setOnCheckedChangeListener(checkedChangeListener);
        headache = (RadioGroup) findViewById(R.id.headache);
        if(InfoStore.headache==1) headache.check(R.id.headache_yes); else headache.check(R.id.headache_no);
        headache.setOnCheckedChangeListener(checkedChangeListener);
        cough = (RadioGroup) findViewById(R.id.cough);
        if(InfoStore.cough==1) cough.check(R.id.cough_yes); else cough.check(R.id.cough_no);
        cough.setOnCheckedChangeListener(checkedChangeListener);
        breath = (RadioGroup) findViewById(R.id.breath);
        if(InfoStore.breath == 1) breath.check(R.id.breath_yes); else breath.check(R.id.breath_no);
        breath.setOnCheckedChangeListener(checkedChangeListener);
        onset = (TextView) findViewById(R.id.onset);
        onset.setText(InfoStore.onset);
        onset.setOnClickListener(listener);
        txt_oth_symp = (EditText) findViewById(R.id.txt_oth_symp);
        txt_oth_symp.setText(InfoStore.txt_oth_symp);
        cr = (RadioGroup) findViewById(R.id.cr);
        cr.setOnCheckedChangeListener(checkedChangeListener);
        if (InfoStore.cr==0) cr.check(R.id.cr_no); else if(InfoStore.cr==1) cr.check(R.id.cr_yes); else cr.check(R.id.cr_un);
        th = (RadioGroup) findViewById(R.id.th);
        th.setOnCheckedChangeListener(checkedChangeListener);
        th_coun = (EditText) findViewById(R.id.th_coun);
        th_coun.setText(InfoStore.th_coun);
        spec_c_date = (DatePicker) findViewById(R.id.spec_c_date);
        spec_c_date.init(InfoStore.spec_yr,InfoStore.spec_m,InfoStore.spec_d,null);
        th_date = (DatePicker) findViewById(R.id.th_date);
        Log.d("SetDate",String.valueOf(InfoStore.yr)+"-"+String.valueOf(InfoStore.mth)+"-"+String.valueOf(InfoStore.day));
        th_date.init(InfoStore.yr,InfoStore.mth,InfoStore.day,null);
        if (InfoStore.th==0) {
            th.check(R.id.th_no);
            if (((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==View.VISIBLE)
                ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(GONE);
            if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==View.VISIBLE)
                ((TableRow)findViewById(R.id.txt_th_date)).setVisibility(GONE);
        } else if(InfoStore.th==1) {
            th.check(R.id.th_yes);
            if(((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==GONE)
                ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(View.VISIBLE);
            if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==GONE) {
                ((TableRow) findViewById(R.id.txt_th_date)).setVisibility(View.VISIBLE);
                th_date.init(InfoStore.yr, InfoStore.mth, InfoStore.day, null);
            }
        } else {
            th.check(R.id.th_un);
            if (((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==View.VISIBLE)
                ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(GONE);
            if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==View.VISIBLE)
                ((TableRow)findViewById(R.id.txt_th_date)).setVisibility(GONE);
        }
        cc = (RadioGroup) findViewById(R.id.cc);
        cc.setOnCheckedChangeListener(checkedChangeListener);
        if (InfoStore.cc==0) cc.check(R.id.cc_no); else if(InfoStore.cc==1) cc.check(R.id.cc_yes); else cc.check(R.id.cc_un);
        hc = (RadioGroup) findViewById(R.id.hc);
        hc.setOnCheckedChangeListener(checkedChangeListener);
        if (InfoStore.hc==0) hc.check(R.id.hc_no); else if(InfoStore.hc==1) hc.check(R.id.hc_yes); else hc.check(R.id.hc_un);
        copd = (CheckBox) findViewById(R.id.copd);
        if(InfoStore.copd==1) copd.setChecked(true);
        asthma = (CheckBox) findViewById(R.id.asthma);
        if(InfoStore.asthma==1) asthma.setChecked(true);
        ild = (CheckBox) findViewById(R.id.ild);
        if(InfoStore.ild==1) ild.setChecked(true);
        dm = (CheckBox) findViewById(R.id.dm);
        if(InfoStore.dm==1) dm.setChecked(true);
        ihd = (CheckBox) findViewById(R.id.ihd);
        if(InfoStore.ihd==1) ihd.setChecked(true);
        htn = (CheckBox) findViewById(R.id.htn);
        if(InfoStore.htn==1) htn.setChecked(true);
        ckd = (CheckBox) findViewById(R.id.ckd);
        if(InfoStore.ckd==1) ckd.setChecked(true);
        cld = (CheckBox) findViewById(R.id.cld);
        if(InfoStore.cld==1) cld.setChecked(true);
        md = (CheckBox) findViewById(R.id.md);
        if(InfoStore.md==1) md.setChecked(true);
        ost = (CheckBox) findViewById(R.id.ost);
        if(InfoStore.ost==1) ost.setChecked(true);
        prg = (CheckBox) findViewById(R.id.prg);
        if(InfoStore.prg==1) prg.setChecked(true);
        crf_oth = (EditText) findViewById(R.id.crf_oth);
        crf_oth.setText(InfoStore.crf_oth);
        spec = (RadioGroup) findViewById(R.id.spec);
        spec.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.spec==1){
            spec.check(R.id.spec_c);
            if(((TableRow)findViewById(R.id.spec_oth_txt_ip)).getVisibility()!=VISIBLE){
                ((TableRow)findViewById(R.id.spec_oth_txt_ip)).setVisibility(VISIBLE);
            }
            if(((TableRow)findViewById(R.id.spec_list)).getVisibility()!=VISIBLE) {
                ((TableRow) findViewById(R.id.spec_list)).setVisibility(VISIBLE);
            }
        }else{
            spec.check(R.id.spec_nc);
            if(((TableRow)findViewById(R.id.spec_oth_txt_ip)).getVisibility()==VISIBLE){
                ((TableRow)findViewById(R.id.spec_oth_txt_ip)).setVisibility(GONE);
            }
            if(((TableRow)findViewById(R.id.spec_list)).getVisibility()==VISIBLE) {
                ((TableRow) findViewById(R.id.spec_list)).setVisibility(GONE);
            }
        }
        nasal = (CheckBox) findViewById(R.id.nasal);
        if(InfoStore.nasal == 1) nasal.setChecked(true);
        throat_swab = (CheckBox) findViewById(R.id.throat_swab);
        if(InfoStore.throat_swab == 1) throat_swab.setChecked(true);
        sputum = (CheckBox) findViewById(R.id.sputum);
        if(InfoStore.sputum == 1) sputum.setChecked(true);
        tracheal_aspirate = (CheckBox) findViewById(R.id.tracheal_aspirate);
        if(InfoStore.tracheal_aspirate == 1) tracheal_aspirate.setChecked(true);
        serum = (CheckBox) findViewById(R.id.serum);
        if(InfoStore.serum == 1) serum.setChecked(true);
        spec_oth_txt = (EditText) findViewById(R.id.spec_oth_txt);
        spec_oth_txt.setText(InfoStore.spec_oth_txt);
        remarks = (EditText) findViewById(R.id.remarks);
        remarks.setText(InfoStore.remarks);
        in_cond_by = (EditText) findViewById(R.id.in_cond_by);
        in_cond_by.setText(InfoStore.in_cond_by);

        nid = (EditText) findViewById(R.id.nid);
        nid.setText(InfoStore.nid);
        dept_name = (EditText) findViewById(R.id.dept_name);
        dept_name.setText(InfoStore.dept_name);
        service_code = (EditText) findViewById(R.id.service_code);
        service_code.setText(InfoStore.service_code);
        receipt_no = (EditText) findViewById(R.id.receipt_no);
        receipt_no.setText(InfoStore.receipt_no);
        freedom_id = (EditText) findViewById(R.id.freedom_id);
        freedom_id.setText(InfoStore.freedom_id);
        bg = (RadioGroup) findViewById(R.id.bg);
        bg.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.bg.compareTo("ap")==0) bg.check(R.id.ap);
        else if(InfoStore.bg.compareTo("an")==0) bg.check(R.id.an);
        else if(InfoStore.bg.compareTo("bp")==0) bg.check(R.id.bp);
        else if(InfoStore.bg.compareTo("bn")==0) bg.check(R.id.bn);
        else if(InfoStore.bg.compareTo("op")==0) bg.check(R.id.op);
        else if(InfoStore.bg.compareTo("on")==0) bg.check(R.id.on);
        else if(InfoStore.bg.compareTo("abp")==0) bg.check(R.id.abp);
        else if(InfoStore.bg.compareTo("abn")==0) bg.check(R.id.abn);
        else if(InfoStore.bg.compareTo("unknown")==0) bg.check(R.id.unknown);
        free_oth_details = (EditText) findViewById(R.id.free_oth_details);
        payment = (RadioGroup) findViewById(R.id.payment);
        payment.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.paid==0){
            payment.check(R.id.paid);
            if(((TableRow) findViewById(R.id.paid_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.paid_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.free_reason)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_reason)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
            }
        }
        else if(InfoStore.paid==1) {
            payment.check(R.id.free);
            if(((TableRow) findViewById(R.id.paid_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.paid_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_reason)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.free_reason)).setVisibility(VISIBLE);
            }

        }
        frees = (RadioGroup) findViewById(R.id.frees);
        frees.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.reason==0 && InfoStore.paid==1){
            frees.check(R.id.govt);
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE) {
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
            }
        }
        else if(InfoStore.reason==1 && InfoStore.paid==1) {
            frees.check(R.id.freedom);
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
            }
        }
        else if(InfoStore.reason==2 && InfoStore.paid==1) {
            frees.check(R.id.destitute);
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
            }
        }
        else if(InfoStore.reason==3 && InfoStore.paid==1) {
            frees.check(R.id.study);
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
            }
        }else if(InfoStore.reason==4 && InfoStore.paid==1) {
            frees.check(R.id.free_oth);
            if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
            }
            if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
            }
            if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()!= VISIBLE){
                ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(VISIBLE);
            }
        }
        rel = (RadioGroup) findViewById(R.id.rel);
        rel.setOnCheckedChangeListener(checkedChangeListener);
        if(InfoStore.rel == 0 && InfoStore.paid==1)    rel.check(R.id.self);
        else if (InfoStore.rel == 1 && InfoStore.paid==1) rel.check(R.id.spouse);
        else if (InfoStore.rel == 2 && InfoStore.paid==1) rel.check(R.id.father);
        else if (InfoStore.rel == 3 && InfoStore.paid==1) rel.check(R.id.mother);
        else if (InfoStore.rel == 4 && InfoStore.paid==1) rel.check(R.id.son);
        else if (InfoStore.rel == 5 && InfoStore.paid==1) rel.check(R.id.daughter);
        else if (InfoStore.rel == 6 && InfoStore.paid==1) rel.check(R.id.brother);
        else if (InfoStore.rel == 7 && InfoStore.paid==1) rel.check(R.id.sister);

        s_dose_date = (TextView) findViewById(R.id.s_dose_date);
        s_dose_date.setOnClickListener(listener);
        s_dose_date.setText(InfoStore.s_dose_date);
        f_dose_date = (TextView) findViewById(R.id.f_dose_date);
        f_dose_date.setOnClickListener(listener);
        f_dose_date.setText(InfoStore.f_dose_date);
        rt_pcr_my = (EditText) findViewById(R.id.rt_pcr_my);
        rt_pcr_my.setText(InfoStore.rt_pcr_my);

        f_dose = (CheckBox) findViewById(R.id.f_dose);
        if (InfoStore.f_dose==1) {
            f_dose.setChecked(true);
            if(((TableRow) findViewById(R.id.f_dose_date_row)).getVisibility()!=VISIBLE){
                ((TableRow) findViewById(R.id.f_dose_date_row)).setVisibility(VISIBLE);
            }
        }else{
            if(((TableRow) findViewById(R.id.f_dose_date_row)).getVisibility()==VISIBLE){
                ((TableRow) findViewById(R.id.f_dose_date_row)).setVisibility(GONE);
            }
        }
        f_dose.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        s_dose = (CheckBox) findViewById(R.id.s_dose);
        if (InfoStore.s_dose==1) {
            s_dose.setChecked(true);
            if(((TableRow) findViewById(R.id.s_dose_date_row)).getVisibility()!=VISIBLE){
                ((TableRow) findViewById(R.id.s_dose_date_row)).setVisibility(VISIBLE);
            }
        }else{
            if(((TableRow) findViewById(R.id.s_dose_date_row)).getVisibility()==VISIBLE){
                ((TableRow) findViewById(R.id.s_dose_date_row)).setVisibility(GONE);
            }
        }
        s_dose.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        no_dose = (CheckBox) findViewById(R.id.no_dose);
        if (InfoStore.no_dose==1) no_dose.setChecked(true);
        yes_inf = (CheckBox) findViewById(R.id.yes_inf);
        if(InfoStore.yes_inf==1) {
            yes_inf.setChecked(true);
            if(((TableRow) findViewById(R.id.rt_pcr_my_row)).getVisibility()!=VISIBLE){
                ((TableRow) findViewById(R.id.rt_pcr_my_row)).setVisibility(VISIBLE);
            }
        }else{
            if(((TableRow) findViewById(R.id.rt_pcr_my_row)).getVisibility()==VISIBLE){
                ((TableRow) findViewById(R.id.rt_pcr_my_row)).setVisibility(GONE);
            }
        }
        yes_inf.setOnCheckedChangeListener(checkboxCheckedChangeListener);
        no_inf = (CheckBox) findViewById(R.id.no_inf);
        if(InfoStore.no_inf==1) no_inf.setChecked(true);
        un_inf = (CheckBox) findViewById(R.id.un_inf);
        if(InfoStore.un_inf==1) un_inf.setChecked(true);
    }
    private ArrayAdapter<String> getAdapter(Cursor c){
        ArrayList<String> str_arr = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                str_arr.add(c.getString(c.getColumnIndex("name")));
            }while (c.moveToNext());
        }
        return new ArrayAdapter<String>(InfoActivity.this,android.R.layout.simple_dropdown_item_1line,str_arr);
    }
    private CompoundButton.OnCheckedChangeListener checkboxCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.getId() == s_dose.getId()){
                if (isChecked){
                    if(((TableRow) findViewById(R.id.s_dose_date_row)).getVisibility()!=VISIBLE){
                        ((TableRow) findViewById(R.id.s_dose_date_row)).setVisibility(VISIBLE);
                    }
                }else{
                    if(((TableRow) findViewById(R.id.s_dose_date_row)).getVisibility()==VISIBLE){
                        ((TableRow) findViewById(R.id.s_dose_date_row)).setVisibility(GONE);
                    }
                }
            }
            else if (buttonView.getId() == f_dose.getId()){
                if (isChecked){
                    if(((TableRow) findViewById(R.id.f_dose_date_row)).getVisibility()!=VISIBLE){
                        ((TableRow) findViewById(R.id.f_dose_date_row)).setVisibility(VISIBLE);
                    }
                }else{
                    if(((TableRow) findViewById(R.id.f_dose_date_row)).getVisibility()==VISIBLE){
                        ((TableRow) findViewById(R.id.f_dose_date_row)).setVisibility(GONE);
                    }
                }
            }
            else if (buttonView.getId() == yes_inf.getId()){
                if (isChecked){
                    if(((TableRow) findViewById(R.id.rt_pcr_my_row)).getVisibility()!=VISIBLE){
                        ((TableRow) findViewById(R.id.rt_pcr_my_row)).setVisibility(VISIBLE);
                    }
                }else{
                    if(((TableRow) findViewById(R.id.rt_pcr_my_row)).getVisibility()==VISIBLE){
                        ((TableRow) findViewById(R.id.rt_pcr_my_row)).setVisibility(GONE);
                    }
                }
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(group.getId()==R.id.coll_from){
                if (checkedId == R.id.home){
                    InfoStore.coll_from = 0;
                    if(((TextInputLayout) findViewById(R.id.coll_ads_txt)).getVisibility()==View.VISIBLE) {
                        ((TextInputLayout) findViewById(R.id.coll_ads_txt)).setVisibility(GONE);
                        coll_ads.setText("");
                    }
                }else if(checkedId == R.id.other){
                    InfoStore.coll_from = 1;
                    if(((TextInputLayout) findViewById(R.id.coll_ads_txt)).getVisibility()!=View.VISIBLE)
                        ((TextInputLayout) findViewById(R.id.coll_ads_txt)).setVisibility(View.VISIBLE);
                }
            }else if(group.getId()==R.id.sex){
                if(checkedId == R.id.male)
                    InfoStore.sex = 0;
                else
                    InfoStore.sex = 1;
            }else if(group.getId()==R.id.age_type){
                if(checkedId == R.id.year)
                    InfoStore.age_type = 0;
                else
                    InfoStore.age_type = 1;
            }else if(group.getId()==R.id.confm_type){
                if(checkedId == R.id.confm)
                    InfoStore.confm_type = 0;
                else
                    InfoStore.confm_type = 1;
            }else if(group.getId()==R.id.emr_type){
                if(checkedId == R.id.norm)
                    InfoStore.emr_type = 0;
                else
                    InfoStore.emr_type = 1;
            }else if(group.getId()==R.id.fev) {
                if (checkedId == R.id.fev_yes){
                    InfoStore.fev = 1;
                }else{
                    InfoStore.fev = 0;
                }
            }else if(group.getId()==R.id.headache){
                if(checkedId == R.id.headache_yes){
                    InfoStore.headache = 1;}
                else{
                    InfoStore.headache = 0;}
            }else if(group.getId() == R.id.cough){
                if(checkedId == R.id.cough_yes){
                    InfoStore.cough = 1;}
                else{
                    InfoStore.cough = 0;}
            }else if(group.getId()==R.id.breath){
                if (checkedId == R.id.breath_yes){
                    InfoStore.breath = 1;
                }else
                    InfoStore.breath = 0;
            }else if(group.getId() == R.id.cr){
                if(checkedId == R.id.cr_yes){
                    InfoStore.cr = 1;
                }else if(checkedId == R.id.cr_no){
                    InfoStore.cr = 0;
                }else {
                    InfoStore.cr = 2;
                }
            }else if(group.getId() == R.id.th){
                if(checkedId == R.id.th_yes){
                    InfoStore.th = 1;
                    if(((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==GONE)
                        ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(View.VISIBLE);
                    if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==GONE) {
                        ((TableRow) findViewById(R.id.txt_th_date)).setVisibility(View.VISIBLE);
                        th_date.init(InfoStore.yr, InfoStore.mth, InfoStore.day, null);
                    }
                }else if(checkedId == R.id.th_no){
                    InfoStore.th = 0;
                    if (((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==View.VISIBLE)
                        ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(GONE);
                    if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==View.VISIBLE)
                        ((TableRow)findViewById(R.id.txt_th_date)).setVisibility(GONE);
                }else {
                    InfoStore.th = 2;
                    if (((TableRow)findViewById(R.id.txt_th_coun)).getVisibility()==View.VISIBLE)
                        ((TableRow)findViewById(R.id.txt_th_coun)).setVisibility(GONE);
                    if(((TableRow)findViewById(R.id.txt_th_date)).getVisibility()==View.VISIBLE)
                        ((TableRow)findViewById(R.id.txt_th_date)).setVisibility(GONE);
                }
            }else if(group.getId() == R.id.cc){
                if(checkedId == R.id.cc_yes){
                    InfoStore.cc = 1;
                }else if(checkedId == R.id.cc_no){
                    InfoStore.cc = 0;
                }else {
                    InfoStore.cc = 2;
                }
            }else if(group.getId() == R.id.hc){
                if(checkedId == R.id.hc_yes){
                    InfoStore.hc = 1;
                }else if(checkedId == R.id.hc_no){
                    InfoStore.hc = 0;
                }else {
                    InfoStore.hc = 2;
                }
            }else if(group.getId() == R.id.spec) {
                if (checkedId == R.id.spec_c) {
                    InfoStore.spec = 1;
                    if(((TableRow)findViewById(R.id.spec_oth_txt_ip)).getVisibility()!=VISIBLE){
                        ((TableRow)findViewById(R.id.spec_oth_txt_ip)).setVisibility(VISIBLE);
                    }
                    if(((TableRow)findViewById(R.id.spec_list)).getVisibility()!=VISIBLE) {
                        ((TableRow) findViewById(R.id.spec_list)).setVisibility(VISIBLE);
                    }
                } else if (checkedId == R.id.spec_nc) {
                    InfoStore.spec = 0;
                    if(((TableRow)findViewById(R.id.spec_oth_txt_ip)).getVisibility()==VISIBLE){
                        ((TableRow)findViewById(R.id.spec_oth_txt_ip)).setVisibility(GONE);
                    }
                    if(((TableRow)findViewById(R.id.spec_list)).getVisibility()==VISIBLE) {
                        ((TableRow) findViewById(R.id.spec_list)).setVisibility(GONE);
                    }
                }
            }else if(group.getId()== bg.getId()){
                if(checkedId == R.id.ap)
                    InfoStore.bg = "ap";
                else if(checkedId == R.id.an)
                    InfoStore.bg = "an";
                else if(checkedId == R.id.bp)
                    InfoStore.bg = "bp";
                else if(checkedId == R.id.bn)
                    InfoStore.bg = "bn";
                else if(checkedId == R.id.abp)
                    InfoStore.bg = "abp";
                else if(checkedId == R.id.abn)
                    InfoStore.bg = "abn";
                else if(checkedId == R.id.op)
                    InfoStore.bg = "op";
                else if(checkedId == R.id.on)
                    InfoStore.bg = "on";
                else if(checkedId == R.id.unknown)
                    InfoStore.bg = "unknown";
            }else if(group.getId() == R.id.payment){
                if(checkedId == R.id.paid){
                    InfoStore.paid=0;
                    if(((TableRow) findViewById(R.id.paid_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.paid_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.free_reason)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_reason)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
                    }
                }else if(checkedId == R.id.free) {
                    InfoStore.paid=1;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.free_reason)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_reason)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.paid_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.paid_row)).setVisibility(GONE);
                    }
                }
            }else if(group.getId() == R.id.frees){
                if(checkedId == R.id.govt){
                    InfoStore.reason=0;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE) {
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
                    }
                }
                else if(checkedId == R.id.freedom) {
                    InfoStore.reason=1;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
                    }
                }else if(checkedId == R.id.destitute) {
                    InfoStore.reason=2;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
                    }
                }else if(checkedId == R.id.study) {
                    InfoStore.reason=3;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(GONE);
                    }
                }else if(checkedId == R.id.free_oth) {
                    InfoStore.reason=4;
                    if(((TableRow) findViewById(R.id.free_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_row)).setVisibility(VISIBLE);
                    }
                    if(((TableRow) findViewById(R.id.freedom_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.freedom_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.govt_row)).getVisibility()== VISIBLE){
                        ((TableRow) findViewById(R.id.govt_row)).setVisibility(GONE);
                    }
                    if(((TableRow) findViewById(R.id.free_oth_row)).getVisibility()!= VISIBLE){
                        ((TableRow) findViewById(R.id.free_oth_row)).setVisibility(VISIBLE);
                    }
                }
            }else if(group.getId() == R.id.rel){
                if (checkedId == R.id.self) InfoStore.rel=0;
                if (checkedId == R.id.spouse) InfoStore.rel=1;
                if (checkedId == R.id.father) InfoStore.rel=2;
                if (checkedId == R.id.mother) InfoStore.rel=3;
                if (checkedId == R.id.son) InfoStore.rel=4;
                if (checkedId == R.id.daughter) InfoStore.rel=5;
                if (checkedId == R.id.brother) InfoStore.rel=6;
                if (checkedId == R.id.sister) InfoStore.rel=7;
            }
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.onset){
                new CalenderDialog(InfoActivity.this,InfoActivity.this,
                        InfoStore.onset,"Date of Onset",onset).show();
                return;
            }
            if (v.getId() == R.id.s_dose_date){
                new CalenderDialog(InfoActivity.this,InfoActivity.this,
                        InfoStore.onset,"Date of Onset",s_dose_date).show();
                return;
            }
            if (v.getId() == R.id.f_dose_date){
                new CalenderDialog(InfoActivity.this,InfoActivity.this,
                        InfoStore.onset,"Date of Onset",f_dose_date).show();
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
        InfoStore.dis = dis.getText().toString();
        InfoStore.up = up.getText().toString();
        InfoStore.citycorp = city.getText().toString();
        InfoStore.mc = mc.getText().toString();
        InfoStore.thana = thana.getText().toString();
        InfoStore.name = name.getText().toString();
        InfoStore.age = age.getText().toString();
        InfoStore.ocu = ocu.getText().toString();
        InfoStore.mob = mob.getText().toString();
        InfoStore.add = add.getText().toString();
        InfoStore.cls_id = cls_id.getText().toString();
        InfoStore.lab_id = lab_id.getText().toString();
        InfoStore.email = email.getText().toString();
        InfoStore.coll_ads = coll_ads.getText().toString();
        InfoStore.ref = ref.getText().toString();
        InfoStore.ins_name = ins_name.getText().toString();
        InfoStore.ins_dept = ins_dept.getText().toString();
        InfoStore.ins_unit = ins_unit.getText().toString();
        InfoStore.ins_unit_head = ins_unit_head.getText().toString();
        InfoStore.ins_ward = ins_ward.getText().toString();
        InfoStore.ins_cabin = ins_cabin.getText().toString();
        InfoStore.txt_oth_symp = txt_oth_symp.getText().toString();
        InfoStore.th_coun = th_coun.getText().toString();
        InfoStore.day = th_date.getDayOfMonth()+1;
        InfoStore.mth = th_date.getMonth()+1;
        InfoStore.yr = th_date.getYear();
        InfoStore.spec_d = spec_c_date.getDayOfMonth()+1;
        InfoStore.spec_m = spec_c_date.getMonth()+1;
        InfoStore.spec_yr = spec_c_date.getYear();
        InfoStore.crf_oth = crf_oth.getText().toString();
        InfoStore.onset = onset.getText().toString();
        InfoStore.spec_oth_txt = spec_oth_txt.getText().toString();
        InfoStore.remarks = remarks.getText().toString();
        InfoStore.in_cond_by = in_cond_by.getText().toString();
//        check BOx
        InfoStore.copd = getCheckValue(copd);
        InfoStore.asthma = getCheckValue(asthma);
        InfoStore.ild = getCheckValue(ild);
        InfoStore.dm = getCheckValue(dm);
        InfoStore.ihd = getCheckValue(ihd);
        InfoStore.htn = getCheckValue(htn);
        InfoStore.ckd = getCheckValue(ckd);
        InfoStore.cld = getCheckValue(cld);
        InfoStore.md = getCheckValue(md);
        InfoStore.ost = getCheckValue(ost);
        InfoStore.prg = getCheckValue(prg);
        InfoStore.nasal = getCheckValue(nasal);
        InfoStore.throat_swab = getCheckValue(throat_swab);
        InfoStore.sputum = getCheckValue(sputum);
        InfoStore.tracheal_aspirate = getCheckValue(tracheal_aspirate);
        InfoStore.serum = getCheckValue(serum);

        InfoStore.nid = nid.getText().toString();
        InfoStore.dept_name = dept_name.getText().toString();
        InfoStore.service_code = service_code.getText().toString();
        InfoStore.receipt_no = receipt_no.getText().toString();
        InfoStore.freedom_id = freedom_id.getText().toString();
        InfoStore.free_oth_details = free_oth_details.getText().toString();
    }
    private int getCheckValue(CheckBox checkBox){
        return  checkBox.isChecked()?1:0;
    }
    private boolean checkValues(){
        if (InfoStore.name.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert Name",Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(InfoStore.mob.isEmpty() || InfoStore.mob.length()!=11){
//            Toast.makeText(InfoActivity.this,"Check Mobile Number",Toast.LENGTH_SHORT).show();
//            return false;
//        }else if(InfoStore.age.isEmpty()){
//            Toast.makeText(InfoActivity.this,"Insert age",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else if(InfoStore.dis.isEmpty()){
            Toast.makeText(InfoActivity.this,"Insert District name",Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if(InfoStore.thana.isEmpty()){
//            Toast.makeText(InfoActivity.this,"Insert Thana name",Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }
    private void sendData(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://119.40.84.187/surveillance/api/v1/casesubmit";
        if (InfoStore.id>0) {
            url = "http://119.40.84.187/surveillance/api/v1/casesubmit/"+String.valueOf(InfoStore.id);
        }

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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                if (InfoStore.id==0) {
                    Log.d("lat",String.valueOf(lat));
                    params.put("lat",String.valueOf(lat));
                    params.put("lng",String.valueOf(lng));
                }
                params.put("cls",String.valueOf(InfoStore.cls));
                params.put("cls_id",InfoStore.cls_id);
                params.put("lab_id",InfoStore.lab_id);
                params.put("email",InfoStore.email);
                params.put("coll_from",String.valueOf(InfoStore.coll_from));
                params.put("coll_ads", InfoStore.coll_ads);
                params.put("user_id",String.valueOf(DataStore.id));
                params.put("name",InfoStore.name);
                params.put("age",InfoStore.age);
                params.put("ocu",InfoStore.ocu);
                params.put("age_type",String.valueOf(InfoStore.age_type));
                params.put("sex",String.valueOf(InfoStore.sex));
                params.put("emr_type",String.valueOf(InfoStore.emr_type));
                params.put("confm_type",String.valueOf(InfoStore.confm_type));
                params.put("adds",InfoStore.add);
                params.put("mob",InfoStore.mob);

                params.put("ref",InfoStore.ref);
                params.put("ins_name",InfoStore.ins_name);
                params.put("ins_dept",InfoStore.ins_dept);
                params.put("ins_unit",InfoStore.ins_unit);
                params.put("ins_unit_head",InfoStore.ins_unit_head);
                params.put("ins_ward",InfoStore.ins_ward);
                params.put("ins_cabin",InfoStore.ins_cabin);
                params.put("fev",String.valueOf(InfoStore.fev));
                params.put("headache",String.valueOf(InfoStore.headache));
                params.put("cough",String.valueOf(InfoStore.cough));
                params.put("breath",String.valueOf(InfoStore.breath));
                params.put("onset",String.valueOf(InfoStore.onset));
                params.put("txt_oth_symp",InfoStore.txt_oth_symp);
                params.put("cr",String.valueOf(InfoStore.cr));
                params.put("th",String.valueOf(InfoStore.th));
                params.put("th_coun",InfoStore.th_coun);
//                Log.d("DateValue:",String.valueOf(InfoStore.yr)+"-"+String.valueOf(InfoStore.mth)+"-"+String.valueOf(InfoStore.day-1));
                params.put("spec_c_date",String.valueOf(InfoStore.spec_yr)+"-"+String.valueOf(InfoStore.spec_m)+"-"+String.valueOf(InfoStore.spec_d-1));
                params.put("th_date",String.valueOf(InfoStore.yr)+"-"+String.valueOf(InfoStore.mth)+"-"+String.valueOf(InfoStore.day-1));
                params.put("cc",String.valueOf(InfoStore.cc));
                params.put("hc",String.valueOf(InfoStore.hc));
                params.put("copd",String.valueOf(InfoStore.copd));
                params.put("asthma",String.valueOf(InfoStore.asthma));
                params.put("ild",String.valueOf(InfoStore.ild));
                params.put("dm",String.valueOf(InfoStore.dm));
                params.put("ihd",String.valueOf(InfoStore.ihd));
                params.put("htn",String.valueOf(InfoStore.htn));
                params.put("ckd",String.valueOf(InfoStore.ckd));
                params.put("cld",String.valueOf(InfoStore.cld));
                params.put("md",String.valueOf(InfoStore.md));
                params.put("ost",String.valueOf(InfoStore.ost));
                params.put("preg",String.valueOf(InfoStore.prg));
                params.put("crf_oth",String.valueOf(InfoStore.crf_oth));
                params.put("spec",String.valueOf(InfoStore.spec));
                params.put("nasal",String.valueOf(InfoStore.nasal));
                params.put("throat_swab",String.valueOf(InfoStore.throat_swab));
                params.put("sputum",String.valueOf(InfoStore.sputum));
                params.put("tracheal_aspirate",String.valueOf(InfoStore.tracheal_aspirate));
                params.put("serum",String.valueOf(InfoStore.serum));
                params.put("spec_oth_txt",InfoStore.spec_oth_txt);
                params.put("remarks",InfoStore.remarks);
                params.put("in_cond_by",InfoStore.in_cond_by);
                Cursor cdis = db.getDIS(InfoStore.dis);
                cdis.moveToNext();
                params.put("dis",String.valueOf(cdis.getInt(cdis.getColumnIndex(DatabaseHelper.SELF_ID))));
                if(InfoStore.up.isEmpty()){
                    params.put("up","");
                }else{
                    Cursor cup = db.getUP(InfoStore.up);
                    cup.moveToNext();
                    params.put("up",String.valueOf(cup.getInt(cup.getColumnIndex(DatabaseHelper.SELF_ID))));
                }
                if(InfoStore.citycorp.isEmpty()){
                    params.put("citycorp","");
                }else{
                    Cursor cup = db.getCITY(InfoStore.citycorp);
                    cup.moveToNext();
                    params.put("citycorp",String.valueOf(cup.getInt(cup.getColumnIndex(DatabaseHelper.SELF_ID))));
                }
                if(InfoStore.mc.isEmpty()){
                    params.put("mc","");
                }else{
                    Cursor cup = db.getMC(InfoStore.mc);
                    cup.moveToNext();
                    params.put("mc",String.valueOf(cup.getInt(cup.getColumnIndex(DatabaseHelper.SELF_ID))));
                }if(InfoStore.thana.isEmpty()){
                    params.put("thana","");
                }else{
                    Cursor cup = db.getTHANA(InfoStore.thana);
                    cup.moveToNext();
                    params.put("thana",String.valueOf(cup.getInt(cup.getColumnIndex(DatabaseHelper.SELF_ID))));
                }
                Log.d("set BG",InfoStore.bg);
                params.put("bg",InfoStore.bg);
                Log.d("set nid",InfoStore.nid);
                params.put("nid",InfoStore.nid);
                params.put("paid",String.valueOf(InfoStore.paid));
                params.put("receipt_no",InfoStore.receipt_no);
                params.put("reason",String.valueOf(InfoStore.reason));
                params.put("rel",String.valueOf(InfoStore.rel));
                params.put("dept_name",String.valueOf(InfoStore.dept_name));
                params.put("service_code",String.valueOf(InfoStore.service_code));
                params.put("freedom_id",String.valueOf(InfoStore.freedom_id));
                params.put("free_oth_details",InfoStore.free_oth_details);

                params.put("f_dose",f_dose.isChecked()?"1":"0");
                params.put("s_dose",s_dose.isChecked()?"1":"0");
                params.put("no_dose",no_dose.isChecked()?"1":"0");
                params.put("yes_inf",yes_inf.isChecked()?"1":"0");
                params.put("no_inf",no_inf.isChecked()?"1":"0");
                params.put("un_inf",un_inf.isChecked()?"1":"0");
                params.put("f_dose_date",f_dose_date.getText().toString());
                params.put("s_dose_date",s_dose_date.getText().toString());
                params.put("rt_pcr_my",rt_pcr_my.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
