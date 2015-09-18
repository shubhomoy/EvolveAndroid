package EvolveFragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evolve.evolve.R;

import java.io.File;
import java.util.ArrayList;

import EvolveAdapters.GalleryAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private RecyclerView recyclerView_images;
    private GalleryAdapter galleryAdapter;
    private ArrayList<String> file_names;

    public GalleryFragment() {
        // Required empty public constructor
    }

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
        recyclerView_images.setHasFixedSize(true);
        recyclerView_images.setAdapter(galleryAdapter);
        recyclerView_images.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        refreshGallery();
        return v;
    }
}
