package com.trak.sam.collegelog;

import android.app.Application;

import com.trak.sam.collegelog.service.DepartmentService;
import com.trak.sam.collegelog.service.SchoolService;
import com.trak.sam.collegelog.service.UserService;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UserService.setContext(getApplicationContext());
        SchoolService.setContext(getApplicationContext());
        DepartmentService.setContext(getApplicationContext());
    }
}
