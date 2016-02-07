package com.evolve.evolve.EvolveActivities.EvolveObjects;

import java.util.ArrayList;

/**
 * Created by shubhomoy on 26/1/16.
 */
public class School {

    public int id;
    public String name;
    public String description;
    public String fee_structure;
    public String logo_url;
    public String contact_no;
    public String email;
    public String x_coord;
    public String y_coord;
    public String address;
    public String principal_name;
    public ArrayList<Affiliation> affiliations;
    public ArrayList<Type> types;

    public class Affiliation {
        public String id;
        public String affiliation;
    }

    public class Type {
        public String id;
        public String type_name;
    }
}
