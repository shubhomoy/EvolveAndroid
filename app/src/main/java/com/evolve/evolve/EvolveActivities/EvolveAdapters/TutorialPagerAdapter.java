package com.evolve.evolve.EvolveActivities.EvolveAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by ddvlslyr on 16/9/15.
 */
public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> list;

    public TutorialPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
