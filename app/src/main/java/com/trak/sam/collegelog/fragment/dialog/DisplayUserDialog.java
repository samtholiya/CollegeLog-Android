package com.trak.sam.collegelog.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.model.User;

public class DisplayUserDialog extends DialogFragment {

    public User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_show_user, container);

        ((TextView) view.findViewById(R.id.text_name)).setText(user.firstName + " " + user.lastName);
        ((TextView) view.findViewById(R.id.text_username)).setText(user.userName);
        ((TextView) view.findViewById(R.id.text_address)).setText(user.address);
        ((TextView) view.findViewById(R.id.text_phone_number)).setText(user.mobile);
        ((TextView) view.findViewById(R.id.text_email)).setText(user.email);
        ((TextView) view.findViewById(R.id.text_date_of_birth)).setText(user.dateOfBirth.toString());

        if (user.departments != null && user.departments.length > 0) {
            ((TextView) view.findViewById(R.id.text_department)).setText(user.departments[0].name);
            ((TextView) view.findViewById(R.id.text_school)).setText(user.departments[0].school.name);
        }
        ((Button)view.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ((TextView) view.findViewById(R.id.text_role)).setText(user.role.name);
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
