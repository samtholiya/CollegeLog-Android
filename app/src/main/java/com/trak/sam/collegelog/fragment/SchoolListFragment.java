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
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnSchoolListItemClick;
import com.trak.sam.collegelog.callback.SchoolCallback;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.service.SchoolService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class SchoolListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnSchoolListItemClick mListener;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SchoolListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SchoolListFragment newInstance() {
        SchoolListFragment fragment = new SchoolListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentChangeListener) {
            mListener = (OnSchoolListItemClick) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onResume() {
        super.onResume();
        SchoolService.getSchools(new SchoolCallbackHandler());
    }

    private class SchoolCallbackHandler implements SchoolCallback {

        @Override
        public void onSchoolReceived(School school) {

        }

        @Override
        public void onSchoolsReceived(School[] schools) {
            ArrayList<School> schoolArrayList = new ArrayList<>(Arrays.asList(schools));
            mRecyclerView.setAdapter(new SchoolRecyclerViewAdapter(schoolArrayList, mListener));
        }

        @Override
        public void onFailed(Exception e) {

        }
    }
}
