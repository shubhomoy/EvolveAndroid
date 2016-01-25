package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolve.evolve.EvolveActivities.DoctorSearchActivity;
import com.evolve.evolve.EvolveActivities.SchoolFilterActivity;
import com.evolve.evolve.R;

import java.util.ArrayList;

/**
 * Created by vellapanti on 2/12/15.
 */
public class QuickListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    Context context;
    ArrayList<String> helpList;

    private static final int[] icons = {R.mipmap.ic_local_hospital_black_24dp,
            R.mipmap.ic_school_black_24dp};

    public QuickListAdapter(Context context, ArrayList<String> helpList) {
        this.context = context;
        this.helpList = helpList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.custom_quicklistview, parent, false);
        QuickListViewHolder quickListViewHolder = new QuickListViewHolder(view);

        return quickListViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        QuickListViewHolder viewHolder = (QuickListViewHolder) holder;
        viewHolder.quicklist.setText(helpList.get(position).toString());
        viewHolder.itemLogo.setImageResource(icons[position]);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        Intent doctorIntent = new Intent(context, DoctorSearchActivity.class);
                        context.startActivity(doctorIntent);
                        break;
                    case 1:
                        Intent schoolIntent = new Intent(context, SchoolFilterActivity.class);
                        context.startActivity(schoolIntent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    class QuickListViewHolder extends RecyclerView.ViewHolder {
        private TextView quicklist;
        private ImageView itemLogo;
        private CardView cardView;

        public QuickListViewHolder(View itemView) {
            super(itemView);
            quicklist = (TextView) itemView.findViewById(R.id.quicklist_item);
            itemLogo = (ImageView) itemView.findViewById(R.id.item_logo);
            cardView = (CardView) itemView.findViewById(R.id.card_root);
        }
    }
}
