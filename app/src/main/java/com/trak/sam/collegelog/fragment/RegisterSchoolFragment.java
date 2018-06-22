package com.trak.sam.collegelog.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.CreateItemListFragment;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.model.Department;

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
    private LinearLayout mLinearLayout;
    private CreateItemListFragment<Department> mCreateDepartmentFragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register_school, container, false);
        DepartmentCreateListFragment departmentCreateListFragment = DepartmentCreateListFragment.newInstance();
        mCreateDepartmentFragment = departmentCreateListFragment;
        getChildFragmentManager().beginTransaction()
                .add(R.id.department_container, departmentCreateListFragment)
                .commit();

        return mView;
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
        mFragmentChangeListener.setAddButtonListener(onAddButtonClick);
    }

}
