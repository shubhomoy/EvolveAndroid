package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by ddvlslyr on 15/9/15.
 */
public class MainpagePagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> list;

    public MainpagePagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
