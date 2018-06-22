package com.trak.sam.collegelog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.ViewAdapter.CreateDepartmentRecyclerViewAdapter;
import com.trak.sam.collegelog.callback.CreateItemListFragment;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnDeleteItemClicked;
import com.trak.sam.collegelog.model.Department;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class DepartmentCreateListFragment extends Fragment implements OnAddButtonClick, OnDeleteItemClicked, CreateItemListFragment<Department> {

    private FragmentChangeListener mFragmentChangeListener;
    private ArrayList<Department> mDepartments;
    private CreateDepartmentRecyclerViewAdapter mCreateDepartmentRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DepartmentCreateListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DepartmentCreateListFragment newInstance() {
        return new DepartmentCreateListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department_create_list, container, false);
        mDepartments = new ArrayList<>();
        mFragmentChangeListener.setAddButtonListener(this);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mCreateDepartmentRecyclerViewAdapter = new CreateDepartmentRecyclerViewAdapter(mDepartments, this);
            recyclerView.setAdapter(mCreateDepartmentRecyclerViewAdapter);
        }
        return view;
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
    public void OnAddButtonClick(View view) {

    }

    @Override
    public void onDeleteItemClick(View view, int position) {
        mDepartments.remove(position);
        mCreateDepartmentRecyclerViewAdapter.notifyItemRemoved(position);
    }

    @Override
    public ArrayList<Department> getItemList() {
        return mDepartments;
    }

    @Override
    public void setItemList(ArrayList<Department> arrayList) {

    }
}
