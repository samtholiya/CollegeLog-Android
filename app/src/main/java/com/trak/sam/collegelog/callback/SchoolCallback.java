package com.trak.sam.collegelog.callback;

import com.trak.sam.collegelog.model.School;

public interface SchoolCallback {
    public void onSchoolReceived(School school);
    public void onSchoolsReceived(School[] schools);
    public void onFailed(Exception e);
}
