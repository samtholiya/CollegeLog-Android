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
import android.widget.FrameLayout;

import com.trak.sam.collegelog.R;
import com.trak.sam.collegelog.callback.FragmentChangeListener;
import com.trak.sam.collegelog.callback.OnSchoolListItemClick;
import com.trak.sam.collegelog.fragment.SchoolListFragment;
import com.trak.sam.collegelog.model.School;

public class AdminActivity extends AppCompatActivity implements FragmentChangeListener, OnSchoolListItemClick {

    private FrameLayout mTextMessage;
    private FloatingActionButton mAddButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, SchoolListFragment.newInstance())
                .commit();

    }

    @Override
    public void replaceFragment(Fragment fragment) {
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
    public void onListItemClick(School item) {
        Log.d("Sam", item.name);
    }
}
