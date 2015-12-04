package com.evolve.evolve.EvolveActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.evolve.evolve.EvolveActivities.EvolveObjects.Image;
import com.evolve.evolve.R;

public class DoctorDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView doctorImage;
    private TextView docName, docAddress, docPhone, docDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        instantiate();

    }

    private void instantiate(){

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doctorImage= (ImageView) findViewById(R.id.doctor_photo);
        docName= (TextView) findViewById(R.id.doc_name);
        docAddress= (TextView) findViewById(R.id.doctor_address);
        docPhone= (TextView) findViewById(R.id.doctor_phone);
        docDesc= (TextView) findViewById(R.id.doctor_details);


    }
}
