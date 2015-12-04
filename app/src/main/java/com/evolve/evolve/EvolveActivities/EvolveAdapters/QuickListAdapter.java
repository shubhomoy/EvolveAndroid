package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.evolve.R;

/**
 * Created by vellapanti on 2/12/15.
 */
public class QuickListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater inflater;
    Context context;
    public QuickListAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view=inflater.inflate(R.layout.custom_quicklistview,parent,false);
        QuickListViewHolder quickListViewHolder=new QuickListViewHolder(view);

        return quickListViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuickListViewHolder viewHolder=(QuickListViewHolder)holder;
        viewHolder.quicklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class QuickListViewHolder extends RecyclerView.ViewHolder{
        TextView quicklist;
        public QuickListViewHolder(View itemView) {
            super(itemView);
            quicklist=(TextView)itemView.findViewById(R.id.quicklist_item);
        }
    }
}
