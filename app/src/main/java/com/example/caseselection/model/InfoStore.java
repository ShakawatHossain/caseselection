package com.example.caseselection.model;

import java.util.Calendar;
import java.util.Date;

public class InfoStore {

//    AutoCompleteTextView dis,thana;
//    EditText name,age,mob,add;
//    RadioGroup sex,age_type,confm_type,emr_type;
    public static int cls = 0;
    public static String cls_id = "";
    public static String lab_id = "";
    public static String email = "";
    public static int spec_yr = 2020;
    public static int spec_m = 3;
    public static int spec_d = 20;
    public static String coll_ads = "";
    public static int coll_from = 0;
    public static int id = 0;
    public static String dis = "";
    public static String up = "";
    public static String mc = "";
    public static String thana = "";
    public static String citycorp = "";
    public static String name = "";
    public static String age = "";
    public static String bg = "";
    public static String ocu = "";
    public static String mob = "";
    public static String add = "";
    public static int sex = 0;
    public static int age_type = 0;
    public static int confm_type = 0;
    public static int emr_type = 0;
    public static String ref = "";
    public static String nid = "";

//    Payment
    public static int paid=0;   //0=paid; 1=free
    public static String receipt_no = "";
    public static int reason = 0;
    public static int rel = 0;
    public static String dept_name = "";
    public static String service_code = "";
    public static String freedom_id = "";
    public static String free_oth_details = "";

//    hospital
    public static String ins_name = "";
    public static String ins_dept = "";
    public static String ins_unit = "";
    public static String ins_unit_head = "";
    public static String ins_ward = "";
    public static String ins_cabin = "";

//    suspect criteria
    public static int fev = 0;
    public static int headache = 0;
    public static int cough = 0;
    public static int breath = 0;
    public static String txt_oth_symp = "";
    public static String onset = "2020-04-02";

    public static int cr = 0;
    public static int th = 0;
    public static String th_coun = "";
    public static int day = 2;
    public static int mth = 4;
    public static int yr = 2020;
    public static int cc = 0;
    public static int hc = 0;
    public static int copd = 0;
    public static int asthma = 0;
    public static int ild = 0;
    public static int dm = 0;
    public static int ihd = 0;
    public static int htn= 0;
    public static int ckd = 0;
    public static int cld = 0;
    public static int md = 0;
    public static int ost = 0;
    public static int prg = 0;
    public static String crf_oth = "";

    public static int spec = 0;
    public static int nasal = 0;
    public static int throat_swab = 0;
    public static int sputum = 0;
    public static int tracheal_aspirate = 0;
    public static int serum = 0;
    public static String spec_oth_txt = "";
    public static String remarks = "";
    public static String in_cond_by = "";

    public static int f_dose=0;
    public static int s_dose=0;
    public static int no_dose=0;
    public static int yes_inf=0;
    public static int no_inf=0;
    public static int un_inf=0;
    public static String f_dose_date="";
    public static String s_dose_date="";
    public static String rt_pcr_my="";

    public static void clear(){
        Date dt = new Date();
//      System.out.println(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        day = cal.get(Calendar.DAY_OF_MONTH);
        mth = cal.get(Calendar.MONTH);
        yr = cal.get(Calendar.YEAR);
        cls = 0;
        cls_id = "";
        id = 0;
        dis = "";
        up = "";
        mc = "";
        thana = "";
        citycorp = "";
        name = "";
        spec_yr = cal.get(Calendar.YEAR);
        spec_m = cal.get(Calendar.MONTH);
        spec_d = cal.get(Calendar.DAY_OF_MONTH);
        age = "";
        ocu = "";
        mob = "";
        add = "";
        sex = 0;
        age_type = 0;
        confm_type = 0;
        emr_type = 0;
        lab_id = "";
        email = "";
        coll_ads = "";
        coll_from = 0;
        ref = "";

//    Payment
        paid=0;   //0=paid; 1=free
        receipt_no = "";
        reason = 0;
        rel = 0;
        dept_name = "";
        service_code = "";
        freedom_id = "";
        free_oth_details = "";


//    hospital
        ins_name = "";
        ins_dept = "";
        ins_unit = "";
        ins_unit_head = "";
        ins_ward = "";
        ins_cabin = "";

//    suspect criteria
        fev = 0;
        headache = 0;
        cough = 0;
        breath = 0;
        txt_oth_symp = "";
        onset = "";

        cr = 0;
        th = 0;
        th_coun = "";
        cc = 0;
        hc = 0;
        copd = 0;
        asthma = 0;
        ild = 0;
        dm = 0;
        ihd = 0;
        htn= 0;
        ckd = 0;
        cld = 0;
        md = 0;
        ost = 0;
        prg = 0;
        crf_oth = "";

        spec = 0;
        nasal = 0;
        throat_swab = 0;
        sputum = 0;
        tracheal_aspirate = 0;
        serum = 0;

        spec_oth_txt = "";
        remarks = "";
        in_cond_by = "";

        f_dose=0;
        s_dose=0;
        no_dose=0;
        yes_inf=0;
        no_inf=0;
        un_inf=0;
        f_dose_date="";
        s_dose_date="";
        rt_pcr_my="";
    }
}
