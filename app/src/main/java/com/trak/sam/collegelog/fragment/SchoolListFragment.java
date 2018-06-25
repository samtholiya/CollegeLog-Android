package com.trak.sam.collegelog.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.ViewAdapter.SchoolRecyclerViewAdapter;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnItemSwiped;
import com.trak.sam.collegelog.callback.OnSchoolListItemClick;
import com.trak.sam.collegelog.fragment.dialog.DisplaySchoolDialog;
import com.trak.sam.collegelog.helper.BaseOnScrollListener;
import com.trak.sam.collegelog.helper.HttpPageLoaderHelper;
import com.trak.sam.collegelog.helper.SwipeToEditDeleteHelper;
import com.trak.sam.collegelog.model.DeleteResult;
import com.trak.sam.collegelog.model.School;
import com.trak.sam.collegelog.service.SchoolService;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class SchoolListFragment extends Fragment implements OnSchoolListItemClick, OnAddButtonClick, OnItemSwiped {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnSchoolListItemClick mListener;
    private RecyclerView mRecyclerView;
    private FragmentChangeListener mFragmentChangeListner;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<School> mSchoolArrayList;
    private BaseOnScrollListener.PageOperator mPageOperator;
    private SchoolRecyclerViewAdapter mSchoolRecyclerViewAdapter;
    private BaseOnScrollListener<School> mBaseOnScrollListener;

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
        mListener = this;
        if (context instanceof FragmentChangeListener) {
            mFragmentChangeListner = (FragmentChangeListener) context;
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

        mFragmentChangeListner.setAddButtonListener(this);
        mSchoolArrayList = new ArrayList<>();
        mBaseOnScrollListener = new BaseOnScrollListener<>(mLinearLayoutManager);

        mPageOperator = new PageOperatorImpl(mBaseOnScrollListener);
        mSchoolRecyclerViewAdapter = new SchoolRecyclerViewAdapter(mSchoolArrayList, mListener);
        mRecyclerView.setAdapter(mSchoolRecyclerViewAdapter);
        mBaseOnScrollListener.addPageOperator(mPageOperator);
        mBaseOnScrollListener.setAdapter(mSchoolRecyclerViewAdapter);
        mBaseOnScrollListener.setArrayList(mSchoolArrayList);
        mRecyclerView.addOnScrollListener(mBaseOnScrollListener);
        mRecyclerView.setAdapter(mSchoolRecyclerViewAdapter);
        ItemTouchHelper instituteSwipeHelper = new ItemTouchHelper(new SwipeToEditDeleteHelper(getContext(), this));
        instituteSwipeHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void swipedLeft(int position) {
        SchoolService.deleteSchool(mSchoolArrayList.get(position).id, new SchoolDeleteResult(position));
    }

    @Override
    public void swipedRight(int position) {
        mFragmentChangeListner.replaceFragment(RegisterSchoolFragment.newInstance(mSchoolArrayList.get(position)));
    }

    private class SchoolDeleteResult implements BaseHttpCallback<DeleteResult> {

        private int mPosition;

        private SchoolDeleteResult(int position) {
            mPosition = position;
        }

        @Override
        public void onItemReceived(DeleteResult item) {
            mBaseOnScrollListener.removeItem(mPosition);
        }

        @Override
        public void onItemsReceived(DeleteResult[] items) {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }


    private class PageOperatorImpl implements BaseOnScrollListener.PageOperator {

        private BaseOnScrollListener<School> mSchoolBaseOnScrollListener;

        private PageOperatorImpl(BaseOnScrollListener<School> schoolBaseOnScrollListener) {
            mSchoolBaseOnScrollListener = schoolBaseOnScrollListener;
        }

        @Override
        public void loadDataBellow(long offset, long limit, RecyclerView view) {
            SchoolService.getSchools(offset, limit, new HttpPageLoaderHelper<>(true, mSchoolBaseOnScrollListener));
        }

        @Override
        public void loadDataAbove(long offset, long limit, RecyclerView view) {
            SchoolService.getSchools(offset, limit, new HttpPageLoaderHelper<>(false, mSchoolBaseOnScrollListener));
        }
    }


    @Override
    public void onListItemClick(School item) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DisplaySchoolDialog dialogFragment = new DisplaySchoolDialog();
        dialogFragment.school = item;
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void OnAddButtonClick(View view) {
        mFragmentChangeListner.replaceFragment(RegisterSchoolFragment.newInstance());
    }
}
