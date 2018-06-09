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
import com.trak.sam.collegelog.ViewAdapter.UserRecyclerViewAdapter;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnUserListItemClick;
import com.trak.sam.collegelog.model.User;
import com.trak.sam.collegelog.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class UserListFragment extends Fragment implements OnAddButtonClick {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private OnUserListItemClick mListener;
    private RecyclerView mRecyclerView;
    private FragmentChangeListener mFragmentChangeListner;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

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
        if (context instanceof OnUserListItemClick)
            mListener = (OnUserListItemClick) context;
        if (context instanceof FragmentChangeListener)
            mFragmentChangeListner = (FragmentChangeListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        UserService.getUsersOfRole("all", new UserCallbackHandler());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void OnAddButtonClick(View view) {
        mFragmentChangeListner.replaceFragment(RegisterUserFragment.newInstance(), false);
    }


    private class UserCallbackHandler implements BaseHttpCallback<User> {

        @Override
        public void onItemReceived(User item) {

        }

        @Override
        public void onItemsReceived(User[] items) {
            ArrayList<User> userArrayList = new ArrayList<>(Arrays.asList(items));
            mRecyclerView.setAdapter(new UserRecyclerViewAdapter(userArrayList, mListener));
        }

        @Override
        public void onFailed(Exception e) {

        }
    }
}
