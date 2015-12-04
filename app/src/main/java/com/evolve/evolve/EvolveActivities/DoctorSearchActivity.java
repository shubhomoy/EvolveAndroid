package com.evolve.evolve.EvolveActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.evolve.evolve.R;

public class DoctorSearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);

        instantiate();
        //searchView.setAdapter();
    }

    private void instantiate(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView= (RecyclerView) findViewById(R.id.search_result_view);
        searchView.setHasFixedSize(true);
        searchView.setLayoutManager(new LinearLayoutManager(this));
    }
}
