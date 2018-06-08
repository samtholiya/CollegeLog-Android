/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trak.sam.collegelog.model;

import java.util.Date;

/**
 *
 * @author shubh
 */
public class User {

    public Long id;
    public String firstName;
    public String lastName;
    public String userName;
    public String password;
    public Date dateOfBirth;
    public String mobile;
    public String email;
    public String address;
    public boolean isAdmin;
    public boolean isActive;
    public Department[] departments;
    public Role role;
}

