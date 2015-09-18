package com.evolve.evolve.EvolveActivities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.evolve.evolve.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView previewImageView;
    private String file_name;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        instantiate();
        file_name=mainIntent.getStringExtra("file");
        Bitmap bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/Evolve/temp/img_"+file_name+".jpg");
        previewImageView.setImageBitmap(bitmap);
    }


    private void instantiate(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        previewImageView=(ImageView)findViewById(R.id.image_temp);
        mainIntent = getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.done:
                File source=new File (Environment.getExternalStoragePublicDirectory("Evolve/temp"),"img_"+file_name+".jpg");
                File destination=new File (Environment.getExternalStoragePublicDirectory("Evolve/"),"img_"+file_name+".jpg");
                source.renameTo(destination);
                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
