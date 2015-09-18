package com.evolve.evolve.EvolveActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
import com.evolve.evolve.R;

import java.util.ArrayList;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.TutorialPagerAdapter;
import com.evolve.evolve.EvolveActivities.EvolveFragments.RegistrationFragment;
import com.evolve.evolve.EvolveActivities.EvolveFragments.Tutorialfragment;

public class RegistrationActivity extends AppCompatActivity {

    ViewPager pager;
    ArrayList<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        instantiate();

        TutorialPagerAdapter adapter = new TutorialPagerAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);
    }

    private void instantiate() {
        pager = (ViewPager) findViewById(R.id.view_pager);
        list = new ArrayList<Fragment>();
        for (int i = 0; i < 3; i++)
            list.add(new Tutorialfragment());
        list.add(new RegistrationFragment());
        EvolvePreferences preferences = new EvolvePreferences(this);
        if(!preferences.getAccessToken().isEmpty()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
