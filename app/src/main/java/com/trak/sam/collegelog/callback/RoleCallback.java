package com.trak.sam.collegelog.callback;

import com.trak.sam.collegelog.model.Role;

public interface RoleCallback {
    public void onRoleReceived(Role role);
    public void onRolesReceived(Role[] roles);
    public void onFailed(Exception e);
}
