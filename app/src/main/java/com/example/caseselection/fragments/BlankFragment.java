package com.example.caseselection.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.caseselection.R;
import com.example.caseselection.interfaces.SearchInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    CheckBox id,name,date;
    EditText txt_id,txt_name,txt_date;
    Button btn;
    private static SearchInterface searchInterface;
    // TODO: Rename and change types of parameters

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(SearchInterface searchInterface) {
        BlankFragment fragment = new BlankFragment();
        BlankFragment.searchInterface = searchInterface;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        id = (CheckBox) view.findViewById(R.id.id);
        name = (CheckBox) view.findViewById(R.id.name);
        date = (CheckBox) view.findViewById(R.id.date);

        txt_id = (EditText) view.findViewById(R.id.txt_id);
        txt_name = (EditText) view.findViewById(R.id.txt_name);
        txt_date = (EditText) view.findViewById(R.id.txt_date);
        btn = (Button) view.findViewById(R.id.submit);
        btn.setOnClickListener(onClickListener);
        return view;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (id.isChecked()){
                searchInterface.search("id",txt_id.getText().toString());
            }else if(name.isChecked()){
                searchInterface.search("name",txt_name.getText().toString());
            }else{
                searchInterface.search("date",txt_date.getText().toString());
            }
            dismiss();
        }
    };
}
