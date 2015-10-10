package com.evolve.evolve.EvolveActivities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.evolve.evolve.EvolveActivities.EvolveObjects.Image;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.Config;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveDatabase;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveRequest;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.ImageManipulator;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.VolleySingleton;
import com.evolve.evolve.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.MainpagePagerAdapter;
import com.evolve.evolve.EvolveActivities.EvolveFragments.GalleryFragment;
import com.evolve.evolve.EvolveActivities.EvolveFragments.QuickListFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private FloatingActionButton uploadBtn, cameraBtn;
    private FloatingActionsMenu fabMenu;
    private ArrayList<Fragment> pageList;
    private MainpagePagerAdapter adapter;
    private File image_file;
    public String timeStamp;
    private GalleryFragment galleryFragment;
    private int CAMERA_CAPTURE_TAG = 0;
    private int PREVIEW_TAG = 1;
    private int RESULT_LOAD_IMG=2;
    EvolveDatabase evolveDatabase;
    ArrayList<Image> imageList;
    EvolvePreferences prefs;

    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pageList = new ArrayList<>();
        imageList = new ArrayList<Image>();
        galleryFragment = new GalleryFragment();
        pageList.add(galleryFragment);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        uploadBtn = (FloatingActionButton) findViewById(R.id.action_a);
        cameraBtn = (FloatingActionButton) findViewById(R.id.action_b);
        evolveDatabase=new EvolveDatabase(this);
        prefs = new EvolvePreferences(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchFromServer();
        instantiate();


        adapter = new MainpagePagerAdapter(getSupportFragmentManager(), pageList);
        pager.setAdapter(adapter);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload_pic=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(upload_pic, RESULT_LOAD_IMG);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_process();
            }
        });

    }


    //In this function the camera is opened and a pic is clicked and the file name is created
    void fetchFromServer() {
        String url = Config.apiUrl+"/api/fetch/all";
        EvolveRequest evolveRequest = new EvolveRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                evolveDatabase.open();
                try {
                    JSONArray arr = new JSONArray(response.getString("data"));
                    Gson gson = new Gson();
                    imageList.removeAll(imageList);
                    imageList.clear();
                    File dir = new File(Environment.getExternalStorageDirectory().toString()+"/Evolve");
                    if(!dir.exists())
                        dir.mkdir();
                    String [] files = dir.list();
                    ImageManipulator manipulator = new ImageManipulator();

                    for(int i=0; i<arr.length(); i++) {
                        final Image image = gson.fromJson(arr.getJSONObject(i).toString(), Image.class);
                        imageList.add(image);
                        boolean flag = false;
                        for(int j=0; j<files.length; j++) {
                            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+files[j]);
                            if(!file.isDirectory()) {
                                try {
                                    if(manipulator.readExifInfo(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+files[j]) == image.id) {
                                        flag = true;
                                        break;
                                    }
                                } catch (Exception e) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if(!flag) {
                            String url = Config.apiUrl+"/images/"+prefs.getId()+"/"+image.id+".jpg";
                            ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    FileOutputStream fout = null;
                                    try {
                                        fout = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+image.id+".jpg");
                                        response.compress(Bitmap.CompressFormat.JPEG, 100, fout);
                                    } catch (FileNotFoundException e) {

                                    }finally {
                                        try {
                                            fout.close();
                                        } catch (IOException e) {

                                        }
                                    }
                                }
                            }, 0, 0, null, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("option", "Unable to download image");
                                }
                            });
                            VolleySingleton.getInstance().getRequestQueue().add(imageRequest);
                        }
                        if(!evolveDatabase.checkExif(image)) {
                            evolveDatabase.insertInformation(image);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    galleryFragment.refreshGallery();
                } catch (JSONException e) {

                }
                evolveDatabase.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                galleryFragment.refreshGallery();
            }
        }, this);
        VolleySingleton.getInstance().getRequestQueue().add(evolveRequest);
    }

    public void camera_process() {
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File evolve = new File(Environment.getExternalStorageDirectory(), "Evolve");
        if (!evolve.exists()) {
            evolve.mkdir();
        }
        File temp = new File(evolve, "temp");
        if (!temp.exists()) {
            temp.mkdir();
        }
        image_file = new File(Environment.getExternalStoragePublicDirectory("Evolve/temp"), "img_" + timeStamp + ".jpg");
        Uri uri = Uri.fromFile(image_file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, CAMERA_CAPTURE_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_TAG && resultCode == RESULT_OK) {
            if (image_file.exists()) {
                Intent in = new Intent(MainActivity.this, PreviewActivity.class);
                in.putExtra("file", timeStamp);
                startActivityForResult(in, PREVIEW_TAG);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else if(requestCode == PREVIEW_TAG && resultCode == RESULT_OK) {
            galleryFragment.refreshGallery();
        }
        else if(requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK){
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Uri gallery_image=data.getData();
            String[] imageselected={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(gallery_image, imageselected, null, null, null);
            cursor.moveToFirst();
            int colindex=cursor.getColumnIndex(imageselected[0]);
            String image_path_gallery=cursor.getString(colindex);
            cursor.close();
            File source= new File(image_path_gallery);
            File destination =new File(Environment.getExternalStorageDirectory().toString()+"/Evolve/temp/img_"+timeStamp+".jpg");

            InputStream in = null;
            OutputStream out=null;
            try {
                in = new FileInputStream(source);
                out= new FileOutputStream(destination);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent i =new Intent(MainActivity.this,PreviewActivity.class);
            i.putExtra("file",timeStamp);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit)
            finish();
        return super.onOptionsItemSelected(item);
    }
}