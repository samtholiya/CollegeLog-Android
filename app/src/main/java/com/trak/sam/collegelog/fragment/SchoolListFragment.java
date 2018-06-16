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
import com.trak.sam.collegelog.ViewAdapter.SchoolRecyclerViewAdapter;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnSchoolListItemClick;
import com.trak.sam.collegelog.helper.BaseOnScrollListener;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.service.SchoolService;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class SchoolListFragment extends Fragment implements OnSchoolListItemClick, OnAddButtonClick {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnSchoolListItemClick mListener;
    private RecyclerView mRecyclerView;
    private FragmentChangeListener mFragmentChangeListner;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<School> mSchoolArrayList;
    private BaseOnScrollListener.PageOperator mPageOperator;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SchoolListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SchoolListFragment newInstance() {
        return new SchoolListFragment();
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
            mLinearLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSchoolListItemClick) {
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

        mSchoolArrayList = new ArrayList<>();
        BaseOnScrollListener<School> baseOnScrollListener = new BaseOnScrollListener<>(mLinearLayoutManager);
        mPageOperator = new PageOperatorImpl(baseOnScrollListener);

        SchoolRecyclerViewAdapter schoolRecyclerViewAdapter = new SchoolRecyclerViewAdapter(mSchoolArrayList, mListener);
        mRecyclerView.setAdapter(schoolRecyclerViewAdapter);
        baseOnScrollListener.addPageOperator(mPageOperator);
        baseOnScrollListener.setAdapter(schoolRecyclerViewAdapter);
        baseOnScrollListener.setArrayList(mSchoolArrayList);
        mRecyclerView.addOnScrollListener(baseOnScrollListener);
        mRecyclerView.setAdapter(schoolRecyclerViewAdapter);
    }

    private class PageOperatorImpl implements BaseOnScrollListener.PageOperator {

        private BaseOnScrollListener<School> mSchoolBaseOnScrollListener;
        private PageOperatorImpl(BaseOnScrollListener<School> schoolBaseOnScrollListener) {
            mSchoolBaseOnScrollListener = schoolBaseOnScrollListener;
        }

        @Override
        public void loadDataBellow(long offset, long limit, RecyclerView view) {
            SchoolService.getSchools(offset, limit, new SchoolCallbackHandler(true, mSchoolBaseOnScrollListener));
        }

        @Override
        public void loadDataAbove(long offset, long limit, RecyclerView view) {
            SchoolService.getSchools(offset, limit, new SchoolCallbackHandler(false, mSchoolBaseOnScrollListener));
        }
    }



    @Override
    public void onListItemClick(School item) {

    }

    @Override
    public void OnAddButtonClick(View view) {

    }

    private class SchoolCallbackHandler implements BaseHttpCallback<School> {

        private boolean mIsBellow;
        private BaseOnScrollListener<School> mBaseOnScrollListener;

        private SchoolCallbackHandler(boolean isBellow, BaseOnScrollListener<School> baseOnScrollListener) {
            mIsBellow = isBellow;
            mBaseOnScrollListener = baseOnScrollListener;
        }

        @Override
        public void onItemReceived(School item) {

        }

        @Override
        public void onItemsReceived(School[] items) {
            if(mIsBellow){
                mBaseOnScrollListener.addPageBellow(items);
            } else {
                mBaseOnScrollListener.addPageAbove(items);
            }
        }

        @Override
        public void onFailed(Exception e) {

        }
    }
}
