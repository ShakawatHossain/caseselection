package com.example.caseselection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.caseselection.model.InfoStore;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.caseselection.model.ListDataStore;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.MyViewHolder> {
    Context ctx;
    ListActivity listActivity;
    RecyclerView recyclerView;
    ArrayList<ListDataStore> listDataStores;

    public RecAdapter(Context context,RecyclerView recyclerView,ListActivity listActivity,ArrayList<ListDataStore> listDataStores){
        this.ctx = context;
        this.listActivity = listActivity;
        this.listDataStores = listDataStores;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list,parent,false);
        viewHolder.setOnClickListener(recListen);
        return new MyViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListDataStore listDataStore = listDataStores.get(position);
        holder.name.setText(listDataStore.name+" ( "+listDataStore.lab_id+" )");
        holder.mob.setText(listDataStore.mob);
        holder.dis.setText("Dis: "+listDataStore.dis);
        String age_type = listDataStore.age_type==0?"Y":"M";
        holder.age.setText("Age: "+String.valueOf(listDataStore.age)+" "+age_type);
        String gen = listDataStore.sex==0?"M":"F";
        holder.sex.setText("Sex: "+gen);
        String sampleDate = String.valueOf(listDataStore.spec_d)+"-"+String.valueOf(listDataStore.spec_m+1)+
                "-"+String.valueOf(listDataStore.spec_yr);
        holder.samDT.setText("SCD: "+sampleDate);
//        if (listDataStore.emr_type==1){
//            holder.star.setVisibility(View.VISIBLE);
//        }
//        if (listDataStore.confm_type==1){
//            holder.blue_flag.setVisibility(View.VISIBLE);
//        }else
//            holder.red_flag.setVisibility(View.VISIBLE);
//        recyclerView.getChildAt(position).OnClickListener(recListen);
    }

    public View.OnClickListener recListen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            InfoStore.cls = listDataStores.get(position).cls;
            InfoStore.cls_id = listDataStores.get(position).cls_id;
            InfoStore.id = listDataStores.get(position).id;
            InfoStore.dis = listDataStores.get(position).dis;
            InfoStore.up = listDataStores.get(position).up;
            InfoStore.mc = listDataStores.get(position).mc;
            InfoStore.thana = listDataStores.get(position).thana;
            InfoStore.citycorp = listDataStores.get(position).citycorp;
            InfoStore.name = listDataStores.get(position).name;
            InfoStore.age = listDataStores.get(position).age;
            InfoStore.ocu = listDataStores.get(position).ocu;
            InfoStore.mob = listDataStores.get(position).mob;
            InfoStore.add = listDataStores.get(position).add;
            InfoStore.sex = listDataStores.get(position).sex;
            InfoStore.age_type = listDataStores.get(position).age_type;
            InfoStore.confm_type = listDataStores.get(position).confm_type;
            InfoStore.emr_type = listDataStores.get(position).emr_type;
            InfoStore.lab_id = listDataStores.get(position).lab_id;
            InfoStore.email = listDataStores.get(position).email;
            InfoStore.coll_from = listDataStores.get(position).coll_from;
            InfoStore.coll_ads = listDataStores.get(position).coll_ads;
            InfoStore.ref = listDataStores.get(position).ref;

//    hospital
            InfoStore.ins_name = listDataStores.get(position).ins_name;
            InfoStore.ins_dept = listDataStores.get(position).ins_dept;
            InfoStore.ins_unit = listDataStores.get(position).ins_unit;
            InfoStore.ins_unit_head = listDataStores.get(position).ins_unit_head;
            InfoStore.ins_ward = listDataStores.get(position).ins_ward;
            InfoStore.ins_cabin = listDataStores.get(position).ins_cabin;


//    suspect criteria
            InfoStore.fev = listDataStores.get(position).fev;
            InfoStore.headache = listDataStores.get(position).headache;
            InfoStore.cough = listDataStores.get(position).cough;
            InfoStore.breath = listDataStores.get(position).breath;
            InfoStore.onset = listDataStores.get(position).onset;
            InfoStore.txt_oth_symp = listDataStores.get(position).txt_oth_symp;
            InfoStore.cr = listDataStores.get(position).cr;
            InfoStore.th = listDataStores.get(position).th;
            InfoStore.th_coun = listDataStores.get(position).th_coun;
            InfoStore.spec_d = listDataStores.get(position).spec_d;
            InfoStore.spec_m = listDataStores.get(position).spec_m;
            InfoStore.spec_yr = listDataStores.get(position).spec_yr;
            InfoStore.day = listDataStores.get(position).day;
            InfoStore.mth = listDataStores.get(position).mth;
            InfoStore.yr = listDataStores.get(position).yr;
            InfoStore.cc = listDataStores.get(position).cc;
            InfoStore.hc = listDataStores.get(position).hc;
            InfoStore.copd = listDataStores.get(position).copd;
            InfoStore.asthma = listDataStores.get(position).asthma;
            InfoStore.ild = listDataStores.get(position).ild;
            InfoStore.dm = listDataStores.get(position).dm;
            InfoStore.ihd = listDataStores.get(position).ihd;
            InfoStore.htn = listDataStores.get(position).htn;
            InfoStore.ckd = listDataStores.get(position).ckd;
            InfoStore.cld = listDataStores.get(position).cld;
            InfoStore.md = listDataStores.get(position).md;
            InfoStore.ost = listDataStores.get(position).ost;
            InfoStore.prg = listDataStores.get(position).preg;
            InfoStore.crf_oth = listDataStores.get(position).crf_oth;

            InfoStore.spec = listDataStores.get(position).spec;
            InfoStore.nasal = listDataStores.get(position).nasal;
            InfoStore.throat_swab = listDataStores.get(position).throat_swab;
            InfoStore.sputum = listDataStores.get(position).sputum;
            InfoStore.tracheal_aspirate = listDataStores.get(position).tracheal_aspirate;
            InfoStore.serum = listDataStores.get(position).serum;

            InfoStore.spec_oth_txt = listDataStores.get(position).spec_oth_txt;
            InfoStore.remarks = listDataStores.get(position).remarks;
            InfoStore.in_cond_by = listDataStores.get(position).in_cond_by;
            Log.d("bg",""+listDataStores.get(position).bg);
            InfoStore.bg = listDataStores.get(position).bg;
            Log.d("nid",""+listDataStores.get(position).nid);
            InfoStore.nid = listDataStores.get(position).nid;
            Log.d("payment",""+listDataStores.get(position).paid);
            InfoStore.paid = listDataStores.get(position).paid;
            InfoStore.receipt_no = listDataStores.get(position).receipt_no;
            Log.d("reason",""+listDataStores.get(position).reason);
            InfoStore.reason = listDataStores.get(position).reason;
            Log.d("rel",""+listDataStores.get(position).rel);
            InfoStore.rel = listDataStores.get(position).rel;
            InfoStore.dept_name = listDataStores.get(position).dept_name;
            InfoStore.service_code = listDataStores.get(position).service_code;
            InfoStore.freedom_id = listDataStores.get(position).freedom_id;
            InfoStore.free_oth_details = listDataStores.get(position).free_oth_details;
            InfoStore.f_dose = listDataStores.get(position).f_dose;
            InfoStore.s_dose = listDataStores.get(position).s_dose;
            InfoStore.no_dose = listDataStores.get(position).no_dose;
            InfoStore.yes_inf = listDataStores.get(position).yes_inf;
            InfoStore.no_inf = listDataStores.get(position).no_inf;
            InfoStore.un_inf = listDataStores.get(position).un_inf;
            InfoStore.f_dose_date = listDataStores.get(position).f_dose_date;
            InfoStore.s_dose_date = listDataStores.get(position).s_dose_date;
            InfoStore.rt_pcr_my = listDataStores.get(position).rt_pcr_my;

            Log.d("Clicked on",""+position);
            ctx.startActivity(new Intent(ctx,InfoActivity.class));
        }
    };

    @Override
    public int getItemCount() {
        return listDataStores.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,age,sex,mob,dis,samDT;
        ImageView red_flag,blue_flag,star;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            sex = (TextView) itemView.findViewById(R.id.sex);
            mob = (TextView) itemView.findViewById(R.id.mob);
            dis = (TextView) itemView.findViewById(R.id.dis);
            samDT = (TextView) itemView.findViewById(R.id.samDT);
            red_flag = (ImageView) itemView.findViewById(R.id.red_flag);
            blue_flag = (ImageView) itemView.findViewById(R.id.blue_flag);
            star = (ImageView) itemView.findViewById(R.id.star);
        }
    }
}
