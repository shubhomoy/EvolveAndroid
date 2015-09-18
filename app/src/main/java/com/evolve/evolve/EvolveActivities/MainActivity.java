package com.evolve.evolve.EvolveActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.evolve.evolve.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import EvolveAdapters.GalleryAdapter;
import EvolveAdapters.MainpagePagerAdapter;
import EvolveFragments.GalleryFragment;
import EvolveFragments.QuickListFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private FloatingActionButton fab1, cameraBtn;
    private FloatingActionsMenu fabMenu;
    private ArrayList<Fragment> pageList;
    private MainpagePagerAdapter adapter;
    private File image_file;
    public String timeStamp;
    private GalleryFragment galleryFragment;

    private int CAMERA_CAPTURE_TAG = 0;
    private int PREVIEW_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiate();
        adapter = new MainpagePagerAdapter(getSupportFragmentManager(), pageList);
        pager.setAdapter(adapter);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "SortByName", Toast.LENGTH_SHORT).show();

            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_process();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        fabMenu.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        fabMenu.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void instantiate() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pageList = new ArrayList<>();
        galleryFragment = new GalleryFragment();
        pageList.add(galleryFragment);
        pageList.add(new QuickListFragment());

        fabMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        fab1 = (FloatingActionButton) findViewById(R.id.action_a);
        cameraBtn = (FloatingActionButton) findViewById(R.id.action_b);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search)
            Toast.makeText(this, "Yet to come", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}