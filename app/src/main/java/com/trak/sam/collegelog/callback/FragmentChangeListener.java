package com.trak.sam.collegelog.callback;

import android.support.v4.app.Fragment;
import android.view.View;

public interface FragmentChangeListener {
    public void replaceFragment(Fragment fragment);
    public void onClickedView(View view);
}
