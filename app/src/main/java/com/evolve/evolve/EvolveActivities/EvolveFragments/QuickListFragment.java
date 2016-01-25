package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.QuickListAdapter;
import com.evolve.evolve.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickListFragment extends Fragment {
    RecyclerView recyclerView_quicklist;
    QuickListAdapter quickListAdapter;
    ArrayList<String> helpList;
    public QuickListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helpList = new ArrayList<>();
        helpList.add("Find a Doctor");
        helpList.add("Find a School");
        quickListAdapter=new QuickListAdapter(getActivity(),helpList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_quick_list,container,false);
        recyclerView_quicklist=(RecyclerView)v.findViewById(R.id.recview_quicklist);
        recyclerView_quicklist.setAdapter(quickListAdapter);
        recyclerView_quicklist.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }


}
