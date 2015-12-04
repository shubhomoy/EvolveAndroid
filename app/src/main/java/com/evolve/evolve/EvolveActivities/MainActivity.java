package com.evolve.evolve.EvolveActivities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.MainpagePagerAdapter;
import com.evolve.evolve.EvolveActivities.EvolveFragments.GalleryFragment;
import com.evolve.evolve.EvolveActivities.EvolveFragments.QuickListFragment;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolveDatabase;
import com.evolve.evolve.EvolveActivities.EvolveUtilities.EvolvePreferences;
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

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    FloatingActionButton uploadBtn, cameraBtn;
    public FloatingActionsMenu fabMenu;
    ArrayList<Fragment> pageList;
    MainpagePagerAdapter adapter;
    File image_file;
    String timeStamp;
    GalleryFragment galleryFragment;
    QuickListFragment quickListFragment;
    EvolveDatabase evolveDatabase;
    EvolvePreferences prefs;


    int CAMERA_CAPTURE_TAG = 0;
    int PREVIEW_TAG = 1;
    int RESULT_LOAD_IMG=2;

    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pageList = new ArrayList<>();
        galleryFragment = new GalleryFragment();
        quickListFragment=new QuickListFragment();
        pageList.add(galleryFragment);
        pageList.add(quickListFragment);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        uploadBtn = (FloatingActionButton) findViewById(R.id.action_a);
        cameraBtn = (FloatingActionButton) findViewById(R.id.action_b);
        evolveDatabase=new EvolveDatabase(this);
        prefs = new EvolvePreferences(this);
        adapter = new MainpagePagerAdapter(getSupportFragmentManager(), pageList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instantiate();

        pager.setAdapter(adapter);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload_pic=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fabMenu.collapse();
                startActivityForResult(upload_pic, RESULT_LOAD_IMG);
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                image_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Evolve/temp/img_" + timeStamp + ".jpg");
                Uri uri = Uri.fromFile(image_file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                fabMenu.collapse();
                startActivityForResult(intent, CAMERA_CAPTURE_TAG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_TAG && resultCode == RESULT_OK) {
            if (image_file.exists()) {
                Intent in = new Intent(MainActivity.this, PreviewActivity.class);
                in.putExtra("file", timeStamp);
                startActivityForResult(in, PREVIEW_TAG);
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