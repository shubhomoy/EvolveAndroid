package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.evolve.R;

import java.io.File;
import java.util.ArrayList;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.GalleryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView_images;
    private TextView emptyView;
    private GalleryAdapter galleryAdapter;
    private ArrayList<String> file_names;

    public GalleryFragment() {
        // Required empty public constructor
    }
//This function is called whenever a new pic is uploaded (to refresh the main gallery)
    public void refreshGallery() {
        file_names.removeAll(file_names);
        file_names.clear();
        File evolve = new File(Environment.getExternalStorageDirectory(), "Evolve");
        if(!evolve.exists()){
            evolve.mkdir();
        }
        File folder=new File(Environment.getExternalStorageDirectory(), "Evolve");
        File[] list_of_files=folder.listFiles();

        for (int i=0;i<list_of_files.length;i++){
            if(list_of_files[i].isFile()){
                file_names.add(list_of_files[i].getName());
            }
        }
        galleryAdapter.notifyDataSetChanged();
        if (list_of_files.length==0) {
            recyclerView_images.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else {
            recyclerView_images.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file_names=new ArrayList<String>();
        galleryAdapter=new GalleryAdapter(getActivity(),file_names);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView_images=(RecyclerView)v.findViewById(R.id.recview_images);
        emptyView= (TextView) v.findViewById(R.id.empty_recyclerview);
        recyclerView_images.setHasFixedSize(true);
        recyclerView_images.setAdapter(galleryAdapter);
        recyclerView_images.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        refreshGallery();
        return v;
    }
}
