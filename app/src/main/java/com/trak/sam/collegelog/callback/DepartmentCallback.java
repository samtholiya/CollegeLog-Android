package com.trak.sam.collegelog.callback;

import com.trak.sam.collegelog.model.Department;

public interface DepartmentCallback {
    public void onDepartmentReceived(Department user);
    public void onDepartmentsReceived(Department[] users);
    public void onFailed(Exception e);
}
