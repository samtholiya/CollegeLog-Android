package com.trak.sam.collegelog.callback;

import com.trak.sam.collegelog.model.User;

public interface UserCallback {
    public void onUserReceived(User user);
    public void onUsersReceived(User[] users);
    public void onFailed(Exception e);
}
