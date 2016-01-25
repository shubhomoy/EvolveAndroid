package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.evolve.evolve.EvolveActivities.EvolveObjects.Clinic;
import com.evolve.evolve.R;

import java.util.ArrayList;


/**
 * Created by ddvlslyr on 5/12/15.
 */
public class ClinicAdapter extends ArrayAdapter<String> {

    private Context context;
    ArrayList<Clinic> list;

    public ClinicAdapter(Context context, ArrayList<Clinic> clinic, ArrayList<String> slist) {
        super(context, R.layout.custom_doctor_search, R.id.doc_name, slist);
        this.context = context;
        this.list = clinic;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_doctor_search, parent, false);
        }

        TextView clinicName = (TextView) convertView.findViewById(R.id.doc_name);
        TextView clinicAdd = (TextView) convertView.findViewById(R.id.doc_clinic);

        clinicName.setText(list.get(position).name);
        clinicAdd.setText(list.get(position).address);
        return convertView;
    }
}
