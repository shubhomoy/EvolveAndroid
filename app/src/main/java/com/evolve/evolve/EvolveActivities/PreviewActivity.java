package com.evolve.evolve.EvolveActivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.evolve.evolve.EvolveActivities.EvolveObjects.Image;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveDatabase;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.ImageManipulator;
import com.evolve.evolve.R;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PreviewActivity extends AppCompatActivity implements LocationListener {

    private Toolbar toolbar;
    private ImageView previewImageView;
    private String file_name;
    private Intent mainIntent;
    private double latitude;
    private double longitude;
    protected LocationManager locationManager;
    public Location location;
    CheckBox checkBox;
    EvolvePreferences prefs;
    ProgressDialog pDialog;
    private EditText description;
    private final int NAVIGATION_TAG = 1;
    private String img_date;
    public  EvolveDatabase database;
    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload");

        previewImageView = (ImageView) findViewById(R.id.image_temp);
        mainIntent = getIntent();
        checkBox = (CheckBox) findViewById(R.id.loc);
        prefs = new EvolvePreferences(this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        description=(EditText)findViewById(R.id.des);
        database=new EvolveDatabase(this);
    }
// This function is called when the user chooses to
// share his location and then his latitude and longitude is captured
    void shareLocation(final String provider) {
        if (provider.equals("network")) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000 * 60, 10, PreviewActivity.this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 60, 300, PreviewActivity.this);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = true;
                while (flag) {
                    if (provider.equals("network"))
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    else
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        flag = false;
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
                if (!flag) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PreviewActivity.this, "Lat : " + latitude + " Lon : " + longitude, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        instantiate();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean choice) {
                if (choice) {
                    boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!isGPSEnabled && !isNetworkEnabled) {
                        showSettingsAlert();
                        checkBox.setChecked(false);
                    } else {
                        if (isNetworkEnabled) {
                            shareLocation("network");
                        } else if (isGPSEnabled) {
                            shareLocation("geo");
                        }
                    }
                }
            }
        });

        file_name = mainIntent.getStringExtra("file");
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString() + "/Evolve/temp/img_" + file_name + ".jpg");
        previewImageView.setImageBitmap(bitmap);
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

            case android.R.id.home:
                finish();
                break;

            case R.id.done:
                File source = new File(Environment.getExternalStoragePublicDirectory("Evolve/temp"), "img_" + file_name + ".jpg");
                File destination = new File(Environment.getExternalStoragePublicDirectory("Evolve/"), "img_" + file_name + ".jpg");
                source.renameTo(destination);
                img_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                new UploadPictureHttp(description.getText().toString(),img_date,file_name,String.valueOf(longitude),String.valueOf(latitude)).execute(Environment.getExternalStorageDirectory().toString()+"/Evolve/img_" + file_name + ".jpg");

                setResult(RESULT_OK);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
//This function is called when the GPS of the user is disabled and he chooses to share his location

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PreviewActivity.this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, NAVIGATION_TAG);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.create().show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

//This class is responsible for the uploading of the pic to the server.
    private class UploadPictureHttp extends AsyncTask<String, String, String> {
        String desc;
        String date;
        String name;
        String lon;
        String lat;
        public UploadPictureHttp(String desc,String date,String name,String lon ,String lat){
            this.desc=desc;
            this.date=date;
            this.name=name;
            this.lon=lon;
            this.lat=lat;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            Config config = new Config();
            String res = null;
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 60000);
            HttpConnectionParams.setSoTimeout(params, 60000);
            HttpClient client = new DefaultHttpClient(params);
            HttpPost post = new HttpPost(config.apiUrl + "/api/upload");
            post.setHeader("id", String.valueOf(prefs.getId()));
            post.setHeader("access_token", prefs.getAccessToken());
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                entity.addPart("image_description", new StringBody(desc));
                entity.addPart("image_date", new StringBody(date));
                entity.addPart("image_name", new StringBody(name));
                entity.addPart("image_lon", new StringBody(lon));
                entity.addPart("image_lat", new StringBody(lat));
            } catch (UnsupportedEncodingException e) {
                return null;
            }
            entity.addPart("image", new FileBody(new File(strings[0])));
            post.setEntity(entity);
            try {
                HttpResponse response = client.execute(post);
                res = EntityUtils.toString(response.getEntity());
                return res;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Image image = gson.fromJson(jsonObject.getString("data"), Image.class);
                    ImageManipulator manipulator = new ImageManipulator();
                    try {
                        manipulator.writeExifInfo(Environment.getExternalStorageDirectory().toString()+"/Evolve/img_" + file_name + ".jpg", image.id);
                        database.open();
                        database.insertInformation(image);
                        database.close();
                    } catch (IOException e) {
                        Log.d("option", "unable to write EXIF tag");
                    }
                } catch (JSONException e) {

                }
            } else {
                Toast.makeText(PreviewActivity.this, "Connection Timeout", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
