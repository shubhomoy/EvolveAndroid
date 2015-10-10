package com.evolve.evolve.EvolveActivities.EvolveFragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.evolve.evolve.EvolveActivities.MainActivity;
import com.evolve.evolve.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.evolve.evolve.EvolveActivities.EvolveAdapters.GalleryAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    RecyclerView recyclerView_images;
    TextView emptyView;
    GalleryAdapter galleryAdapter;
    ArrayList<String> file_names;
    EvolveDatabase evolveDatabase;
    ArrayList<Image> imageList;
    EvolvePreferences prefs;

    boolean hasImages;

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
                hasImages = true;
                file_names.add(list_of_files[i].getName());
            }
        }
        galleryAdapter.notifyDataSetChanged();
        if (!hasImages) {
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
        evolveDatabase=new EvolveDatabase(getActivity());
        imageList = new ArrayList<Image>();
        prefs = new EvolvePreferences(getActivity());
        hasImages = false;
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
        fetchFromServer();

        recyclerView_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).fabMenu.collapse();
            }
        });
        return v;
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
                    final ImageManipulator manipulator = new ImageManipulator();

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
                                        manipulator.writeExifInfo(Environment.getExternalStorageDirectory().toString()+"/Evolve/"+image.id+".jpg" ,image.id);
                                    } catch (FileNotFoundException e) {

                                    } catch (IOException e) {

                                    } finally {
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
                    galleryAdapter.notifyDataSetChanged();
                    refreshGallery();
                } catch (JSONException e) {

                }
                evolveDatabase.close();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshGallery();
            }
        }, getActivity());
        VolleySingleton.getInstance().getRequestQueue().add(evolveRequest);
    }
}
