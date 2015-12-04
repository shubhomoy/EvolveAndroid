package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.evolve.R;

import java.util.ArrayList;

/**
 * Created by ddvlslyr on 2/12/15.
 */
public class DoctorSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    LayoutInflater inflater;

    public DoctorSearchAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.custom_doctor_search,parent,false);
        DoctorViewHolder doctorViewHolder=new DoctorViewHolder(view);
        return doctorViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {




    }

    @Override
    public int getItemCount() {
        return 0;
    }


    static class DoctorViewHolder extends RecyclerView.ViewHolder {

        private TextView docName;
        private TextView docClinic;

        public DoctorViewHolder(View itemView) {
            super(itemView);

            docName = (TextView) itemView.findViewById(R.id.doc_name);
            docClinic = (TextView) itemView.findViewById(R.id.doc_clinic);
        }
    }
}
