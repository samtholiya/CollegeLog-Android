package com.trak.sam.collegelog.callback;

import android.support.v4.app.Fragment;
import android.view.View;

public interface FragmentChangeListener {
    void replaceFragment(Fragment fragment);
    void onClickedView(View view);
    void setAddButtonListener(OnAddButtonClick onAddButtonClick);
}
