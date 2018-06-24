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
import com.trak.sam.collegelog.ViewAdapter.UserRecyclerViewAdapter;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnItemSwiped;
import com.trak.sam.collegelog.callback.OnUserListItemClick;
import com.trak.sam.collegelog.fragment.dialog.DisplayUserDialog;
import com.trak.sam.collegelog.helper.BaseOnScrollListener;
import com.trak.sam.collegelog.helper.BaseViewAdapter;
import com.trak.sam.collegelog.helper.HttpPageLoaderHelper;
import com.trak.sam.collegelog.helper.SwipeToEditDeleteHelper;
import com.trak.sam.collegelog.model.DeleteResult;
import com.trak.sam.collegelog.model.User;
import com.trak.sam.collegelog.service.UserService;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class UserListFragment extends Fragment implements OnAddButtonClick, OnUserListItemClick, OnItemSwiped {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private OnUserListItemClick mListener;
    private RecyclerView mRecyclerView;
    private FragmentChangeListener mFragmentChangeListener;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<User> mUserArrayList;
    private BaseOnScrollListener.PageOperator mPageOperator;
    private BaseViewAdapter<UserRecyclerViewAdapter.ViewHolder, User> mUserRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserListFragment newInstance() {
        return new UserListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mListener = this;
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
        /*
        if (context instanceof OnUserListItemClick)
            mListener = (OnUserListItemClick) context;
        */
        if (context instanceof FragmentChangeListener)
            mFragmentChangeListener = (FragmentChangeListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        mUserArrayList = new ArrayList<>();
        mFragmentChangeListener.setAddButtonListener(this);
        BaseOnScrollListener<User> baseOnScrollListener = new BaseOnScrollListener<>(mLinearLayoutManager);
        mPageOperator = new PageOperatorImpl(baseOnScrollListener);

        mUserRecyclerViewAdapter = new UserRecyclerViewAdapter(mUserArrayList, mListener);
        mRecyclerView.setAdapter(mUserRecyclerViewAdapter);
        baseOnScrollListener.addPageOperator(mPageOperator);
        baseOnScrollListener.setAdapter(mUserRecyclerViewAdapter);
        baseOnScrollListener.setArrayList(mUserArrayList);
        mRecyclerView.addOnScrollListener(baseOnScrollListener);
        mRecyclerView.setAdapter(mUserRecyclerViewAdapter);
        ItemTouchHelper instituteSwipeHelper = new ItemTouchHelper(new SwipeToEditDeleteHelper(getContext(), this));
        instituteSwipeHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnAddButtonClick(View view) {
        if (mFragmentChangeListener != null)
            mFragmentChangeListener.replaceFragment(RegisterUserFragment.newInstance());
    }

    @Override
    public void onListItemClick(User item) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DisplayUserDialog dialogFragment = new DisplayUserDialog();
        dialogFragment.user = item;
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void swipedLeft(int position) {
        UserService.deleteUserWithId(mUserArrayList.get(position).id, new UserDeleteResult(position));
    }

    private class UserDeleteResult implements BaseHttpCallback<DeleteResult> {

        private int mPosition;

        public UserDeleteResult(int position) {
            mPosition = position;
        }

        @Override
        public void onItemReceived(DeleteResult item) {
            mUserRecyclerViewAdapter.removeItem(mPosition);
        }

        @Override
        public void onItemsReceived(DeleteResult[] items) {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }

    @Override
    public void swipedRight(int position) {
        //mFragmentChangeListener.replaceFragment();
    }

    private class PageOperatorImpl implements BaseOnScrollListener.PageOperator {

        private BaseOnScrollListener<User> mBaseOnScrollListener;

        private PageOperatorImpl(BaseOnScrollListener<User> baseOnScrollListener) {
            mBaseOnScrollListener = baseOnScrollListener;
        }

        @Override
        public void loadDataBellow(long offset, long limit, RecyclerView view) {
            UserService.getUsersOfRole("all", offset, limit, new HttpPageLoaderHelper<>(true, mBaseOnScrollListener));
        }

        @Override
        public void loadDataAbove(long offset, long limit, RecyclerView view) {
            UserService.getUsersOfRole("all", offset, limit, new HttpPageLoaderHelper<>(false, mBaseOnScrollListener));
        }
    }

}
