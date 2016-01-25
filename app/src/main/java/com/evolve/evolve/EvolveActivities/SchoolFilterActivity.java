package com.evolve.evolve.EvolveActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.evolve.evolve.R;

import java.util.ArrayList;

public class SchoolFilterActivity extends AppCompatActivity {

    private Context context;
    private Toolbar toolbar;
    private Spinner affSpinner, typeSpinner;
    private FloatingActionButton fab;
    private ArrayList<String> affilationList, schoolTypeList;
    private ArrayAdapter<String> affilAdapter, catAdapter;

    void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Filter School");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = SchoolFilterActivity.this;
        affSpinner = (Spinner) findViewById(R.id.affilation);
        typeSpinner = (Spinner) findViewById(R.id.type);
        fab = (FloatingActionButton) findViewById(R.id.done);
        affilationList = new ArrayList<>();
        schoolTypeList = new ArrayList<>();

        affilationList.add("ICSE");
        affilationList.add("CBSE");
        affilationList.add("OBSE");
        affilationList.add("WBSE");

        schoolTypeList.add("Private");
        schoolTypeList.add("Govt.");
        schoolTypeList.add("Semi-Private");

        affilAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, affilationList);
        affilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schoolTypeList);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_filter);

        instantiate();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SchoolSearchActivity.class));
            }
        });

        affSpinner.setAdapter(affilAdapter);
        affSpinner.setPrompt("Choose affilation");
        typeSpinner.setAdapter(catAdapter);
        typeSpinner.setPrompt("Choose school type");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_school_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
