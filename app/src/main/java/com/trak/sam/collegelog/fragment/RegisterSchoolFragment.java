package com.trak.sam.collegelog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.CreateItemListFragment;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.model.Department;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.service.SchoolService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RegisterSchoolFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterSchoolFragment extends Fragment implements FragmentChangeListener {

    private View mView;
    private LinearLayout mContainer;
    private FragmentChangeListener mFragmentChangeListener;
    private EditText mName;
    private EditText mAddress;
    private EditText mCity;
    private EditText mUrl;
    private Button mSave;
    private ArrayList<Department> mDepartments;
    private CreateItemListFragment<Department> mCreateDepartmentFragment;
    private School school;

    public RegisterSchoolFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RegisterSchoolFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterSchoolFragment newInstance() {
        return new RegisterSchoolFragment();
    }

    public static Fragment newInstance(School item) {
        RegisterSchoolFragment registerSchoolFragment = new RegisterSchoolFragment();
        registerSchoolFragment.school = item;
        return registerSchoolFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register_school, container, false);
        DepartmentCreateListFragment departmentCreateListFragment = DepartmentCreateListFragment.newInstance();

        mName = mView.findViewById(R.id.input_name);
        mCity = mView.findViewById(R.id.input_city);
        mAddress = mView.findViewById(R.id.input_address);
        mUrl = mView.findViewById(R.id.input_url);
        mSave = mView.findViewById(R.id.save);

        mCreateDepartmentFragment = departmentCreateListFragment;
        mDepartments = new ArrayList<>();
        if (school != null) {
            mName.setText(school.name);
            mCity.setText(school.city);
            mAddress.setText(school.address);
            mUrl.setText(school.url);
            mDepartments = new ArrayList<>(Arrays.asList(school.departments));
        } else {
            mDepartments.add(new Department());
        }
        mCreateDepartmentFragment.setItemList(mDepartments);
        getChildFragmentManager().beginTransaction()
                .add(R.id.department_container, departmentCreateListFragment)
                .commit();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                School school = new School();
                school.name = mName.getText().toString();
                school.city = mCity.getText().toString();
                school.address = mAddress.getText().toString();
                school.url = mUrl.getText().toString();
                school.departments = mDepartments.toArray(new Department[1]);
                SchoolService.saveSchool(school, new SchoolResponse());
            }
        });
        return mView;
    }

    private class SchoolResponse implements BaseHttpCallback<School> {

        @Override
        public void onItemReceived(School item) {
            getActivity().onBackPressed();
        }

        @Override
        public void onItemsReceived(School[] items) {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentChangeListener)
            mFragmentChangeListener = (FragmentChangeListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void replaceFragment(Fragment fragment) {

    }

    @Override
    public void onClickedView(View view) {

    }

    @Override
    public void setAddButtonListener(OnAddButtonClick onAddButtonClick) {
        Log.d("sam", "RegisterSchoolFrag");
        mFragmentChangeListener.setAddButtonListener(onAddButtonClick);
    }

}
