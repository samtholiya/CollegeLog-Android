package com.trak.sam.collegelog.fragment;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.DepartmentCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.model.Department;
import com.trak.sam.collegelog.model.Role;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.model.User;
import com.trak.sam.collegelog.service.RoleService;
import com.trak.sam.collegelog.service.SchoolService;
import com.trak.sam.collegelog.service.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterUserFragment} interface
 * to handle interaction events.
 * Use the {@link RegisterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Department> departmentArrayList;
    private EditText username;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText address;
    private EditText password;
    private EditText confirmPassword;
    private AppCompatSpinner schools;
    private AppCompatSpinner dateOfBirth;
    private AppCompatSpinner role;
    private Button register;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar calendar = Calendar.getInstance();
    private AppCompatSpinner department;
    private View mView;
    private FragmentChangeListener mListener;
    private ArrayList<Role> roleArrayList;
    private ArrayList<School> mSchoolArrayList;

    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterUserFragment newInstance() {
        RegisterUserFragment fragment = new RegisterUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_register_user, container, false);
        department = mView.findViewById(R.id.input_department);

        username = mView.findViewById(R.id.input_username);
        firstName = mView.findViewById(R.id.input_first_name);
        lastName = mView.findViewById(R.id.input_last_name);
        password = mView.findViewById(R.id.input_password);
        email = mView.findViewById(R.id.input_email);
        phoneNumber = mView.findViewById(R.id.input_phone_number);
        dateOfBirth = mView.findViewById(R.id.input_date_of_birth);
        role = mView.findViewById(R.id.input_role);
        address = mView.findViewById(R.id.input_address);
        confirmPassword = mView.findViewById(R.id.input_confirm_password);
        schools = mView.findViewById(R.id.input_school);
        schools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> spinnerArray = new ArrayList<String>();
                departmentArrayList = new ArrayList<>();
                for (Department department : mSchoolArrayList.get(i).departments) {
                    spinnerArray.add(department.name);
                    departmentArrayList.add(department);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                List<String> spinnerArray = new ArrayList<String>();

                spinnerArray.add(calendar.getTime().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(), android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dateOfBirth.setAdapter(adapter);
            }

        };

        dateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    new DatePickerDialog(getContext(), date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();

                return true;
            }
        });
        register = mView.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    User user = new User();
                    user.userName = username.getText().toString();
                    user.password = password.getText().toString();
                    user.firstName = firstName.getText().toString();
                    user.lastName = lastName.getText().toString();
                    user.address = address.getText().toString();
                    user.dateOfBirth = calendar.getTime();
                    user.email = email.getText().toString();
                    user.mobile = phoneNumber.getText().toString();
                    Department[] departments = new Department[1];
                    departments[0] = departmentArrayList.get(department.getSelectedItemPosition());
                    user.role = roleArrayList.get(role.getSelectedItemPosition());

                    user.departments = departments;
                    UserService.register(user, new BaseHttpCallback<User>() {
                        @Override
                        public void onItemReceived(User item) {
                            getActivity().onBackPressed();
                        }

                        @Override
                        public void onItemsReceived(User[] items) {

                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(getActivity(),"Register user failed",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Snackbar.make(mView, "Passwords don't match", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        return mView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        SchoolService.getSchools(new SchoolCallbackHandler());
        RoleService.getRoles(new RoleCallbackHandler());
    }

    private class RoleCallbackHandler implements BaseHttpCallback<Role> {


        @Override
        public void onItemReceived(Role item) {

        }

        @Override
        public void onItemsReceived(Role[] items) {
            List<String> spinnerArray = new ArrayList<String>();
            roleArrayList = new ArrayList<Role>();
            for (Role role : items) {
                spinnerArray.add(role.name);
                roleArrayList.add(role);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            role.setAdapter(adapter);
        }

        @Override
        public void onFailed(Exception e) {

        }
    }

    private class SchoolCallbackHandler implements BaseHttpCallback<School> {

        @Override
        public void onItemReceived(School item) {

        }

        @Override
        public void onItemsReceived(School[] items) {
            List<String> spinnerArray = new ArrayList<String>();
            mSchoolArrayList = new ArrayList<School>();
            for (School school : items) {
                spinnerArray.add(school.name);
                mSchoolArrayList.add(school);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            schools.setAdapter(adapter);
        }

        @Override
        public void onFailed(Exception e) {

        }
    }

    private class DepartmentCallbackHandler implements DepartmentCallback {

        @Override
        public void onDepartmentReceived(Department department) {

        }

        @Override
        public void onDepartmentsReceived(Department[] departments) {

            List<String> spinnerArray = new ArrayList<String>();
            departmentArrayList = new ArrayList<>();
            for (Department department : departments) {
                spinnerArray.add(department.name);
                departmentArrayList.add(department);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item, spinnerArray);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            department.setAdapter(adapter);
        }

        @Override
        public void onFailed(Exception e) {

        }
    }

}
