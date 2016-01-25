package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evolve.evolve.EvolveActivities.EvolveObjects.School;
import com.evolve.evolve.R;

import java.util.ArrayList;

/**
 * Created by vellapanti on 25/1/16.
 */
public class SchoolSearchAdapter extends RecyclerView.Adapter<SchoolSearchAdapter.SchoolSearchViewHolder> {

    private Context context;
    private ArrayList<School> schools;

    public SchoolSearchAdapter(Context context, ArrayList<School> schools) {
        this.context = context;
        this.schools = schools;

    }

    @Override
    public SchoolSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_school_search_item, parent, false);
        return new SchoolSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SchoolSearchViewHolder holder, int position) {


        holder.schoolName.setText(schools.get(position).name);
        String url = schools.get(position).logo_url;
        Glide.with(context).load(url).into(holder.schoolImage);

    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    static class SchoolSearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView schoolImage;
        private TextView schoolName;

        public SchoolSearchViewHolder(View itemView) {
            super(itemView);

            schoolImage = (ImageView) itemView.findViewById(R.id.school_img);
            schoolName = (TextView) itemView.findViewById(R.id.school_name);
        }
    }
}
