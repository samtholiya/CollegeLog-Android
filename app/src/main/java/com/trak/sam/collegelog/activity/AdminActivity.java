package com.trak.sam.collegelog.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnAddButtonClick;
import com.trak.sam.collegelog.callback.OnSchoolListItemClick;
import com.trak.sam.collegelog.fragment.SchoolListFragment;
import com.trak.sam.collegelog.fragment.UserListFragment;
import com.trak.sam.collegelog.model.School;

public class AdminActivity extends AppCompatActivity implements FragmentChangeListener, OnSchoolListItemClick {

    private FloatingActionButton mAddButton;
    private OnAddButtonClick mOnAddButtonClickListner;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_users:
                    replaceFragment(UserListFragment.newInstance());
                    return true;
                case R.id.navigation_schools:
                    replaceFragment(SchoolListFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAddButton = findViewById(R.id.fab);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnAddButtonClickListner != null)
                    mOnAddButtonClickListner.OnAddButtonClick(view);
            }
        });

        SchoolListFragment schoolListFragment = SchoolListFragment.newInstance();
        addFragment(schoolListFragment);
    }

    public void addFragment(Fragment fragment) {
        mAddButton.hide();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment) == null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment, fragment)
                    .commit();
    }



    @Override
    public void replaceFragment(Fragment fragment) {
        mAddButton.hide();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment, fragment.toString())
                .addToBackStack(fragment.toString())
                .commit();
    }

    @Override
    public void onClickedView(View view) {

    }

    @Override
    public void setAddButtonListener(OnAddButtonClick onAddButtonClick) {
        mOnAddButtonClickListner = onAddButtonClick;
        mAddButton.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onListItemClick(School item) {
        Log.d("Sam", item.name);
    }
}
