package com.example.caseselection.model;

import java.util.Calendar;
import java.util.Date;

public class InfoStore {

    public static String orgUnit = "";
    public static  String spec_c_date= "";
    public static String mob = "";
    public static String relation = "";
    public static int relation_seq = 0;
    public static String cls_id = "";
    public static String name = "";
    public static String age = "";
    public static String sex = "";
    public static String dis = "";
    public static String up = "";
    public static String add = "";

//    UNUSED
public static int cls = 0;
    public static String lab_id = "";
    public static String email = "";
    public static int spec_yr = 2020;
    public static int spec_m = 3;
    public static int spec_d = 20;
    public static String coll_ads = "";
    public static int coll_from = 0;
    public static int id = 0;

    public static String mc = "";
    public static String thana = "";
    public static String citycorp = "";

    public static String bg = "";
    public static String ocu = "";
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

    public static void clear(){
        orgUnit = "";
        spec_c_date= "";
        mob = "";
        relation = "";
        cls_id = "";
        name = "";
        age = "";
        sex = "";
        dis = "";
        up = "";
        add = "";
    }
}
