package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evolve.evolve.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuickListFragment extends Fragment {


    public QuickListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quick_list, container, false);
    }


}
