package com.trak.sam.collegelog.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.model.Department;
import com.trak.sam.collegelog.model.School;

public class DisplaySchoolDialog extends DialogFragment {

    public School school;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog_show_school,container);
        ((TextView)view.findViewById(R.id.text_name)).setText(school.name);
        ((TextView)view.findViewById(R.id.text_address)).setText(school.address);
        ((TextView)view.findViewById(R.id.text_city)).setText(school.city);
        ((TextView)view.findViewById(R.id.text_url)).setText(school.url);
        LinearLayout linearLayout = view.findViewById(R.id.department_container);
        for(Department department:school.departments){
            TextView textView = new TextView(getActivity());
            textView.setText(department.name);
            linearLayout.addView(textView);
        }
        ((Button)view.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

}
