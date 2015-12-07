package com.evolve.evolve.EvolveActivities.EvolveObjects;

import java.util.ArrayList;

/**
 * Created by vellapanti on 4/12/15.
 */
public class Doctor {
    public int id;
    public String name;
    public String reg_id;
    public String photo;
    public String address;
    public String email;
    public String description;
    public String created_at;
    public String update_at;

    public ArrayList<Contacts> contacts;
    public ArrayList<Clinic> clinics;
    public ArrayList<Specializations> specializations;
    public class Contacts{
        public String contact_no;
    }
}
