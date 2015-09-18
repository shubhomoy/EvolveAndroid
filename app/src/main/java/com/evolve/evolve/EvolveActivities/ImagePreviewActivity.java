package com.evolve.evolve.EvolveActivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.evolve.evolve.R;

/**
 * Created by shubhomoy on 18/9/15.
 */
public class ImagePreviewActivity extends AppCompatActivity {

    ImageView imageView;
    TextView descriptionTv;
    Intent mainIntent;

    void instantiate() {
        imageView = (ImageView)findViewById(R.id.imageview);
        descriptionTv = (TextView)findViewById(R.id.description_tv);
        mainIntent = getIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        instantiate();

        String filename = mainIntent.getStringExtra("filename");
        Glide.with(this).load(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+filename).into(imageView);
    }
}
