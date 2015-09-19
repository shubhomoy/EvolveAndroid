package com.evolve.evolve.EvolveActivities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.evolve.evolve.EvolveActivities.EvolveUtilities.ImageManipulator;
import com.evolve.evolve.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView previewImageView;
    private String file_name;
    private Intent mainIntent;
    private double latitude;
    private double longitude;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    protected LocationManager locationManager;
    public Location location;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        instantiate();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean choice) {
                if(choice){
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if(isGPSEnabled && isNetworkEnabled) {
                        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d("gps",String.valueOf(latitude)+ " " + String.valueOf(longitude));
                    }
                    else{
                        showSettingsAlert();
                        checkBox.setChecked(false);
                    }
                }
            }
        });
        file_name=mainIntent.getStringExtra("file");
        Bitmap bitmap= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/Evolve/temp/img_"+file_name+".jpg");
        previewImageView.setImageBitmap(bitmap);


    }


    private void instantiate(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        previewImageView=(ImageView)findViewById(R.id.image_temp);
        mainIntent = getIntent();
        checkBox=(CheckBox)findViewById(R.id.loc);
        locationManager=(LocationManager) this.getSystemService(LOCATION_SERVICE);
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

//                try {
//                    ImageManipulator.writeExifInfo(Environment.getExternalStorageDirectory().toString()+"/Evolve/img_"+file_name+".jpg", 1);
//                } catch (IOException e) {
//
//                }

                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.create().show();
    }



}
