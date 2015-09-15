package com.evolve.evolve.EvolveActivities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.evolve.evolve.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import EvolveAdapters.MainpagePagerAdapter;
import EvolveFragments.GalleryFragment;
import EvolveFragments.QuickListFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager pager;
    private FloatingActionButton fab1,fab2;
    private FloatingActionsMenu fabMenu;
    private ArrayList<Fragment> list;
    private MainpagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiate();

        adapter=new MainpagePagerAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(adapter);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "SortByName", Toast.LENGTH_SHORT).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"SortByAddress",Toast.LENGTH_SHORT).show();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
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

    private void instantiate(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager= (ViewPager) findViewById(R.id.view_pager);
        list=new ArrayList<>();
        list.add(new GalleryFragment());
        list.add(new QuickListFragment());

        fabMenu= (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        fab1= (FloatingActionButton) findViewById(R.id.action_a);
        fab2= (FloatingActionButton) findViewById(R.id.action_b);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.search)
            Toast.makeText(this,"Yet to come",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}