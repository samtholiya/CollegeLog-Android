package com.trak.sam.collegelog.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.activity.AdminActivity;
import com.trak.sam.collegelog.callback.BaseHttpCallback;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.model.User;
import com.trak.sam.collegelog.service.UserService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements BaseHttpCallback<User>{

    private View mView;

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Button mRegister;
    private FragmentChangeListener mListener;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_main, container, false);
        mUsername = (EditText) mView.findViewById(R.id.input_username);
        mPassword = (EditText) mView.findViewById(R.id.input_password);
        mLogin = (Button) mView.findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService.login(mUsername.getText().toString(),
                        mPassword.getText().toString(), MainActivityFragment.this);
            }
        });
        mRegister = mView.findViewById(R.id.register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.replaceFragment(RegisterUserFragment.newInstance());
            }
        });
        return mView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentChangeListener) {
            mListener = (FragmentChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onItemReceived(User item) {
        if(item.role.name.equals("Admin")) {
            Intent intent = new Intent(getActivity(), AdminActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemsReceived(User[] items) {

    }

    @Override
    public void onFailed(Exception e) {
        Toast.makeText(getContext(),"Failed"+e.getMessage(),Toast.LENGTH_LONG).show();
    }
}
